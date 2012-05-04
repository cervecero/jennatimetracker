import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 8:25:36 PM
 */
class ReconnectJob {

    def group = 'jenna-jobs'
    def name = 'reconnect-job'

    def sessionRequired = false
    def concurrent = false

    static triggers = {
        cron startDelay: 10000, cronExpression: ConfigurationHolder.config['reconnect']['cronExpression']
    }

    JabberService jabberService

    def execute() {
        jabberService.checkAndReconnect()
    }
}