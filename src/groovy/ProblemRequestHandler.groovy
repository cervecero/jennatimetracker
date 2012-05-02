
/**
 * Created by IntelliJ IDEA.
 * User: Lea
 * Date: 12/01/11
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
class ProblemRequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.problemStep1 || super.accepts(_conversation)
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.clear()

        def assignments = _conversation.actualRequest.user.listActiveAssignments()

        _conversation.context.problemStep2=true

        _conversation.responses << Response.build('ProblemRequestHandler.line1')

        int counter = 0
        assignments.each{ Assignment ass ->
          _conversation.responses << Response.build('ProblemRequestHandler.project', [counter, ass.project.name])
          counter++
        }
    }
}