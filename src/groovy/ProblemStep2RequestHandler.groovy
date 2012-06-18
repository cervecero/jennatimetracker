
/**
 * @author Leandro Larroulet
 * Date: 12/01/11
 * Time: 16:47
 */
class ProblemStep2RequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.problemStep2
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.clear()

        def assignments = _conversation.actualRequest.user.listActiveAssignments()
        String actualMessage = _conversation.actualRequest.message

        int selection

        try {
          selection = Integer.parseInt(actualMessage)
        } catch (Exception e){
          selection = -1
        }

        if (selection < 0 || selection >= assignments.size()){
          _conversation.context.problemStep2=true

          _conversation.responses << Response.build('ProblemRequestHandler.project.selection.error.line1')
          _conversation.responses << Response.build('ProblemRequestHandler.project.selection.error.line2', [assignments.size()-1])

          int counter = 0
          assignments.each{ Assignment ass ->
            _conversation.responses << Response.build('ProblemRequestHandler.project', [counter, ass.project.name])
            counter++
          }
          return
        } else {
          _conversation.context.problemStep3=true
          _conversation.context.assignmentId = assignments.get(selection).id
          _conversation.responses << Response.build('ProblemRequestHandler.ask.description.hours')

        }
    }
}

