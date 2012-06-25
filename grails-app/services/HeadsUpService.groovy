import grails.gsp.PageRenderer
import org.springframework.context.MessageSource

class HeadsUpService {

    boolean transactional = false

    MessageSource messageSource
    CompanyService companyService
    EmailNotificationService emailNotificationService
    
    def sendNewKnowledgeReport(company) {
        def today = new Date()
        def from = (today-7).onlyDate
        def to = today.onlyDate
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
}
