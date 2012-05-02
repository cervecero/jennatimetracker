

import org.compass.core.converter.basic.AbstractBasicConverter
import org.compass.core.mapping.ResourcePropertyMapping
import org.compass.core.marshall.MarshallingContext

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: 4/16/12
 * Time: 11:56 AM
 */
class UserConverter extends AbstractBasicConverter<User> {

    @Override
    protected User doFromString(String s, ResourcePropertyMapping resourcePropertyMapping, MarshallingContext marshallingContext) {
        return User.load(Long.valueOf(s))
    }

    @Override
    protected String doToString(User o, ResourcePropertyMapping resourcePropertyMapping, MarshallingContext context) {
        return o.id
    }
}
