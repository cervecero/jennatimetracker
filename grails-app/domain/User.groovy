import org.hibernate.type.TimeZoneType
import org.hibernate.type.LocaleType

class User {

    String password
    String name
    String account
    String chatTime
    String localChatTime
    String humour
    TimeZone timeZone
    Locale locale
    boolean enabled
    Company company
    String activationHash
    Integer dailyWorkingHours
    java.sql.Date birthday
    Integer points = 0
    Date joined = new Date()
    Boolean remindToInviteCoworkers = true

    boolean deleted  = false

    List<Assignment> listActiveAssignments() {
        return listAssignmentsByDate(new Date())
    }

    List<Assignment> listAssignmentsByDate(Date _date) {
        return Assignment.executeQuery(
                'select distinct a from Assignment a where a.user = :user and a.startDate <= :date and a.endDate >= :date and (a.deleted is null or a.deleted = false) and a.active = true order by a.id asc',
                [user: this, date: _date.onlyDate]
        )
    }

    boolean registeredEffortsFor(Date _date) {
        Object result = Effort.executeQuery(
                'select count(*) as c from Effort e where e.user = :user and e.date >= :fromDate and e.date < :toDate',
                [user: this, fromDate: _date.onlyDate, toDate: (_date + 1).onlyDate]
        )
        return result[0] > 0
    }

    int getPoints(){
      //FIXME: Use default?
      if (!points)
        return 0;
      return points
    }

    String toString() {
        "$name"
    }

    static hasMany = [efforts: Effort, assignments: Assignment, reminders: Reminder, permissions: Permission, learnings: Learning, moods: UserMood, scores:Score, skills: Skill]

    static belongsTo = Permission

    static mapping = {
        efforts cascade: 'all,delete-orphan'
        assignments cascade: 'all,delete-orphan'
        reminders cascade: 'all,delete-orphan'
        timeZone type: TimeZoneType
        locale type: LocaleType
    }

    static constraints = {
        password(nullable: false, blank: false, size: 32..32)
        name(nullable: false, blank: false, unique: false, size: 2..255)
        account(nullable: false, blank: false, email: true, unique: true, size: 5..255)
        chatTime(nullable: true)
        localChatTime(nullable: true)
        humour(nullable: true)
        company(nullable: false)
        activationHash(nullable: true, size: 32..32)
        dailyWorkingHours(nullable:true, max:24)
        birthday(max:new Date(), nullable: true)
        joined(nullable: true)
        remindToInviteCoworkers(nullable: true)

    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }

}
