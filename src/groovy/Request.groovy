/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 30, 2009
 * Time: 12:34:56 AM
 */
class Request {

    User user
    String message

    def getLocale() {
        user.locale
    }
}
