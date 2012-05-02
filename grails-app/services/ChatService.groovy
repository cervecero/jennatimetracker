import org.springframework.context.NoSuchMessageException
import org.springframework.context.MessageSource
import eliza.ElizaMain
import org.springframework.beans.factory.InitializingBean

class ChatService implements InitializingBean {

    def grailsApplication

    def conversationExpiracy

    boolean transactional = true
    def scope = 'singleton'

    def conversations = [:]
    def handlers
    def defaultLocale
    def defaultHumour
    TwitterService twitterService
    MessageSource messageSource

    @Override
    void afterPropertiesSet() {
        conversationExpiracy = grailsApplication.config['conversationExpiracy']
        defaultLocale = new Locale(grailsApplication.config['jenna']['defaultLanguage'])
        defaultHumour = grailsApplication.config['jenna']['defaultHumour']
        handlers = [
                new CancelRequestHandler(),
                // new AskMeRequestHandler(),
                //new CreateProjectRequestHandler(),
                // new CreateAssignmentRequestHandler(),
                new EnterYesterdayHoursRequestHandler(),
                new EnterHoursRequestHandler(),
                new ActiveAssignmentsRequestHandler(),
                // new SalutationRequestHandler(),
                new HelpRequestHandler(),
                //  new HumourRequestHandler(availableHumours: ConfigurationHolder.config['jenna']['availableHumours']),
                new LanguageRequestHandler(availableLanguages: grailsApplication.config['jenna']['availableLanguages']),
                // new ReminderRequestHandler(),
                new SalutationStep2RequestHandler(),
                new SalutationStep3RequestHandler(),
                new SalutationStep4RequestHandler(),
                new SalutationRequestHandler(),
                new ProblemRequestHandler(),
                new ProblemStep2RequestHandler(),
                new ProblemStep3RequestHandler(),
                new ProblemStep4RequestHandler(),
                new KnowledgeStep1RequestHandler(),
                new KnowledgeStep2RequestHandler(),
                new KnowledgeStep3RequestHandler(),
                new TodayRequestHandler(),
                new YesterdayRequestHandler(),
                new InviteCoworkersStep1RequestHandler(),
                new InviteCoworkersStep2RequestHandler(),
                new InviteCoworkersStep3RequestHandler(),
                // new EffortRequestHandler(),
                // new TwitRequestHandler(),
                // new AcceptSuggestionRequestHandler(),
                // new RejectSuggestionRequestHandler()
        ]
    }

    def handleRequest(Request _request) {
        if (!_request.user) {
            return expressResponse(Response.build('unknownUser'))
        }
        Conversation conversation = getConversationForUser(_request.user)
        conversation.actualRequest = _request

        // Searchs within previous registered handlers until it finds the first that accept this conversation.
        // It will evaluate the accept method, expecting a boolean value.
        RequestHandler handler = handlers.find { RequestHandler handler ->
            handler.accepts(conversation)
        }
        if (handler) {
            handler.handle(conversation, this)
            conversation.actualRequest.user.save(flush: true) // flush: true --> Tells Hibernate to execute the query now!
            return expressResponse(conversation)
        } else {
            conversation.responses << Response.build('unknownCommand')
            //conversation.responses << new Response(text: conversation.eliza.processInput(_request.message))
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
        def projects = assignments*.project.unique()
        Date today = new Date()
        projects.each { Project project ->
            project.milestones.find { Milestone milestone ->
                if (milestone.dueDate.after(today) && milestone.dueDate - today < 7) {
                    conversation.responses << Response.build('reminderForMilestone', [milestone.name, milestone.project.name, milestone.dueDate])
                }
            }
        }



        if (assignments) {
            Queue queue = new LinkedList(assignments*.id)
            // Comentar para que pregunte a la vieja andanza.
            // conversation.context.assignments = queue

            // Descomentar para que pregunte copada
            // conversation.context.assignmentsAuxiliar = queue
            // Assignment assignment = Assignment.get(queue.peek())

            // Descomentar para que pregunte copada
            conversation.context.salutateStep1=true

            //FIXME: Can this redirect actual 'conversation' to my Request Handler
            return handleRequest(conversation.actualRequest);

            // conversation.responses << Response.build('requestTracking.assignment', [assignment.project.name, assignment.role.name])
        } else {
            // Aunque no tenga asignaciones, hacemos que pregunte Jenna.
            conversation.context.salutateStep1=true
            return handleRequest(conversation.actualRequest);

            // conversation.responses << Response.build('requestTracking')
        }
        return expressResponse(conversation)
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
        conversation.responses << Response.build('angry.remindEvent', [_event.name, _event.startDate])
        return expressResponse(conversation)
    }

    def getConversationForUser(User _user) {
        Conversation conversation = conversations[_user.account]
        if (!conversation) {
            conversation = new Conversation()
            conversation.humour = _user.humour ?: defaultHumour
            conversation.handlers = handlers
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
}
