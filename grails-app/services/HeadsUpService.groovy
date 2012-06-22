import grails.gsp.PageRenderer
import org.springframework.context.MessageSource

class HeadsUpService {

    boolean transactional = false

    MessageSource messageSource
    EmailerService emailerService
    CompanyService companyService
    PageRenderer groovyPageRenderer
    
    def sendNewKnowledgeReport(company) {
        def today = new Date()
        def from = (today-7).onlyDate
        def to = today.onlyDate
        def newKnowledge = companyService.listNewLearnings(company, from, to)
        company.employees.each { employee ->
            def email = [
                    to: [employee.account],
                    subject: messageSource.getMessage('knowledge.heads.up.subject', null, employee.locale),
                    from: messageSource.getMessage('application.email', null, employee.locale),
                    text: groovyPageRenderer.render(view: newKnowledge ? '/email/knowledgeHeadsUp' : '/email/knowledgeHeadsUpNoNew', model: [recipient: employee, company: company, newKnowledge: newKnowledge, from: from, to: to])
            ]
            emailerService.sendEmails([email])
        }
    }
}
