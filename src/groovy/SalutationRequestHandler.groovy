/**
 * @author Leandro Larroulet
 * Date: Jul 30, 2009
 * Time: 11:59:15 PM
 */
class SalutationRequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.salutateStep1 || super.accepts(_conversation)
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.clear()

        _conversation.context.salutateStep3=true

        _conversation.responses << Response.build('SalutationRequestHandler', [_conversation.actualRequest.user.name])
        _conversation.responses << Response.build('SalutationRequestHandlerStep2')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options1')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options2')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options3')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options4')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options5')
    }
}
