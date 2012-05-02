/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 12:44:08 AM
 */
class AcceptSuggestionRequestHandler extends BaseEffortRequestHandler {

    def accepts(Conversation _conversation) {
        _conversation.context.effortMessage?.isProblematic() && _conversation.affirmative
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.effortMessage.acceptSuggestion()
        saveEffortMessage(_conversation)
        _conversation.context.effortMessage = null
    }
}
