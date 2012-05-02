/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 12:40:52 AM
 */
abstract class BaseEffortRequestHandler extends RequestHandler {

    def saveEffortMessage(Conversation _conversation) {
        EffortMessage effortMessage = _conversation.context.effortMessage
        if (effortMessage.timeSpent > 0) {
            def tags = new HashSet()
            if (_conversation.context.assignments) {
                Assignment assignment = Assignment.get(_conversation.context.assignments.poll())
                tags.addAll(assignment.project.tags)
                if (!_conversation.context.assignments) {
                    _conversation.context.assignments = null
                }
            }
            Effort effort = new Effort()
            effort.timeSpent = effortMessage.timeSpent
            effort.comment = effortMessage.comment
            effort.date = _conversation.context.date ?: new Date()
            effortMessage.tags.each { tagName ->
                def tag = Tag.findByName(tagName)
                if (!tag) {
                    tag = new Tag(name: tagName, category: TagCategory.findByName('task'), company: _conversation.actualRequest.user.company)
                    tag.save()
                }
                tags << tag
            }
            tags.each { tag ->
                effort.addToTags(tag)
            }
            _conversation.actualRequest.user.addToEfforts(effort)
        } else if (_conversation.context.assignments) {
            _conversation.context.assignments.poll()
        }
        if (_conversation.context.assignments) {
            Assignment assignment = Assignment.get(_conversation.context.assignments.peek())
            _conversation.responses << Response.build('requestTracking.assignment', [assignment.project.name, assignment.role.name])
        } else {
            _conversation.responses << Response.build('EffortRequestHandler.thanks', [_conversation.actualRequest.user.name])
        }
    }
}
