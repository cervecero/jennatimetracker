/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 2, 2009
 * Time: 4:58:40 AM
 */
class CancelRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.clear()
        _conversation.responses.clear()
        _conversation.responses << Response.build('CancelRequestHandler.ok')
    }
}
