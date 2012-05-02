
/**
 * Created by IntelliJ IDEA.
 * User: Lea
 * Date: 07/10/2010
 * Time: 09:55:40
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
class Configuration {

  String optionName

  String optionStringValue
  int optionIntegerValue
  double optionDoubleValue
  boolean optionBooleanValue

  boolean deleted = false

  static hibernateFilters = {
    enabledFilter(condition:'deleted=0', default:true)
  }

  static constraints = {
    optionName(nullable: false)
    optionStringValue(nullable: true)
    optionIntegerValue(nullable: true)
    optionDoubleValue(nullable: true)
    optionBooleanValue(nullable: true)
  }

}
