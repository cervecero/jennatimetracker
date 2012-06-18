
/**
 * @author Leandro Larroulet
 * Date: 07/10/2010
 * Time: 17:19:51
 */
class Score {

  User user
  Company company
  Date date
  String description
  int points
  ScoreCategory scoreCategory

  boolean deleted = false

  static belongsTo = User

  static constraints = {
    user(nullable:false)
    company(nullable:false)
    description(nullable:true)
    date(nullable:true)
  }

  static hibernateFilters = {
    enabledFilter(condition:'deleted=0', default:true)
  }
}
