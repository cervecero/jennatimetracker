/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 2:14:48 AM
 */
class HelpRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.responses << Response.build('HelpRequestHandler.begin')
        _conversation.handlers.each { RequestHandler handler ->
            if (handler.isAuthorized(_conversation.actualRequest.user)) {
                String name = handler.getName(_conversation)
                if (name) {
                    _conversation.responses << Response.build('HelpRequestHandler.line', [name, handler.getDescription(_conversation)])
                }
            }
        }
        _conversation.responses << Response.build('HelpRequestHandler.end')
        _conversation.context.clear()
    }
}
