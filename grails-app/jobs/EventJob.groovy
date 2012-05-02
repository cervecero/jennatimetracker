import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 8:25:36 PM
 */
class EventJob {

    def group = 'jenna-jobs'
    def name = 'event-job'

    def sessionRequired = false
    def concurrent = false

    def cronExpression = ConfigurationHolder.config['event']['cronExpression']
/*
    static triggers = {
        cron startDelay: 10000, cronExpression: '0 0/15 0-23 ? * MON-FRI'
    }
*/

    JabberService jabberService

    def execute() {
        Date now = new Date()
        int year = now.year + 1900
        int month = now.month + 1
        int day = now.date
        int hours = now.hours
        int minutes = now.minutes
        String currentTimeExpression = "${hours < 10 ? '0' + hours : hours}:${minutes < 10 ? '0' + minutes : minutes}"
        String currentDateExpression = "$year-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}"
        User.withTransaction {
            def events = Event.findAllByLocalNotificationDateAndLocalNotificationTime(currentDateExpression, currentTimeExpression)
            if (events) {
                jabberService.remindEvents(events)
            }
        }
    }
}