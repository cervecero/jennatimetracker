/**
 * @author Leandro Larroulet
 * Date: Jul 30, 2009
 * Time: 11:59:15 PM
 */
class SalutationStep3RequestHandler extends RequestHandler {

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.salutateStep3
      return accepted
    }

    private String answerEvaluation(String msg){

      String evaluation = Answer.UNKNOWN
      int value
      try {
        value = Integer.parseInt(msg)
      } catch (Exception e){
        // No valid integer number entered by user.
      }

      if (value >0 && value < 3){
        evaluation = Answer.NEGATIVE
      }
      if (value == 3){
        evaluation = Answer.NEUTRAL
      }
      if (value > 3 && value < 6){
        evaluation = Answer.POSITIVE
      }
      return evaluation
    }

    private List<Response> getAnswerFor(String answer){
      List<Response> responses = []

      if (answer.equals(Answer.POSITIVE)){
        responses << Response.build('SalutationRequestHandlerStep3IsFine')
        responses << Response.build('SalutationRequestHandlerStep3FineAskHours')

      } else if (answer.equals(Answer.NEGATIVE)){
        responses << Response.build('SalutationRequestHandlerStep3IsNotFine')
        responses << Response.build('SalutationRequestHandlerStep3BadAskHours')

      } else {
        responses << Response.build('SalutationRequestHandlerStep3IsNeutral')
        responses << Response.build('SalutationRequestHandlerStep3FinalAskHours')
      }
      return responses
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()

      User user =_conversation.actualRequest.user

      String actualMessage = _conversation.actualRequest.message
      String answer = answerEvaluation(actualMessage);

      if (answer.equals(Answer.UNKNOWN)){
        _conversation.responses << Response.build('SalutationRequestHandlerStep2')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options1')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options2')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options3')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options4')
        _conversation.responses << Response.build('SalutationRequestHandlerStep2Options5')
        _conversation.context.salutateStep3=true
        return
      }

      GregorianCalendar calendar = GregorianCalendar.getInstance();
      java.sql.Date date = new java.sql.Date(calendar.getTimeInMillis());

      // Si estamos acá, es porque la respuesta calificó en alguna de las opciones válidas.
      UserMood uMood = new UserMood()
      uMood.user = user
      uMood.date = date
      uMood.value = Integer.parseInt(actualMessage)
      uMood.status = ""
      uMood.company = user.company
      uMood.save(flush:true)

      // List user assignments so that workflow will change in case user doesn't have any.
      def assignments = user.listActiveAssignments()

      // Sea como sea que esté, le voy a preguntar por sus asignaciones, si tiene.
      if (assignments){
        List<Response> responses = getAnswerFor(answer)
        _conversation.responses = responses
        _conversation.context.clear()
        _conversation.context.salutateStep4=true
      } else {
        _conversation.responses << Response.build('SalutationRequestHandlerStep3NoAssignments')
        _conversation.context.clear()
        _conversation.context.askForKnowledge=true
        
        KnowledgeStep1RequestHandler handler = new KnowledgeStep1RequestHandler()
        handler.handle(_conversation, _chatService)


      }

    }

    /*
    def doHandleOld(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()
      _conversation.context.salutateStep4=true
      User user =_conversation.actualRequest.user 

      String actualMessage = _conversation.actualRequest.message
      String answer = userMood(user, actualMessage);

      Status status = new Status()
      status.user = user
      status.date = new Date()
      status.status = actualMessage
      status.category = answer
      status.save(flush:true)
      
      _conversation.context.status = status

      // List user assignments so that workflow will change in case user doesn't have any.
      def assignments = user.listActiveAssignments()

      if (answer.equals(Answer.POSITIVE)){
        // Está bien.
        if (assignments){
          _conversation.responses << Response.build('SalutationRequestHandlerStep3Fine')
          _conversation.responses << Response.build('SalutationRequestHandlerStep3FineAskHours')
        } else {
          _conversation.responses << Response.build('SalutationRequestHandlerStep3FineDontAskHours')
          _conversation.context.clear()
        }

      } else if (answer.equals(Answer.NEGATIVE)){
        // Está mal.
        if (assignments){
          _conversation.responses << Response.build('SalutationRequestHandlerStep3Bad')
          _conversation.responses << Response.build('SalutationRequestHandlerStep3BadAskHours')
        } else {
          _conversation.responses << Response.build('SalutationRequestHandlerStep3BadDontAskHours')
          _conversation.context.clear()
        }
      } else {
        // No sabemos que le pasa, como si fuera una mujer.

        if (assignments){
          _conversation.responses << Response.build('SalutationRequestHandlerStep3Final')
          _conversation.responses << Response.build('SalutationRequestHandlerStep3FinalAskHours')
        } else {
          _conversation.responses << Response.build('SalutationRequestHandlerStep3FinalDontAskHours')
          _conversation.context.clear()
        }
      }
    }

    private String userMood(User user, String message){
      String answer = Answer.UNKNOWN

      message.split().each{ String actualWord ->
        List word = Dictionary.withCriteria {
          eq("type", "adjective")
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
    */
}
