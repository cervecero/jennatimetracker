import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Mar 2, 2012
 * Time: 10:52:36 PM
 */
class ProjectFollowUpJob {

    def group = 'jenna-jobs'
    def name = 'project-follow-up-job'

    def sessionRequired = false
    def concurrent = false

    def projectFollowUpService

    //def cronExpression = ConfigurationHolder.config['projectFollowUp']['cronExpression']

    static triggers = {
        cron startDelay: 10000, cronExpression: ConfigurationHolder.config['projectFollowUp']['cronExpression']
    }

    def execute() {
        projectFollowUpService.sendEmailsToTeamLeaders()
    }
}
