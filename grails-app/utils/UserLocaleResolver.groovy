import org.springframework.web.servlet.LocaleResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.context.SecurityContextHolder
import org.springframework.security.userdetails.UserDetails

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 16, 2009
 * Time: 7:57:47 PM
 */
class UserLocaleResolver implements LocaleResolver {

    Locale resolveLocale(HttpServletRequest _request) {
        def locale = _request.session.getAttribute('__USER_LOCALE__')
        if (!locale) {
            def principal = SecurityContextHolder.context.authentication?.principal
            if (principal && principal instanceof UserDetails) {
                User user = User.findByAccount(principal.username)
                locale = user.locale
            } else {
                locale = _request.locale
            }
            setLocale(_request, null, locale)
        }
        return locale
    }

    public void setLocale(HttpServletRequest _request, HttpServletResponse _response, Locale _locale) {
        _request.session.setAttribute('__USER_LOCALE__', _locale)
    }
}
