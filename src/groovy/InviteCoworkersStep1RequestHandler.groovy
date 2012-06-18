/**
 * Tells a user that, as the only member of its organization, it can invite more people to join,
 * and waits for a response like ok, ask me tomorrow or don't bother me again about it
 * 
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

        _conversation.responses << Response.build('InviteCoworkersStep1RequestHandlerAsk1')
        _conversation.responses << Response.build('InviteCoworkersStep1RequestHandlerAsk2')
        _conversation.responses << Response.build('InviteCoworkersStep1RequestHandlerYes')
        _conversation.responses << Response.build('InviteCoworkersStep1RequestHandlerMaybe')
        _conversation.responses << Response.build('InviteCoworkersStep1RequestHandlerNo')
    }
}