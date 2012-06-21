import org.springframework.context.NoSuchMessageException
import org.springframework.context.MessageSource
import eliza.ElizaMain
import org.springframework.beans.factory.InitializingBean
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.springframework.context.MessageSourceAware

class ChatService  implements InitializingBean, GrailsApplicationAware, MessageSourceAware {

	boolean transactional = true
	def scope = 'singleton'

	def grailsApplication
	TwitterService twitterService
	MessageSource messageSource
	def chatHandlers

	def conversationExpiracy
	def defaultLocale
	def defaultHumour

    def conversations = [:]

    @Override
    void afterPropertiesSet() {
        conversationExpiracy = grailsApplication.config['conversationExpiracy']
        defaultLocale = new Locale(grailsApplication.config['jenna']['defaultLanguage'])
        defaultHumour = grailsApplication.config['jenna']['defaultHumour']
    }

    def handleRequest(Request _request) {
        if (!_request.user) {
            return expressResponse(Response.build('unknownUser'))
        }
        Conversation conversation = getConversationForUser(_request.user)
        conversation.actualRequest = _request

        // Searchs within previous registered handlers until it finds the first that accept this conversation.
        // It will evaluate the accept method, expecting a boolean value.
        RequestHandler handler = chatHandlers.find { RequestHandler handler ->
            handler.accepts(conversation)
        }
        if (handler) {
            handler.handle(conversation, this)
            conversation.actualRequest.user.save(flush: true) // flush: true --> Tells Hibernate to execute the query now!
            return expressResponse(conversation)
        } else {
            conversation.responses << Response.build('unknownCommand')
            return expressResponse(conversation)
        }
    }

    def requestUserToInviteCoworker(User _user) {
        if (!_user.enabled) {
            return defaultHumour.getMessagePattern(defaultLocale, 'requestInactiveUser')
        }
        Conversation conversation = getConversationForUser(_user)
        conversation.actualRequest = new Request(user: _user, message: 'dummy request')

        conversation.context.inviteCoworkers1=true

        return handleRequest(conversation.actualRequest);
    }


    def requestTracking(User _user) {
        if (!_user.enabled) {
            return defaultHumour.getMessagePattern(defaultLocale, 'requestInactiveUser')
        }
        Conversation conversation = getConversationForUser(_user)
        conversation.actualRequest = new Request(user: _user, message: 'dummy request')

        def assignments = _user.listActiveAssignments()
        if (assignments) {
            Queue queue = new LinkedList(assignments*.id)
        }

        conversation.context.salutateStep1 = true
        return handleRequest(conversation.actualRequest);
    }

    def remind(Reminder _reminder) {
        Conversation conversation = getConversationForUser(_reminder.user)
        conversation.actualRequest = new Request(user: _reminder.user, message: 'dummy request')
        conversation.responses << new Response(text: _reminder.what)
        return expressResponse(conversation)
    }

    def askForEfforts(User _user, Date _date) {
        Conversation conversation = getConversationForUser(_user)
        conversation.actualRequest = new Request(user: _user, message: 'dummy request')
        conversation.responses << Response.build('askForEfforts', [_date] as Object[])
        return expressResponse(conversation)
    }

    def twit(String _message) {
        twitterService.twit(_message)
    }

    def remindEvent(Event _event, User _participant) {
        Conversation conversation = getConversationForUser(_participant)
        conversation.actualRequest = new Request(user: _participant, message: 'dummy request')
        conversation.responses << Response.build('remindEvent', [_event.name, _event.startDate])
        return expressResponse(conversation)
    }

    def getConversationForUser(User _user) {
        Conversation conversation = conversations[_user.account]
        if (!conversation) {
            conversation = new Conversation()
            conversation.humour = _user.humour ?: defaultHumour
            conversation.handlers = chatHandlers
            conversation.messageSource = messageSource
            ElizaMain eliza = new ElizaMain()
            InputStream script = this.class.classLoader.getResourceAsStream('eliza/script.txt')
            eliza.readScript(script)
            conversation.eliza = eliza
            conversations[_user.account] = conversation
        }
        return conversation
    }

    def forgetExpiredConversations() {
        long now = System.currentTimeMillis()
        conversations.each { String account, Conversation conversation ->
            if (now - conversation.lastMessageTime > conversationExpiracy) {
                conversations.remove(account)
            }
        }
    }

    def expressResponse(Conversation _conversation) {
        def response = expressResponse(_conversation.responses, _conversation.humour, _conversation.locale)
        _conversation.responses.clear()
        return response
    }

    def expressResponse(responses, humourName = defaultHumour, locale = defaultLocale) {
        if (responses) {
            def lines = []
            responses.each { Response response ->
                if (response.text) {
                    lines << response.text
                } else {
                    try {
                        def codes = response.message.codes.collect { humourName + '.' + it}
                        lines << messageSource.getMessage(Response.build(codes as String[], response.message.arguments).message, locale)
                    } catch (NoSuchMessageException ex) {
                        //fallback to default if no message for the actual humour
                        lines << messageSource.getMessage(response.message, locale)
                    }
                }
            }
            return lines.join('\n')
        } else {
            return messageSource.getMessage(Response.build(humourName + '.unknownCommand').message, locale)
        }
    }

	@Override
	public void setGrailsApplication(GrailsApplication grailsApplication) {
		this.grailsApplication = grailsApplication
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource
	}
}
