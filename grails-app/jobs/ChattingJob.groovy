/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 8:25:36 PM
 */
class ChattingJob {

    def group = 'jenna-jobs'
    def name = 'chatting-job'

    def sessionRequired = false
    def concurrent = false
    JabberService jabberService

    def execute() {
        Date now = new Date()
        int hours = now.hours
        int minutes = now.minutes
        String currentTimeExpression = "${hours < 10 ? '0' + hours : hours}:${minutes < 10 ? '0' + minutes : minutes}"
        User.withTransaction {
            User.withHibernateFilters {
                def users = User.findAllByLocalChatTime(currentTimeExpression)
                List usersToQuery = new ArrayList()
    
                users.each{ User us ->
                        if (!userAlreadyEnteredHoursToday(us))
                          usersToQuery.add(us)
                }
                if (usersToQuery) {
                  jabberService.queryUsers(usersToQuery)
                }
            }
        }
    }

    boolean userAlreadyEnteredHoursToday(User user){

      Date beginToday = new Date()
      Date endToday = new Date()

      beginToday.setHours(0)
      beginToday.setMinutes(0)
      beginToday.setSeconds(0)
      endToday.setHours(23)
      endToday.setMinutes(59)
      endToday.setSeconds(59)

      def efforts = Effort.withCriteria(){
          eq('user', user)
          gt('date', beginToday)
          lt('date', endToday)
      }

      if (efforts)
        return true

      return false
    }
}