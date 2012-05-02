class PendingUsersController  extends BaseController {

    def index = { redirect(action: "list", params: params) }
    def emailerService
    def authenticateService

    static Map allowedMethods = [invite: 'POST', dismiss: 'POST']  

    def beforeInterceptor = [action:this.&auth]

    def auth() {
         try {
          findLoggedUser()
          return true
         } catch (Exception e){
          redirect(controller:'login', action:'auth')
          return false
         }
    }

    def list = {

      User currentUser = findLoggedUser()

      def requestedInvitationsList = InviteMe.findAllByCompany(currentUser.company)

      return [requestedInvitations: requestedInvitationsList]
    }

    def invite = {
      User currentUser = findLoggedUser()

      InviteMe inviteMe = InviteMe.get(params.inviteId)

      if (inviteMe){
        try {
          sendInvitation(inviteMe)
          inviteMe.invited=new Date()
          inviteMe.save(flush:true)
          flash.message="Invitation sent to: "+inviteMe.name+"."
        } catch (Exception e) {
          flash.message="There was a problem trying to send an invitation to "+params.name+"."
        }
      }

      def requestedInvitationsList = InviteMe.findAllByCompany(currentUser.company)
      render(view: "list", model: [requestedInvitations: requestedInvitationsList])
    }

    def dismiss = {
      User currentUser = findLoggedUser()

      InviteMe inviteMe = InviteMe.get(params.dismissId)
      if (inviteMe){
        inviteMe.delete(flush:true)
        flash.message="User request dismissed: "+inviteMe.name
      } else {
        flash.message="There was a problem dismissing the selected request."
      }

      def requestedInvitationsList = InviteMe.findAllByCompany(currentUser.company)
      render(view: "list", model: [requestedInvitations: requestedInvitationsList])

    }

    void sendInvitation(InviteMe inviteMe){

      Invitation invitation = new Invitation()
      invitation.inviter = findLoggedUser()
      invitation.invitee = inviteMe.email
      invitation.invited = new Date()
      invitation.code = authenticateService.encodePassword(String.valueOf(System.currentTimeMillis()) + invitation.invitee)
      invitation.validate()

      if (!invitation.hasErrors()) {
        invitation.save()
        Configuration serverAddress = Configuration.findByOptionName("app.url")

        def email = [
                to: [invitation.invitee],
                subject: getMessage(request, 'invitation.mail.subject'),
                from: getMessage(request, 'application.email'),
                text: getMessage(request, 'invitation.mail.body', ["${request.scheme}://${serverAddress.optionStringValue}${request.contextPath}/register/acceptInvitation?code=${invitation.code}"] as Object[])
        ]
        emailerService.sendEmails([email])
      }


    }





}