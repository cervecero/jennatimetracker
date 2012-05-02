/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 28, 2009
 * Time: 10:47:56 PM
 */
class LocaleUtil {

    def static availableLocales = [new Locale('en'), new Locale('es')]

    def static defaultLocale = availableLocales[0]
}