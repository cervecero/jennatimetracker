
/**
 * @author Mariano Simone (mariano.simone@fdvsolutions.com)
 */
class NewKnowledgesHeadUpJob {

    def group = 'jenna-jobs'
    def name = 'new-knowledges-head-up-job'

    def sessionRequired = true
    def concurrent = false

    HeadsUpService headsUpService

    static triggers = { }

    def execute() {
		Company.all.each { Company company ->
            log.info("Sending knowledge report to members of ${company} through ${headsUpService}")
			headsUpService.sendNewKnowledgeReport(company)
		}
    }
}
