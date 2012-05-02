import grails.test.GrailsUnitTestCase

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 9, 2009
 * Time: 1:24:41 AM
 */
class RequestHandlerTests extends GrailsUnitTestCase {

    void testAccepts() {
        RequestHandler handler = new ActiveAssignmentsRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'assignments')
        assertHandles(handler, Locale.ENGLISH, 'AssIgnmEnts')
        assertDoesntHandle(handler, Locale.ENGLISH, 'assignments, baby')
        handler = new CancelRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'CANCEL')
        assertHandles(handler, Locale.ENGLISH, 'CaNCel')
        assertHandles(handler, Locale.ENGLISH, 'cancel please', 'cancel', 'please')
        assertDoesntHandle(handler, Locale.ENGLISH, 'cancelit')
        handler = new CreateProjectRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'create project')
        assertHandles(handler, Locale.ENGLISH, 'CREATE project NOW!!', 'CREATE project', 'NOW!!')
        assertDoesntHandle(handler, Locale.ENGLISH, 'qwerty')
        handler = new HelpRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'help')
        assertHandles(handler, Locale.ENGLISH, 'Help Me!! please!!', 'Help', 'Me!! please!!')
        assertDoesntHandle(handler, Locale.ENGLISH, 'qwerty')
        handler = new HumourRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'HUMOUR')
        assertHandles(handler, Locale.ENGLISH, 'HumouR sweet', 'HumouR', 'sweet')
        assertDoesntHandle(handler, Locale.ENGLISH, 'humours')
        handler = new LanguageRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'language')
        assertHandles(handler, Locale.ENGLISH, 'LANGUAGE en', 'LANGUAGE', 'en')
        assertDoesntHandle(handler, Locale.ENGLISH, 'languages')
        handler = new ReminderRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'reminder')
        assertHandles(handler, Locale.ENGLISH, 'Reminder 11:22 something to remind', 'Reminder', '11:22 something to remind')
        assertDoesntHandle(handler, Locale.ENGLISH, 'my reminder')
        handler = new SalutationRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'Hello')
        assertHandles(handler, Locale.ENGLISH, 'HEY!', 'HEY', null)
        assertDoesntHandle(handler, Locale.ENGLISH, '  Hello  ')
        handler = new TodayRequestHandler()
        assertHandles(handler, Locale.ENGLISH, 'Today')
        assertHandles(handler, Locale.ENGLISH, 'ToDaY')
        assertHandles(handler, Locale.ENGLISH, 'today efforts', 'today', 'efforts')
        assertDoesntHandle(handler, Locale.ENGLISH, 'todays efforts')
    }

    void assertHandles(RequestHandler _handler, Locale _locale, String _message) {
        assertHandles(_handler, _locale, _message, _message, null)
    }

    void assertHandles(RequestHandler _handler, Locale _locale, String _message, String _command, String _arguments) {
        User user = new User(locale: _locale)
        Request request = new Request(user: user, message: _message)
        Conversation conversation = new Conversation(actualRequest: request)
        assertTrue(_handler.accepts(conversation))
        assertEquals(_command, conversation.context.command)
        assertEquals(_arguments, conversation.context.arguments)
    }

    void assertDoesntHandle(RequestHandler _handler, Locale _locale, String _message) {
        User user = new User(locale: _locale)
        Request request = new Request(user: user, message: _message)
        Conversation conversation = new Conversation(actualRequest: request)
        assertFalse(_handler.accepts(conversation))
    }
}