import grails.gsp.PageRenderer
import org.springframework.context.MessageSource

class HeadsUpService {

    boolean transactional = false

    MessageSource messageSource
    CompanyService companyService
    EmailNotificationService emailNotificationService
    def grailsApplication

    def sendNewKnowledgeReport(company) {
        def today = new Date().clearTime()
        def from = today-7
        def to = today
        def newKnowledge = companyService.listNewLearnings(company, from, to)
        company.employees.each { employee ->
            log.info("Preparing report for ${employee} (locale: ${employee.locale})")
            def model = [
                    recipient: employee, company: company, newKnowledge: newKnowledge,
                    from: messageSource.getMessage("default.date.formatted.short", [from] as Object[], employee.locale),
                    to: messageSource.getMessage("default.date.formatted.short", [to] as Object[], employee.locale)
                ]
            emailNotificationService.sendNotification(employee, messageSource.getMessage('knowledge.heads.up.subject', null, employee.locale), 'knowledgeHeadsUp', model)
            log.info("Report for ${employee} sent")
        }
    }

    def sendMoodWarningReport(company) {
        def today = new Date().clearTime()
        def from = today-7
        def to = today
        def projects = Project.createCriteria().list {
            eq('company', company)
            eq('active', true)
            eq('deleted', false)
            gt('endDate', from)
            lt('startDate', to)
        }
        projects.each { project ->
            def moods = UserMood.executeQuery(
                '''select um
from UserMood um join um.user u join u.assignments a
where a.project = :project and a.deleted = false and a.startDate < :to and a.endDate > :from 
order by um.date desc''',
                [project: project, from: from, to: to])
            def moodPerUser = new HashMap().withDefault { [] }
            moods.each { m ->
                moodPerUser[m.user].add(m.value)
            }
            def moodWarnings = moodPerUser.findAll { it.value.size() >= 2 && (it.value[-1] <= 2 || it.value[-1] < it.value[-2]-2) }
            def model = [
                    recipient: project.teamLeader, moodWarnings: moodWarnings,
                    from: messageSource.getMessage("default.date.formatted.short", [from] as Object[], project.teamLeader.locale),
                    to: messageSource.getMessage("default.date.formatted.short", [to] as Object[], project.teamLeader.locale)
                ]
            emailNotificationService.sendNotification(project.teamLeader, messageSource.getMessage('mood.heads.up.subject', [project] as Object[], project.teamLeader.locale), 'moodHeadsUp', model)
            log.info("Report for ${project.teamLeader} sent")
        }
    }
}
