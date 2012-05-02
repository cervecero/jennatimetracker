/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 30, 2009
 * Time: 11:59:15 PM
 */
class HumourRequestHandler extends RequestHandler {

    def availableHumours

    def getDescriptionParams() {
        return [availableHumours.join(', ')]
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        if (_conversation.context.arguments) {
            def requestedHumour = _conversation.context.arguments?.trim().toLowerCase(_conversation.locale)
            def validHumour = availableHumours.contains(requestedHumour)
            _conversation.context.clear()
            if (validHumour) {
                _conversation.humour = requestedHumour
                _conversation.actualRequest.user.humour = requestedHumour
                _conversation.responses << Response.build('HumourRequestHandler.ok', [_conversation.humour])
            } else {
                _conversation.responses << Response.build('HumourRequestHandler.unknown', [requestedHumour])
            }
        } else {
            _conversation.context.clear()
            _conversation.responses << Response.build('HumourRequestHandler.show', [_conversation.humour])
        }
    }
}
