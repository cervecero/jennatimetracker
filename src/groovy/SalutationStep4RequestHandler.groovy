/**
 * @author Leandro Larroulet
 * Date: Jul 30, 2009
 * Time: 11:59:15 PM
 */
class SalutationStep4RequestHandler extends RequestHandler {

    DatabaseService dbService

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.salutateStep4
      return accepted
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
      User user =_conversation.actualRequest.user 

      _conversation.context.clear()

      String actualMessage = _conversation.actualRequest.message
      String answer = userAnswer(user, actualMessage);

      if (answer.equals(Answer.POSITIVE)){
        _conversation.context.assignments = _conversation.actualRequest.user.listActiveAssignments()
        EnterHoursRequestHandler handler = new EnterHoursRequestHandler()
        handler.handle(_conversation, _chatService)

      } else if (answer.equals(Answer.NEGATIVE)){
        _conversation.responses << Response.build('SalutationRequestHandlerStep4Bad')

      } else {
        _conversation.responses << Response.build('SalutationRequestHandlerStep4Unknown')
        EnterHoursRequestHandler handler = new EnterHoursRequestHandler()
        handler.handle(_conversation, _chatService)

      }
    }

    private String userAnswer(User user, String message){
      String answer = Answer.UNKNOWN

      message.split().each{ String actualWord ->
        List word = Dictionary.withCriteria {
          eq("type", "answer")
          eq("locale", user.locale)
          eq("word", actualWord.toLowerCase())
        }
        word.each{ Dictionary dw ->
          if (dw.category.equals(Answer.POSITIVE))
            answer = Answer.POSITIVE;
          if (dw.category.equals(Answer.NEGATIVE))
            answer = Answer.NEGATIVE;
        }
      }
      return answer;
    }
}