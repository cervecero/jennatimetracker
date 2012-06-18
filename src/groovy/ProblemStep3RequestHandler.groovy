
/**
 * @author Leandro Larroulet
 * Date: 12/01/11
 * Time: 16:48
 */
class ProblemStep3RequestHandler extends RequestHandler {

    DatabaseService dbService

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.problemStep3
      return accepted
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
      User user =_conversation.actualRequest.user

      Integer assignmentId = _conversation.context.assignmentId

      _conversation.context.clear()

      String actualMessage = _conversation.actualRequest.message

      // Ac� validamos que la cantidad de horas indicadas est�n bien.
      int hours

      try {
        hours = Integer.parseInt(actualMessage)
      } catch (Exception e){
        hours = -1
      }
      _conversation.context.assignmentId = assignmentId

      if (hours > 0 && hours < 16){
        // Si la cantidad de horas est� OK, pedimos una descripci�n del problema.
        _conversation.context.problemStep4=true
        _conversation.context.hours = hours
        _conversation.responses << Response.build('ProblemRequestHandler.ask.description')
        return
      }
      else {
        // Si la cantidad de horas est� MAL, pedimos que reingrese la cantidad de horas.
        _conversation.context.problemStep3=true
        _conversation.context.assignmentId = assignmentId
        _conversation.responses << Response.build('ProblemRequestHandler.ask.description.hours.error')
        _conversation.responses << Response.build('ProblemRequestHandler.ask.description.hours')

      }
    }
}
