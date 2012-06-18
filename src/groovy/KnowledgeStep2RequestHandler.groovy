/**
 * @author Leandro Larroulet
 * Date: Sep 13, 2010
 * Time: 13:05
 */

class KnowledgeStep2RequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.askForKnowledge2
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()

      User user =_conversation.actualRequest.user

      String actualMessage = _conversation.actualRequest.message
      String answer = userAnswer(user, actualMessage);

      if (answer.equals(Answer.POSITIVE)){
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerDidLearnSomething')
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerAskKnowledge')
        _conversation.context.askForKnowledge3=true
      } else if (answer.equals(Answer.NEGATIVE)){
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerDidNotLearnAnything')
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerDidNotLearnAnythingPunish')
      } else {
        _conversation.context.askForKnowledge2=true
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerAskKnowledgeAgain')
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