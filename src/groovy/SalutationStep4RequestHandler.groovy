/**
 * @author Leandro Larroulet
 * Date: Jul 30, 2009
 * Time: 11:59:15 PM
 */
class SalutationStep4RequestHandler extends RequestHandler {

    DatabaseService dbService // FIXME: Not used?

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.salutateStep4
      return accepted
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()

      if (_conversation.isAffirmative()){
        _conversation.context.assignments = _conversation.actualRequest.user.listActiveAssignments()
        EnterHoursRequestHandler handler = new EnterHoursRequestHandler()
        handler.handle(_conversation, _chatService)

      } else if (_conversation.isNegative()){
        _conversation.responses << Response.build('SalutationRequestHandlerStep4Bad')

      } else {
        _conversation.responses << Response.build('SalutationRequestHandlerStep4Unknown')
        EnterHoursRequestHandler handler = new EnterHoursRequestHandler()
        handler.handle(_conversation, _chatService)

      }
    }
}