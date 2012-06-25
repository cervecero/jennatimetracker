
/**
 * As some countries use Daylight Saving Time, this Job moves:
 * - User#localChatTime
 * - Reminder#localMoment
 * 
 * In order for the server to respect DST if the User's Timezone uses it
 * 
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 8:21:15 PM
 */
class TimeConverterJob {

    def group = 'jenna-jobs'
    def name = 'time-converter-job'

    def sessionRequired = true
    def concurrent = false

    static triggers = {
        cron name: 'beforeEachHour', cronExpression: "0 55 * ? * *"
    }

    def execute() {
        User.list().each { User user ->
            user.localChatTime = TimeZoneUtil.toSystemTime(user.chatTime, user.timeZone)
            user.save()
        }
        Reminder.findAllByDone(false).each { Reminder reminder ->
            reminder.localMoment = TimeZoneUtil.toSystemTime(reminder.moment, reminder.user.timeZone)
            reminder.save()
        }
    }
}
