import org.springframework.context.MessageSource

class HeadsUpService {

    boolean transactional = false

    MessageSource messageSource
    EmailerService emailerService
    CompanyService companyService

    def sendNewKnowledgeReport(company) {
        def today = new Date()
        def newKnowledge = companyService.listNewLearnings(company, (today-7).onlyDate, today.onlyDate)
        company.employees.each { employee ->
            def email = [
                    to: [employee.account],
                    subject: messageSource.getMessage('welcome', null, employee.locale),
                    from: messageSource.getMessage('application.email', null, employee.locale),
                    text: messageSource.getMessage('email.projectFollowUp.body', ["hola", new Date(), new Date()] as Object[], employee.locale),
            ]
            emailerService.sendEmails([email])
        }
    }
}
