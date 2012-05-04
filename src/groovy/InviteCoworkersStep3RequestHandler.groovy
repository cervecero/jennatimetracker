import groovy.text.SimpleTemplateEngine

import org.springframework.mail.MailSender
import javax.mail.internet.MimeMessage
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.MailException
import javax.mail.MessagingException
import org.springframework.context.MessageSource

/**
 * @author Leandro Larroulet
 * Date: Sep 13, 2010
 * Time: 13:05
 */

class InviteCoworkersStep3RequestHandler extends RequestHandler {

	def grailsApplication
	
    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.inviteCoworkers3
      return accepted
    }

	def isValidEmail(email) {
		//FIXME: Implement
		return true;
	}

    def doHandle(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()

      User user =_conversation.actualRequest.user

      // We expect a list of emails from the user
      String userMessage = _conversation.actualRequest.message
      def emails = userMessage.split('[,]')

      def errors = false

      emails.each {String email ->
        String emailAccount = email.trim()
        if (isValidEmail(emailAccount)){
          inviteUser(user, emailAccount, _conversation)
        } else {
          errors = true
        }
      }

      if (!errors){
        _conversation.responses << Response.build('sweet.InviteCoworkersStep3RequestHandlerOk') //FIXME: Hardcoded humour!
        _conversation.context.clear()
      } else {
        _conversation.responses << Response.build('sweet.InviteCoworkersStep3RequestHandlerError') //FIXME: Hardcoded humour!
        _conversation.context.clear()
        _conversation.context.inviteCoworkers3=true
      }
      return
    }

    private inviteUser(User user, String invitee, Conversation _conversation){
      if (!User.findByAccount(invitee)) {

        Invitation invitation = new Invitation()
        invitation.inviter = user
        invitation.invitee = invitee
        invitation.invited = new Date()
        invitation.code = String.valueOf(System.currentTimeMillis())+String.valueOf(System.currentTimeMillis()-1900)
        invitation.validate()

        if (!invitation.hasErrors()) {
          invitation.save()
		  MessageSource messageSource = grailsApplication.mainContext.getBean('messageSource')
          def email = [
                  to: [invitation.invitee],
                  subject: messageSource.getMessage('invitation.mail.subject', null, user.locale),
                  from: messageSource.getMessage('application.email', null, user.locale),
                  text: messageSource.getMessage('invitation.mail.body', ["FIXME invitation link"] as Object[], user.locale), // FIXME: Create invitation link
          ]
          sendEmail([email])
        } 
      }
    }


    /**
     * Send a list of emails
     *
     * @param mails a list of maps
     */
	// FIXME: Try to inject EmailerService or InvitationService
    def sendEmail(mails) {
		MailSender mailSender = grailsApplication.mainContext.getBean('mailSender')
        def messages = mails.collect { mail ->
            MimeMessage mimeMessage = mailSender.createMimeMessage()
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1")
            helper.to = mail.to
            helper.subject = mail.subject
            helper.setText(mail.text, true)
            helper.from = mail.from
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
