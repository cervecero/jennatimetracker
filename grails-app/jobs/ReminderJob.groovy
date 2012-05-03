import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 8:25:36 PM
 */
class ReminderJob {

    def group = 'jenna-jobs'
    def name = 'reminder-job'

    def sessionRequired = false
    def concurrent = false

    static triggers = {
        cron startDelay: 10000, cronExpression: ConfigurationHolder.config['chat']['cronExpression']
    }

    JabberService jabberService

    def execute() {
        Date now = new Date()
        int hours = now.hours
        int minutes = now.minutes
        String currentTimeExpression = "${hours < 10 ? '0' + hours : hours}:${minutes < 10 ? '0' + minutes : minutes}"
        Reminder.withTransaction {
            def reminders = Reminder.findAllByDoneAndLocalMoment(false, currentTimeExpression)
            jabberService.remind(reminders)
        }
    }
}
