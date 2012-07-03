import grails.util.GrailsUtil
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.plugins.web.taglib.JavascriptTagLib
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.event.EventListeners
import org.codehaus.groovy.grails.commons.ApplicationAttributes

class BootStrap {

    def authenticateService
	GrailsApplication grailsApplication

    def init = { servletContext ->

        // Adding SoftDeleteListener to override default onDelete behaviour.
        def ctx = servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
        def sessionFactory = ctx.sessionFactory
        EventListeners eventListeners = sessionFactory.eventListeners
        eventListeners.deleteEventListeners[0] = new SoftDeleteListener()

        System.setProperty('user.language', 'en')
        System.setProperty('user.country', 'US')
        Locale.setDefault(Locale.ENGLISH)

		/*
		 Seed Data...
		 TODO: Move to a plugin that supports this?
		 */
        def roleUser = Permission.findByName(Permission.ROLE_USER)
        if (!roleUser) {
            roleUser = new Permission(name: Permission.ROLE_USER, description: 'The user role')
            roleUser.save()
        }
        def roleSysAdmin = Permission.findByName(Permission.ROLE_SYSTEM_ADMIN)
        if (!roleSysAdmin) {
            roleSysAdmin = new Permission(name: Permission.ROLE_SYSTEM_ADMIN, description: 'The administrator role')
            roleSysAdmin.save()
        }
        def roleCompanyAdmin = Permission.findByName(Permission.ROLE_COMPANY_ADMIN)
        if (!roleCompanyAdmin) {
            roleCompanyAdmin = new Permission(name: Permission.ROLE_COMPANY_ADMIN, description: 'The company administrator role')
            roleCompanyAdmin.save()
        }
        def roleProjectLeader = Permission.findByName(Permission.ROLE_PROJECT_LEADER)
        if (!roleProjectLeader) {
            roleProjectLeader = new Permission(name: Permission.ROLE_PROJECT_LEADER, description: 'The project leader role')
            roleProjectLeader.save()
        }

		def clientCategory = TagCategory.findByName(TagCategory.CATEGORY_CLIENT)
        if (!clientCategory) {
            clientCategory = new TagCategory(name: TagCategory.CATEGORY_CLIENT)
            clientCategory.save()
        }
        def projectCategory = TagCategory.findByName(TagCategory.CATEGORY_PROJECT)
        if (!projectCategory) {
            projectCategory = new TagCategory(name: TagCategory.CATEGORY_PROJECT)
            projectCategory.save()
        }
        def taskCategory = TagCategory.findByName(TagCategory.CATEGORY_TASK)
        if (!taskCategory) {
            taskCategory = new TagCategory(name: TagCategory.CATEGORY_TASK)
            taskCategory.save()
        }
		
		// FIXME: Is there any better way to force Singleton beans to be loaded eagerly?
		grailsApplication.getMainContext().getBean("jabberService")

		/**
		 * Fire up the jobs
		 * As there's no (easy/proper) way to access the config from the static triggers definition,
		 * we have to schedule the Jobs here (and pray not to forget any)
		 */
		ChattingJob.schedule(grailsApplication.config.chat.cronExpression)
		InviteCoworkersJob.schedule(grailsApplication.config.chat.cronExpression)
		ProjectFollowUpJob.schedule(grailsApplication.config.projectFollowUp.cronExpression)
        NewKnowledgesHeadUpJob.schedule(grailsApplication.config.knowledgeHeadsUp.cronExpression)
        MoodWarningHeadsUpJob.schedule(grailsApplication.config.moodWarningHeadsUp.cronExpression)
		// FIXME: Reminders are not fully-implemented, so we're deactivating this
        //ReminderJob.schedule(grailsApplication.config.chat.cronExpression)
    }

    def destroy = {
    }
}
