import org.springframework.security.context.SecurityContextHolder as SCH
import org.springframework.security.providers.UsernamePasswordAuthenticationToken
import org.springframework.security.GrantedAuthority
import org.springframework.security.GrantedAuthorityImpl
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserImpl
import org.json.JSONObject
import groovy.text.SimpleTemplateEngine

/**
 * Registration controller.
 */
class RegisterController extends BaseController {

	def authenticateService
	def daoAuthenticationProvider
	def emailerService
    def jabberService
    def grailsApplication

	static Map allowedMethods = [save: 'POST', update: 'POST']

	/**
	 * User Registration Top page.
	 */
	def index = {
        //          TODO timeZoneSelect from FormTagLib
        //          TODO localeSelect from FormTagLib

		// skip if already logged in
		if (authenticateService.isLoggedIn()) {
			redirect action: show
			return
		}

		if (session.id) {
			def person = new RegisterCommand()
            bindData(person, params)
			return [person: person, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales()]
		}

		redirect uri: '/'
	}

	/**
	 * User Information page for current user.
	 */
	def show = {

		// get user id from session's domain class.
		def user = authenticateService.userDomain()
		if (user) {
			render view: 'show', model: [person: User.get(user.id)]
		}
		else {
			redirect action: index
		}
	}

	/**
	 * Edit page for current user.
	 */
	def edit = {

		def person
		def user = authenticateService.userDomain()
		if (user) {
			person = User.get(user.id)
		}

		if (!person) {
			flash.message = "[Illegal Access] User not found with id ${params.id}"
			redirect action: index
			return
		}

		[person: person]
	}

	/**
	 * update action for current user's edit page
	 */
	def update = {

		def person
		def user = authenticateService.userDomain()
		if (user) {
			person = User.get(user.id)
		}
		else {
			redirect action: index
			return
		}

		if (!person) {
			flash.message = "[Illegal Access] User not found with id ${params.id}"
			redirect action: index, id: params.id
			return
		}

		// if user want to change password. leave pass field blank, pass will not change.
		if (params.pass && params.pass.length() > 0
				&& params.repass && params.repass.length() > 0) {
			if (params.pass == params.repass) {
				person.pass = authenticateService.encodePassword(params.pass)
			}
			else {
				person.pass = ''
				flash.message = 'The passwords you entered do not match.'
				render view: 'edit', model: [person: person]
				return
			}
		}

		person.name = params.name

		if (person.save()) {
			redirect action: show, id: person.id
		}
		else {
			render view: 'edit', model: [person: person]
		}
	 }

	/**
	 * Person save action.
	 */
	def save = { RegisterCommand cmd ->

		// skip if already logged in
		if (authenticateService.isLoggedIn()) {
			redirect action: show
			return
		}

//        cmd.validate()
        if (cmd.hasErrors()) {
            render view: 'index', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales()]
            return
        }

		def config = authenticateService.securityConfig
		def defaultRole = config.security.defaultRole

		def role = Permission.findByName(defaultRole)
		if (params.captcha.toUpperCase() != session.captcha) {
          //person no existe
			cmd.password = ''
			cmd.repassword = ''
			flash.message = 'Access code did not match.'
			render view: 'index', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales()]
			return
		}

        Company company = Company.findByName(cmd.companyName)
        if (company) {
            //TODO adds company admin permission
            def companyOwnersList = findCompanyOwners(company)

            String owners = ""
            companyOwnersList.each { User user ->
              owners+=user.name+" "
            }
            cmd.errors.rejectValue('companyName', 'user.company.exists.error', [company.name, owners] as Object[], '')

            InviteMe inviteMe = InviteMe.findByEmailAndCompany(params.account, company)
            if (inviteMe == null){
              inviteMe = new InviteMe()
              inviteMe.name = params.name
              inviteMe.email = params.account
              inviteMe.requested = new Date()
              inviteMe.company = company
              inviteMe.save()
            }

            rememberCompanyOwnerPendingInvitations(company)

            render view: 'index', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales()]
            return
        }
        def permissions = []
        permissions << role

        def person = new User()
        person.properties = params
        person.humour= grailsApplication.config['jenna']['defaultHumour']
		person.password = authenticateService.encodePassword(cmd.password)

        company = new Company(name: cmd.companyName)
        permissions << Permission.findByName(Permission.ROLE_COMPANY_ADMIN)
        permissions << Permission.findByName(Permission.ROLE_PROJECT_LEADER)

        person.company = company
        person.activationHash = authenticateService.encodePassword(person.account + company.name)
        person.validate()
		if (!person.hasErrors()) {
            company.addToEmployees(person)
            company.save()
            permissions.each { permission ->
    			permission.addToUsers(person)
            }


            // Now that we've created the company, we can create Score Categories.
            createScoreCategories(company)
            createDefaultInformation(company,person)

			if (config.security.useMail) {

                // Send registration email based on current locale.
              String templatePath = File.separator + "templates" + File.separator + getMessage(request, 'registration.mail.template')
              File tplFile = grailsAttributes.getApplicationContext().getResource(templatePath).getFile();

			  String linkHash = createLink(controller:"register",action:"activate",params:[hash:person.activationHash],absolute:true)
              def binding = ["name": person.name, "account": person.account, "accountToAdd": getMessage(request, 'application.email.account'), "linkHash": linkHash]

              String invitee = person.account

              def engine = new SimpleTemplateEngine()
              def template = engine.createTemplate(tplFile).make(binding)

              def body = template.toString()

              def email = [
                      to: [invitee], // 'to' expects a List, NOT a single email address
                      subject: getMessage(request, 'registration.mail.subject'),
                      from: getMessage(request, 'application.email'),
                      text: body
              ]
              emailerService.sendEmails([email])
			}

          // Finally, we save the newly registered person.
          person.save()

          render view: 'success', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales()]
          return
        } else {
          cmd.password = ''
          cmd.repassword = ''
          cmd.errors = person.errors
          render view: 'index', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales()]
        }
	}

    def acceptInvitation = {
        Invitation invitation = Invitation.findByCode(params.code)
        if (!invitation || invitation.accepted) {
            redirect(uri: '/')
            return
        }
        def person = new RegisterCommand()
        person.account = invitation.invitee
        person.companyName = invitation.inviter.company.name
        person.locale = invitation.inviter.locale
        person.timeZone = invitation.inviter.timeZone
        return [person: person, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales(), code: invitation.code]

    }

    private void createScoreCategories(Company company){
      def scoreCategoryKnowledgeLearning = ScoreCategory.withCriteria {
          eq("category", "KNOWLEDGE")
          eq("subCategory", "LEARNING")
          eq("company", company)
      }
      def scoreCategoryKnowledgeVote = ScoreCategory.withCriteria {
          eq("category", "KNOWLEDGE")
          eq("subCategory", "VOTE")
          eq("company", company)
      }
      def scoreCategoryKnowledgeTag = ScoreCategory.withCriteria {
          eq("category", "KNOWLEDGE")
          eq("subCategory", "TAG")
          eq("company", company)
      }
      if (!scoreCategoryKnowledgeLearning){
        scoreCategoryKnowledgeLearning = new ScoreCategory(company: company, category: "KNOWLEDGE", subCategory: "LEARNING", points: 5)
        scoreCategoryKnowledgeLearning.save()
      }
      if (!scoreCategoryKnowledgeVote){
        scoreCategoryKnowledgeVote = new ScoreCategory(company: company, category: "KNOWLEDGE", subCategory: "VOTE", points: 1)
        scoreCategoryKnowledgeVote.save()
      }
      if (!scoreCategoryKnowledgeTag){
        scoreCategoryKnowledgeTag = new ScoreCategory(company: company, category: "KNOWLEDGE", subCategory: "TAG", points: 2)
        scoreCategoryKnowledgeTag.save()
      }
    }

    public void createDefaultInformation(Company company, User user){

      Role role = Role.findByCompanyAndName(company,g.message(code:'default.role.name'))
      if (!role){
        role = new Role()
        role.name=        g.message(code:'default.role.name')
        role.description= g.message(code:'default.role.description')
        role.company=     company
      }

      Tag clientTag = Tag.withCriteria(uniqueResult:true){
        eq("company", company)
        eq("category", TagCategory.findByName("client"))
        eq("name", g.message(code:'default.tag.client.name'))
      }
      if (!clientTag){
        clientTag = new Tag()
        clientTag.category=TagCategory.findByName("client");
        clientTag.name=    g.message(code:'default.tag.client.name')
        clientTag.company= company
      }

      Tag projectTag = Tag.withCriteria(uniqueResult:true){
        eq("company", company)
        eq("category", TagCategory.findByName("project"))
        eq("name", g.message(code:'default.tag.project.name'))
      }
      if (!projectTag){
        projectTag = new Tag()
        projectTag.category=TagCategory.findByName("project");
        projectTag.name=    g.message(code:'default.tag.project.name')
        projectTag.company= company
      }


      Project project = Project.findByCompanyAndName(company,g.message(code:'default.project.name'))
      if (!project){
        project = new Project()
        project.name=         g.message(code:'default.project.name')
        project.description=  g.message(code:'default.project.description')
        project.startDate=    new Date()-1
        project.endDate=      new Date()+365
        project.company=      company
        project.active=       true
      }

      Assignment ass = new Assignment()
      ass.description= g.message(code:'default.assignment.description')
      ass.project=     project
      ass.user=        user
      ass.role=        role
      ass.startDate=   new Date()-1
      ass.endDate=     new Date()+365

      role.save()
      clientTag.save()
      projectTag.save()
      project.save()
      ass.save()
    }


    def saveInvitation = { RegisterCommand cmd ->
        Invitation invitation = Invitation.findByCode(params.code)
        if (!invitation || invitation.accepted) {
            redirect(uri: '/')
            return
        }
        cmd.companyName = invitation.inviter.company.name
        cmd.account = invitation.invitee
        // not on grails 1.1.1
        //cmd.clearErrors()
        cmd.validate()
        if (cmd.hasErrors()) {
            render view: 'acceptInvitation', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales(), code: invitation.code]
            return
        }

        def config = authenticateService.securityConfig

        if (params.captcha.toUpperCase() != session.captcha) {
            cmd.password = ''
            cmd.repassword = ''
            flash.message = 'Access code did not match.'
            render view: 'acceptInvitation', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales(), code: invitation.code]
            return
        }

        Company company = invitation.inviter.company
        def permissions = []
        def defaultRole = config.security.defaultRole
        def role = Permission.findByName(defaultRole)
        permissions << role

        def person = new User()
        person.properties = params
        person.password = authenticateService.encodePassword(cmd.password)
        person.company = company
        person.activationHash = authenticateService.encodePassword(person.account + company.name)
        person.validate()
        if (!person.hasErrors()) {
            company.addToEmployees(person)
            company.save()
            permissions.each { permission ->
                permission.addToUsers(person)
            }
            person.enabled = true
            person.save()

            // Now that we've created the company, we can create Score Categories.
            createDefaultInformation(company,person)


            jabberService.addAccount(person.account, person.name)

            invitation.accepted = new Date()
            invitation.save()
            GrantedAuthority[] authorities = person.permissions.collect {Permission p ->
                new GrantedAuthorityImpl(p.name)
            } as GrantedAuthority[]
            def authtoken = new UsernamePasswordAuthenticationToken(new GrailsUserImpl(person.account, 'dummy', true, true, true, true, authorities, person), 'dummy', authorities)
            SCH.context.authentication = authtoken
            redirect uri: '/'
        } else {
            cmd.password = ''
            cmd.repassword = ''
            cmd.errors = person.errors
            render view: 'acceptInvitation', model: [person: cmd, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales(), code: invitation.code]
        }
    }

    def activate = {
        User user = User.findByActivationHash(params.hash)
        if (user && !user.enabled) {
            user.enabled = true
            user.save(flush: true)

            jabberService.addAccount(user.account, user.name)

            GrantedAuthority[] authorities = user.permissions.collect {Permission p ->
                new GrantedAuthorityImpl(p.name)
            } as GrantedAuthority[]
            def authtoken = new UsernamePasswordAuthenticationToken(new GrailsUserImpl(user.account, 'dummy', true, true, true, true, authorities, user), 'dummy', authorities)
			SCH.context.authentication = authtoken
        }
        redirect uri: '/'
    }

    def ajaxInvite = {
        JSONObject jsonResponse
        if (!params.invitee || !jabberService.isValidAccount(params.invitee)) {
            jsonResponse = buildJsonErrorResponse(request, buildMessageSourceResolvable('invalid.email'))
            render jsonResponse.toString()
            return
        }
        if (User.findByAccount(params.invitee)) {
            jsonResponse = buildJsonErrorResponse(request, buildMessageSourceResolvable('invitation.already.registered', [params.invitee] as Object[]))
            render jsonResponse.toString()
            return
        }
        Invitation invitation = new Invitation()
        invitation.inviter = findLoggedUser()
        invitation.invitee = params.invitee
        invitation.invited = new Date()
        invitation.code = authenticateService.encodePassword(String.valueOf(System.currentTimeMillis()) + invitation.invitee)
        invitation.validate()
        if (!invitation.hasErrors()) {
            invitation.save()

            def email = [
                    to: [invitation.invitee],
                    subject: getMessage(request, 'invitation.mail.subject'),
                    from: getMessage(request, 'application.email'),
                    text: getMessage(request, 'invitation.mail.body', [createLink(absoulte:true, controller:"register",action:"acceptInvitation",params:[code:invitation.code])] as Object[])
            ]
            emailerService.sendEmails([email])
            jsonResponse = buildJsonOkResponse(request, buildMessageSourceResolvable('confirm'), buildMessageSourceResolvable('invitation.sent'))
            render jsonResponse.toString()
        } else {
            jsonResponse = buildJsonErrorResponse(request, invitation.errors)
            render jsonResponse.toString()
        }
    }

    List findCompanyOwners(Company company){
      Permission permission = Permission.findByName(Permission.ROLE_COMPANY_ADMIN)

      def usersList = User.findAllByCompany(company)
      List owners = new ArrayList()

      usersList.each { User user ->
        if (user.permissions.contains(permission))
          owners.add(user)
      }

      return owners
    }

    void rememberCompanyOwnerPendingInvitations(Company company){

        def ownersList = findCompanyOwners(company)

        ownersList.each{ User owner ->
           def email = [
                    to: [owner.account],
                    subject: getMessage(request, 'invitation.requested.subject'),
                    from: getMessage(request, 'application.email'),
                    text: getMessage(request, 'invitation.requested.body')
            ]
            emailerService.sendEmails([email])
        }


    }
}

class RegisterCommand {

    String account
    String password
    String repassword
    String name
    String companyName
    String chatTime
    TimeZone timeZone
    Locale locale
    String captcha

    static constraints = {
        account(nullable: false, blank: false, email: true, size: 5..255)
        password(nullable: false, blank: false, size: 4..20)
        repassword(validator: { val, obj ->
            obj.properties['password'] == val ? null : 'default.invalid.validator.message'
        })
        name(nullable: false, blank: false, size: 2..255)
        chatTime(nullable: true)
        companyName(nullable: false)
        captcha(nullable: false, blank: false)
    }
}
