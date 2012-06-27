import java.text.DateFormat
import org.springframework.context.MessageSource
import eliza.ElizaMain

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 30, 2009
 * Time: 1:36:12 AM
 */
class Conversation {

    Request actualRequest
    List<Response> responses = []
    Long lastMessageTime
    String humour
    def handlers = []
    Map context = [:]
    MessageSource messageSource
    ElizaMain eliza

    Locale getLocale() {
        actualRequest.locale
    }

    Date parseDate(String _value) {
        if (getMessage('today') == _value) {
            return new Date().clearTime()
        } else if (getMessage('tomorrow') == _value) {
            return (new Date() + 1).clearTime()
        } else if (getMessage('yesterday') == _value) {
            return (new Date() - 1).clearTime()
        } else {
            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale)
            return format.parse(_value)
        }
    }

    boolean isList() {
        return matches('list')
    }

    boolean isAffirmative() {
        return matches('affirmative')
    }

    boolean isNegative() {
        return matches('negative')
    }

    boolean matches(String _msgKey) {
        String message = actualRequest.message
        def pattern = getMessage(_msgKey)
        def matcher = message =~ pattern
        if (matcher.matches()) {
            context.command = matcher[0][1]
            context.arguments = matcher[0].size() > 2 ? matcher[0][2] : null
            return true
        } else {
            return false
        }
    }

    String getMessage(String _msgKey, Object[] _params = null) {
        return messageSource.getMessage(_msgKey, _params, locale)
    }
}
