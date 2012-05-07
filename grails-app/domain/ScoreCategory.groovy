
/**
 * Created by IntelliJ IDEA.
 * User: Lea
 * Date: 07/10/2010
 * Time: 17:20:16
 * To change this template use File | Settings | File Templates.
 */
class ScoreCategory {
  Company company
  String category
  String subCategory
  int   points

  boolean deleted = false

  static constraints = {
    company(nullable:false)
    category(nullable:false)
    subCategory(nullable:false)
  }

  static hibernateFilters = {
    enabledFilter(condition:'deleted=0', default:true)
  }
}
