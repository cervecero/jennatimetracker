package reports

import java.text.DecimalFormatSymbols
import java.text.DecimalFormat
import org.springframework.web.servlet.support.RequestContextUtils

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: 2/29/12
 * Time: 4:09 PM
 */
class Formatters {

    static final ThreadLocal<Locale> LOCALE_BY_THREAD = new ThreadLocal<Locale>()

    def static setLocaleByRequest(request) {
        locale = (RequestContextUtils.getLocale(request) ?: Locale.default)
    }

    def static setLocale(locale) {
        LOCALE_BY_THREAD.set(locale)
    }

    def static getLocale() {
        return LOCALE_BY_THREAD.get()
    }

    def static dateFormatter = { domain, value ->
        return value.format('dd-MM-yyyy')
    }

    def static floatFormatter = { domain, value ->
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale)
        DecimalFormat format = new DecimalFormat('#0.00', symbols)
        return format.format(value)
    }

    def static intFormatter = { domain, value ->
        if (value == null) {
            return null
        }
        DecimalFormat format = new DecimalFormat('0')
        return format.format(value)
    }
}
