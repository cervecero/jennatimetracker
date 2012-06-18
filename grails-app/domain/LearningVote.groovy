
/**
 * @author Leandro Larroulet
 * Date: 29/09/2010
 * Time: 15:40:03
 */
import org.hibernate.type.LocaleType
import org.hibernate.type.TimeZoneType

class LearningVote {

    User user
    Date date
    Integer vote
    Learning learning

    boolean deleted  = false

    String toString() {
        "$user voted $learning with $vote on $date"
    }

    static belongsTo = Learning

    static constraints = {
        user(nullable: false)
        date(nullable: false)
        vote(nullable: false)
        learning(nullable: false)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
