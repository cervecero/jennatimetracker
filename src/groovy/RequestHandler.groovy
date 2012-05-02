import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.context.NoSuchMessageException

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 12:01:23 AM
 */
abstract class RequestHandler {

    def getName(Conversation _conversation) {
        try {
            return _conversation.getMessage(this.class.name + '.name')
        } catch (NoSuchMessageException ex) {
            //do nothing
            return null
        }
    }

    def getDescription(Conversation _conversation) {
        return _conversation.getMessage(this.class.name + '.description', descriptionParams)
    }

    def getDescriptionParams() {
        return null
    }

    def isAuthorized(User _user) {
        def authorizations = ConfigurationHolder.config['jenna']['authorizations'][(this.class.name)]
        return !authorizations || _user.permissions.any { Permission permission ->
            authorizations.contains(permission.name)
        }
    }

    def needsEnabledAccount() {
        return true
    }

    def accepts(Conversation _conversation) {
        return _conversation.matches(this.class.name)
    }

    def handle(Conversation _conversation, ChatService _chatService) {
        _conversation.lastMessageTime = System.currentTimeMillis()
        if (!isAuthorized(_conversation.actualRequest.user)) {
            _conversation.responses << Response.build('unauthorized')
        } else if (needsEnabledAccount() && !_conversation.actualRequest.user.enabled) {
            _conversation.responses << Response.build('inactiveUser')
        } else if (_conversation.isList() && _conversation.context.wizard?.acceptsList()) {
            _conversation.context.wizard.list(_conversation)
        } else {
            doHandle(_conversation, _chatService)
        }
    }

    def abstract doHandle(Conversation _conversation, ChatService _chatService)
}
