import grails.test.GrailsUnitTestCase
import org.springframework.context.support.StaticMessageSource

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 4, 2009
 * Time: 12:59:40 AM
 */
class WizardTests extends GrailsUnitTestCase {

    StaticMessageSource messageSource

    void setUp() {
        messageSource = new StaticMessageSource()
        messageSource.addMessage('today', Locale.ENGLISH, 'today')
        messageSource.addMessage('tomorrow', Locale.ENGLISH, 'tomorrow')
        messageSource.addMessage('yesterday', Locale.ENGLISH, 'yesterday')
        def getOnlyDate = { ->
            Calendar calendar = Calendar.getInstance()
            calendar.time = delegate
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.time
        }
        Date.metaClass.getOnlyDate = getOnlyDate
        java.sql.Date.metaClass.getOnlyDate = getOnlyDate
        java.sql.Time.metaClass.getOnlyDate = getOnlyDate
        java.sql.Timestamp.metaClass.getOnlyDate = getOnlyDate
    }

    void testFirstStepOk() {
        Conversation conversation = new Conversation(actualRequest: new Request(user: new User(locale: Locale.ENGLISH)), messageSource: messageSource)
        conversation.context.project = new Project()
        Wizard w = new CreateProjectWizard()
        w.execute(conversation, ['proyecto'])
        assertEquals(1, w.actualStepIndex)
        assertFalse(w.finished)
        assertEquals(Response.build(SetProjectStartDate.INSTANCE.msgCode).message, conversation.responses[0].message)
        assertEquals('proyecto', conversation.context.project.name)
        assertNull(conversation.context.project.startDate)
        assertNull(conversation.context.project.endDate)
    }

    void testFirst2StepsOk() {
        Conversation conversation = new Conversation(actualRequest: new Request(user: new User(locale: Locale.ENGLISH)), messageSource: messageSource)
        conversation.context.project = new Project()
        Wizard w = new CreateProjectWizard()
        w.execute(conversation, ['proyecto', '02/25/1998'])
        assertEquals(2, w.actualStepIndex)
        assertFalse(w.finished)
        assertEquals('proyecto', conversation.context.project.name)
        assertEquals(25, conversation.context.project.startDate.date)
        assertEquals(1, conversation.context.project.startDate.month)
        assertEquals(98, conversation.context.project.startDate.year)
        assertNull(conversation.context.project.endDate)
    }

    void testFirst2StepsError() {
        Conversation conversation = new Conversation(actualRequest: new Request(user: new User(locale: Locale.ENGLISH)), messageSource: messageSource)
        conversation.context.project = new Project()
        Wizard w = new CreateProjectWizard()
        w.execute(conversation, ['proyecto', '02/25/'])
        assertEquals(1, w.actualStepIndex)
        assertFalse(w.finished)
        assertEquals(Response.build(SetProjectStartDate.INSTANCE.errorMsgCode).message, conversation.responses[0].message)
        assertEquals(Response.build(SetProjectStartDate.INSTANCE.msgCode).message, conversation.responses[1].message)
        assertEquals('proyecto', conversation.context.project.name)
        assertNull(conversation.context.project.startDate)
        assertNull(conversation.context.project.endDate)
    }

    void testFirst3StepsOk() {
        Conversation conversation = new Conversation(actualRequest: new Request(user: new User(locale: Locale.ENGLISH)), messageSource: messageSource)
        conversation.context.project = new Project()
        Wizard w = new CreateProjectWizard()
        w.execute(conversation, ['proyecto', '02/25/1998', 'today'])
        assertEquals(3, w.actualStepIndex)
        assertTrue(w.finished)
        assertEquals('proyecto', conversation.context.project.name)
        assertEquals(25, conversation.context.project.startDate.date)
        assertEquals(1, conversation.context.project.startDate.month)
        assertEquals(98, conversation.context.project.startDate.year)
        Date today = new Date()
        assertEquals(today.date, conversation.context.project.endDate.date)
        assertEquals(today.month, conversation.context.project.endDate.month)
        assertEquals(today.year, conversation.context.project.endDate.year)
    }

    void testFirst3StepsErrorAndResume() {
        Conversation conversation = new Conversation(actualRequest: new Request(user: new User(locale: Locale.ENGLISH)), messageSource: messageSource)
        conversation.context.project = new Project()
        Wizard w = new CreateProjectWizard()
        w.execute(conversation, ['proyecto', '02/25/1998', '02/25/'])
        assertEquals(2, w.actualStepIndex)
        assertFalse(w.finished)
        assertEquals('proyecto', conversation.context.project.name)
        assertEquals(25, conversation.context.project.startDate.date)
        assertEquals(1, conversation.context.project.startDate.month)
        assertEquals(98, conversation.context.project.startDate.year)
        assertNull(conversation.context.project.endDate)
        w.execute(conversation, ['today'])
        assertEquals(3, w.actualStepIndex)
        assertTrue(w.finished)
        assertEquals('proyecto', conversation.context.project.name)
        assertEquals(25, conversation.context.project.startDate.date)
        assertEquals(1, conversation.context.project.startDate.month)
        assertEquals(98, conversation.context.project.startDate.year)
        Date today = new Date()
        assertEquals(today.date, conversation.context.project.endDate.date)
        assertEquals(today.month, conversation.context.project.endDate.month)
        assertEquals(today.year, conversation.context.project.endDate.year)
    }
}
