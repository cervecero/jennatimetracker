import groovy.text.SimpleTemplateEngine
import org.springframework.context.MessageSource

/**
 * This Service handles how emails are sent from Jenna to the users.
 * 
 * @author Mariano Simone (mariano.simone@fdvsolutions.com)
 *
 */
class EmailNotificationService {

    boolean transactional = false

    MessageSource messageSource
    EmailerService emailerService
    def grailsApplication
    
    def sendNotification(recipient, subject, templateName, model, attachments = []) {
        // FIXME: Current implementation can't use PageRenderer, as per http://jira.grails.org/browse/GRAILS-9106
        // Ideally, we could use it and leverage translations to <g:message /> instead of using different templates
        String templatePath = File.separator + "templates" + File.separator + templateName + "_" + recipient.locale +".html"
        File tplFile = grailsApplication.getMainContext().getResource(templatePath).getFile();
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(tplFile).make(model)
        def body = template.toString()

        def email = [
                to: [recipient.account],
                subject: subject,
                from: messageSource.getMessage('application.email', null, recipient.locale),
                text: body,
                attachments: attachments
        ]
        emailerService.sendEmails([email])
    }
}
