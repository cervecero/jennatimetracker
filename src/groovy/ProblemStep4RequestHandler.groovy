
/**
 * Created by IntelliJ IDEA.
 * User: Lea
 * Date: 12/01/11
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
class ProblemStep4RequestHandler extends RequestHandler {

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.problemStep4
      return accepted
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
      User user =_conversation.actualRequest.user

      Integer assignmentId = _conversation.context.assignmentId
      Integer hours = _conversation.context.hours
      String actualMessage = _conversation.actualRequest.message

      _conversation.context.clear()

      // Si la cantidad de horas está OK, pedimos una descripción del problema.

      Boolean partsToProcess = Boolean.TRUE
      int sequence = 0
      int actualMessageLength = actualMessage.length()

      Problem problem
      Date date = new Date()

      // While there are parts to process...
      while (partsToProcess){
        problem = new Problem()
        problem.user = user
        problem.company = user.company
        problem.assignment = Assignment.get(assignmentId)
        problem.date = date
        problem.timeSpent = hours
        problem.sequence = sequence

        int start = sequence*512
        int end   = (sequence+1)*512

        if (end >= actualMessage.length()){
          end = actualMessage.length()
          partsToProcess = Boolean.FALSE
        }

        problem.description = actualMessage.substring(start,end)

        problem.save(flush:true)
        sequence++
      }

      _conversation.responses << Response.build('ProblemRequestHandler.end')

    }

}
