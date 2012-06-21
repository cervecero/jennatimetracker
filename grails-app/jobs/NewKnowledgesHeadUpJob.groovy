
/**
 * @author Mariano Simone (mariano.simone@fdvsolutions.com)
 */
class NewKnowledgesHeadUpJob {

    def group = 'jenna-jobs'
    def name = 'new-knowledges-head-up-job'

    def sessionRequired = true
    def concurrent = false

    HeadsUpService headsUpService

    /*
    static triggers = {
    }
*/
    def execute() {
		Company.all.each { Company company ->
			headsUpService.sendNewKnowledgeReport(company)
		}
    }
}
