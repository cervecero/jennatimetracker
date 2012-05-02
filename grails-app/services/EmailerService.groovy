import javax.mail.MessagingException
import javax.mail.internet.MimeMessage
import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.MimeMessageHelper

class EmailerService {

    boolean transactional = false

    MailSender mailSender

    /**
     * Send a list of emails
     *
     * @param mails a list of maps
     */
    def sendEmails(mails) {
        def messages = mails.collect { mail ->
            MimeMessage mimeMessage = mailSender.createMimeMessage()
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1")
            helper.to = mail.to
            helper.subject = mail.subject
            helper.setText(mail.text, true)
            helper.from = mail.from
            if (mail.attachments) {
                mail.attachments.each { attach ->
                    helper.addAttachment(attach.name, attach)
                }
            }
            mimeMessage
        }
        try {
            mailSender.send messages as MimeMessage[]
        } catch (MailException ex) {
            log.error('Failed to send emails', ex)
        } catch (MessagingException ex) {
            log.error('Failed to send emails', ex)
        }
    }
}
