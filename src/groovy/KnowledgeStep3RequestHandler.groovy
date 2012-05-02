/**
 * @author Leandro Larroulet
 * Date: Sep 13, 2010
 * Time: 13:05
 */

class KnowledgeStep3RequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.askForKnowledge3
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()

      // No further conversation after user answers new learning.
      // _conversation.context.askForKnowledge3=true
      User user =_conversation.actualRequest.user

      String actualMessage = _conversation.actualRequest.message

      Learning newLearning

      GregorianCalendar calendar = GregorianCalendar.getInstance();
      Date date = new Date(calendar.getTimeInMillis());

      ScoreManager.addPoints(user, "KNOWLEDGE","LEARNING")

        newLearning = new Learning()
        newLearning.date = date
        newLearning.user = user
        newLearning.company = user.company
        newLearning.points = 0
        newLearning.description = actualMessage
        newLearning.save(flush: true)

      _conversation.responses << Response.build('KnowledgeStep3RequestHandlerFinal')

    }

}