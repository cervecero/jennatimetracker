import java.text.ParseException

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 9, 2009
 * Time: 1:12:45 AM
 */
class AskMeRequestHandler extends BaseEffortRequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        User user = _conversation.actualRequest.user
        Date date
        def assignments
        if (_conversation.context.arguments) {
            try {
                date = _conversation.parseDate(_conversation.context.arguments)
                _conversation.context.date = date
                assignments = user.listAssignmentsByDate(date)
                _conversation.responses << Response.build('requestTracking.byDate', [date])
            } catch (ParseException ex) {
                date = new Date()
                assignments = user.listActiveAssignments()
            }
        } else {
            date = new Date()
            assignments = user.listActiveAssignments()
        }
        if (assignments) {
            Queue queue = new LinkedList(assignments*.id)
            _conversation.context.assignments = queue
            Assignment assignment = Assignment.get(queue.peek())
            _conversation.responses << Response.build('requestTracking.assignment', [assignment.project.name, assignment.role.name])
        } else {
            _conversation.responses << Response.build('requestTracking.noAssignment')
        }
    }
}
