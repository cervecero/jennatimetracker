import org.compass.core.marshall.MarshallingContext
import org.compass.core.converter.basic.AbstractBasicConverter
import org.compass.core.mapping.ResourcePropertyMapping

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: 4/16/12
 * Time: 11:56 AM
 */
class CompanyConverter extends AbstractBasicConverter<Company> {

    @Override
    protected Company doFromString(String s, ResourcePropertyMapping resourcePropertyMapping, MarshallingContext marshallingContext) {
        return Company.load(Long.valueOf(s))
    }

    @Override
    protected String doToString(Company o, ResourcePropertyMapping resourcePropertyMapping, MarshallingContext context) {
        return o.id
    }
}
