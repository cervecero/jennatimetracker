/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 5:26:53 PM
 */
class LanguageRequestHandler extends RequestHandler {

    def availableLanguages

    def getDescriptionParams() {
        return [availableLanguages.join(', ')]
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        if (_conversation.context.arguments) {
            def requestedLanguage = _conversation.context.arguments?.trim().toLowerCase(_conversation.locale)
            def validLanguage = availableLanguages.contains(requestedLanguage)
            _conversation.context.clear()
            if (validLanguage) {
                _conversation.actualRequest.user.locale = new Locale(requestedLanguage)
                _conversation.responses << Response.build('LanguageRequestHandler.ok')
            } else {
                _conversation.responses << Response.build('LanguageRequestHandler.unknown', [requestedLanguage])
            }
        } else {
            _conversation.context.clear()
            _conversation.responses << Response.build('LanguageRequestHandler.show', [_conversation.locale])
        }
    }
}
