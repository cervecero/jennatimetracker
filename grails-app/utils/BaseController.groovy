import org.springframework.context.MessageSource
import org.springframework.web.servlet.LocaleResolver
import javax.servlet.http.HttpServletRequest
import org.springframework.context.NoSuchMessageException
import org.springframework.context.MessageSourceResolvable
import org.json.JSONObject
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.validation.ObjectError
import org.springframework.validation.BeanPropertyBindingResult
import java.text.SimpleDateFormat
import org.springframework.web.servlet.support.RequestContextUtils
import java.text.DateFormat
import org.springframework.security.context.SecurityContextHolder

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 12, 2009
 * Time: 10:40:09 PM
 */
class BaseController {

    MessageSource messageSource

    boolean checkCompany(def _account) {
        User user = User.findByAccount(_account)
        return findLoggedUser().company == user?.company
    }

    boolean isAuthenticated() {
        def authPrincipal = SecurityContextHolder?.context?.authentication?.principal
        return authPrincipal != null && authPrincipal != 'anonymousUser'
    }

    String findLoggedUsername() {
        return SecurityContextHolder.context.authentication?.principal?.username
    }

    User findLoggedUser() {
        return User.findByAccount(findLoggedUsername())
    }

    Company findLoggedCompany() {
        return findLoggedUser().company
    }

    def setUpDefaultPagingParams(params, sortProperty = 'name') {
        params.offset = params.offset ? params.offset.toInteger() : 0
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        if (!params.sort) {
            params.sort = sortProperty
        }
        if (!params.order) {
            params.order = 'asc'
        }
    }

    String getMessage(HttpServletRequest _request, String _msgKey, Object[] _args = null) {
        try {
            return messageSource.getMessage(_msgKey, _args, getLocale(_request))
        } catch (NoSuchMessageException ex) {
            return "Missing message: $_msgKey"
        }
    }

    String getMessage(HttpServletRequest _request, MessageSourceResolvable _message) {
        try {
            return messageSource.getMessage(_message, getLocale(_request))
        } catch (NoSuchMessageException ex) {
            return "Missing message: $_message"
        }
    }

    def Locale getLocale(HttpServletRequest _request) {
        return RequestContextUtils.getLocale(_request) ?: Locale.default
    }

    JSONObject buildJsonOkResponse(HttpServletRequest _request, MessageSourceResolvable _title, MessageSourceResolvable _message) {
        JSONObject jsonResponse = new JSONObject()
        jsonResponse.put('ok', true).put('title', getMessage(_request, _title)).put('message', getMessage(_request, _message))
        return jsonResponse
    }

    JSONObject buildJsonErrorResponse(HttpServletRequest _request, MessageSourceResolvable _message) {
        JSONObject jsonResponse = new JSONObject()
        jsonResponse.put('ok', false).put('message', getMessage(_request, _message))
        return jsonResponse
    }

    JSONObject buildJsonErrorResponse(HttpServletRequest _request, BeanPropertyBindingResult _bindingResult) {
        def errors = [:]
        _bindingResult.allErrors.each { ObjectError error ->
            errors[(error.field)] = getMessage(_request, error)
        }
        JSONObject jsonResponse = new JSONObject()
        jsonResponse.put('ok', false).put('errors', errors)
        return jsonResponse
    }

    MessageSourceResolvable buildMessageSourceResolvable(String _code, Object[] _args = null) {
        String[] codes = new String[1]
        codes[0] = _code
        return new DefaultMessageSourceResolvable(codes, _args as Object[])
    }

    DateFormat getDateFormat(HttpServletRequest _request) {
        Locale locale = getLocale(_request)
        def pattern = messageSource.getMessage('onlyDate.format', null, locale)
        return new SimpleDateFormat(pattern, locale)
    }

    String formatDate(HttpServletRequest _request, Date _date) {
        return getDateFormat(_request).format(_date)
    }
}
