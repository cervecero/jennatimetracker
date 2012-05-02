/**
 * @author Leandro Larroulet (leandro.larroulet@fdvsolutions.com)
 * Date: Jun 11, 2010
 * Time: 15:00:00 PM
 */

class YesterdayRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        Date yesterday = new Date() -1
        yesterday.setHours(0)
        yesterday.setMinutes(0)
        yesterday.setSeconds(0)

        def today = new Date()
        today.setHours(0)
        today.setMinutes(0)
        today.setSeconds(0)

        def efforts = Effort.withCriteria(){
          eq('user', _conversation.actualRequest.user)
          gt('date', yesterday)
          lt('date', today)
          gt('timeSpent', 0D)
          ne('deleted', true)
        }
        _conversation.context.clear()
        if (efforts) {
            efforts.each { Effort effort ->
                _conversation.responses << Response.build('YesterdayRequestHandler.effort', [effort.timeSpent, effort.tags.join(', '), effort.comment ?: ''])
            }
        } else {
            _conversation.responses << Response.build('YesterdayRequestHandler.nothing')
        }
    }
}