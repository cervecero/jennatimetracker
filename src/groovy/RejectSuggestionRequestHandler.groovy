/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 12:44:40 AM
 */
class RejectSuggestionRequestHandler extends BaseEffortRequestHandler {


    def accepts(Conversation _conversation) {
        _conversation.context.effortMessage?.isProblematic() && _conversation.negative
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        saveEffortMessage(_conversation)
        _conversation.context.effortMessage = null
    }
}
