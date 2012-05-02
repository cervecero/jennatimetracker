import org.springframework.context.MessageSourceResolvable
import org.springframework.context.support.DefaultMessageSourceResolvable

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 30, 2009
 * Time: 12:42:38 AM
 */
class Response {

    def text
    MessageSourceResolvable message

    static Response build(msgKey, params = null) {
        String[] codes
        if (msgKey instanceof String) {
            codes = new String[1]
            codes[0] = msgKey
        } else {
            codes = msgKey as String[]
        }
        return new Response(message: new DefaultMessageSourceResolvable(codes, params as Object[]))
    }
}
