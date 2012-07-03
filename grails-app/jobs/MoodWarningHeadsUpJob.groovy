
/**
 * @author Mariano Simone (mariano.simone@fdvsolutions.com)
 */
class MoodWarningHeadsUpJob {

    def group = 'jenna-jobs'
    def name = 'humor-warning-head-up-job'

    def sessionRequired = true
    def concurrent = false

    HeadsUpService headsUpService

    static triggers = { }

    def execute() {
		Company.all.each { Company company ->
            log.info("Sending mood report to team leaders of ${company} through ${headsUpService}")
			headsUpService.sendMoodWarningReport(company)
		}
    }
}
