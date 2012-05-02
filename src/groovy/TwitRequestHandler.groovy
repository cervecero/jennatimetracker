/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 2, 2009
 * Time: 4:58:40 AM
 */
class TwitRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        _chatService.twit(_conversation.context.arguments)
        _conversation.responses << Response.build('TwitRequestHandler.ok')
    }
}
