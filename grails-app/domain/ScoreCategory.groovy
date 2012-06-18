
/**
 * @author Leandro Larroulet
 * Date: 07/10/2010
 * Time: 17:20:16
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
