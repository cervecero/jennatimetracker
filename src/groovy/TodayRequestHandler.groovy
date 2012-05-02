/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 2:14:48 AM
 */
class TodayRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {

        Date startDate = new Date()
        Date endDate = new Date()
        startDate.setHours(0)
        startDate.setMinutes(0)
        startDate.setSeconds(0)
        endDate.setHours(23)
        endDate.setMinutes(59)
        endDate.setSeconds(59)

        def efforts = Effort.withCriteria(){
          eq('user', _conversation.actualRequest.user)
          gt('date', startDate)
          lt('date', endDate)
          gt('timeSpent', 0D)
          ne('deleted', true)
        }
        _conversation.context.clear()
        if (efforts) {
            efforts.each { Effort effort ->
                _conversation.responses << Response.build('TodayRequestHandler.effort', [effort.timeSpent, effort.tags.join(', '), effort.comment ?: ''])
            }
        } else {
            _conversation.responses << Response.build('TodayRequestHandler.nothing')
        }
    }
}
