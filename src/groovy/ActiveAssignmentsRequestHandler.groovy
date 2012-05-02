/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 1, 2009
 * Time: 11:57:16 PM
 */
class ActiveAssignmentsRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        def assignments = _conversation.actualRequest.user.listActiveAssignments()
        _conversation.context.clear()
        if (assignments) {
            def lines = []
            assignments.each { assignment ->
                lines << "$assignment.project.name - $assignment.role.name"
            }
            _conversation.responses << new Response(text: lines.join('\n'))
        } else {
            _conversation.responses << Response.build('ActiveAssignmentsRequestHandler.empty')
        }
    }
}
