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

      //  Adding SoftDeleteListener to override default onDelete behaviour.
       def ctx = servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
       def sessionFactory = ctx.sessionFactory
       EventListeners eventListeners = sessionFactory.eventListeners
       eventListeners.deleteEventListeners[0] = new SoftDeleteListener()



        System.setProperty('user.language', 'en')
        System.setProperty('user.country', 'US')
        Locale.setDefault(Locale.ENGLISH)
        if (GrailsUtil.isDevelopmentEnv()) {
            JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["jquery/jquery-1.7"]
            JavascriptTagLib.LIBRARY_MAPPINGS.jquery_ui = ["jquery-ui/jquery-ui-1.8.16.custom"]
            JavascriptTagLib.LIBRARY_MAPPINGS.flot = ["flot/jquery.flot"]
            JavascriptTagLib.LIBRARY_MAPPINGS.excanvas = ["flot/excanvas"]
            JavascriptTagLib.LIBRARY_MAPPINGS.calendar = ["calendar/fullcalendar"]
            JavascriptTagLib.LIBRARY_MAPPINGS.qtip = ["qtip/jquery.qtip-1.0.0-rc3"]
            JavascriptTagLib.LIBRARY_MAPPINGS.jgrowl = ["jgrowl/jquery.jgrowl"]
            JavascriptTagLib.LIBRARY_MAPPINGS.fgmenu = ["fg.menu"]
            JavascriptTagLib.LIBRARY_MAPPINGS.query = ["jquery.query-2.1.7"]
        } else {
            JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["jquery/jquery-1.7.min"]
            JavascriptTagLib.LIBRARY_MAPPINGS.jquery_ui = ["jquery-ui/jquery-ui-1.8.16.custom.min"]
            JavascriptTagLib.LIBRARY_MAPPINGS.flot = ["flot/jquery.flot.min"]
            JavascriptTagLib.LIBRARY_MAPPINGS.excanvas = ["flot/excanvas.min"]
            JavascriptTagLib.LIBRARY_MAPPINGS.calendar = ["calendar/fullcalendar"]
            JavascriptTagLib.LIBRARY_MAPPINGS.qtip = ["qtip/jquery.qtip-1.0.0-rc3.min"]
            JavascriptTagLib.LIBRARY_MAPPINGS.jgrowl = ["jgrowl/jquery.jgrowl"]
            JavascriptTagLib.LIBRARY_MAPPINGS.fgmenu = ["fg.menu"]
            JavascriptTagLib.LIBRARY_MAPPINGS.query = ["jquery.query-2.1.7"]
        }
        def getOnlyDate = { ->
            Calendar calendar = Calendar.getInstance()
            calendar.time = delegate
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.time
        }
        def getOnlyDateUTC = { ->
            Calendar calendar = Calendar.getInstance()
            calendar.time = delegate
            calendar.set(Calendar.ZONE_OFFSET, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.time
        }
        def getISO8601Formatted = {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            format.timeZone = TimeZone.getTimeZone("Etc/UTC")
            format.format(delegate)
        }
        Date.metaClass.getOnlyDate = getOnlyDate
        Date.metaClass.getOnlyDateUTC = getOnlyDateUTC
        Date.metaClass.getISO8601Formatted = getISO8601Formatted
        java.sql.Date.metaClass.getOnlyDate = getOnlyDate
        java.sql.Date.metaClass.getOnlyDateUTC = getOnlyDateUTC
        java.sql.Date.metaClass.getISO8601Formatted = getISO8601Formatted
        java.sql.Time.metaClass.getOnlyDate = getOnlyDate
        java.sql.Time.metaClass.getOnlyDateUTC = getOnlyDateUTC
        java.sql.Time.metaClass.getISO8601Formatted = getISO8601Formatted
        java.sql.Timestamp.metaClass.getOnlyDate = getOnlyDate
        java.sql.Timestamp.metaClass.getOnlyDateUTC = getOnlyDateUTC
        java.sql.Timestamp.metaClass.getISO8601Formatted = getISO8601Formatted

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
		ReconnectJob.schedule(grailsApplication.config.reconnect.cronExpression)
		// FIXME: Reminders are not fully-implemented, so we're deactivating this
        //ReminderJob.schedule(grailsApplication.config.chat.cronExpression)
    }

    def destroy = {
    }
}
