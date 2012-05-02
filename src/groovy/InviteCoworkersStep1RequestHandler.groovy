/**
 * @author Leandro Larroulet
 * Date: Sep 13, 2010
 * Time: 13:05
 */

class InviteCoworkersStep1RequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.inviteCoworkers1
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.clear()
        _conversation.context.inviteCoworkers2=true

        // Sos el �nico usuario en tu compa��a, te gustar�a invitar compa�eros de trabajo?
        // Si
        // En otro momento
        // No, y no me vuelvas a preguntar!
        _conversation.responses << Response.build('sweet.InviteCoworkersStep1RequestHandlerAsk1')
        _conversation.responses << Response.build('sweet.InviteCoworkersStep1RequestHandlerAsk2')
        _conversation.responses << Response.build('sweet.InviteCoworkersStep1RequestHandlerYes')
        _conversation.responses << Response.build('sweet.InviteCoworkersStep1RequestHandlerMaybe')
        _conversation.responses << Response.build('sweet.InviteCoworkersStep1RequestHandlerNo')

    }
}