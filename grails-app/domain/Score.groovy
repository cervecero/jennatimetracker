
/**
 * Created by IntelliJ IDEA.
 * User: Lea
 * Date: 07/10/2010
 * Time: 17:19:51
 * To change this template use File | Settings | File Templates.
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
    points(nullable:false)
    description(nullable:true)
    date(nullable:true)
  }

  static hibernateFilters = {
    enabledFilter(condition:'deleted=0', default:true)
  }
}
