import java.text.SimpleDateFormat

class Assignment {

    //TODO: add tags to the assignment?
    Project project
    User user
    Role role
    Date startDate
    Date endDate
    String description
    Boolean active

    boolean deleted = false

    String toString() {
        def format = new SimpleDateFormat('dd/MM/yyyy')
        def startDateString = ""
        def endDateString = ""

        if (this.startDate != null)
          startDateString += format.format(this.startDate)
        if (this.endDate != null)
          endDateString += format.format(this.endDate)

        this?.project?.name+" - "+this?.role?.name+" ("+startDateString+" - "+endDateString+")"
    }

    static belongsTo = [User, Role, Project]
    static hasMany = [efforts: Effort]

    static mapping = {
      efforts cascade:"all"
    }

    static constraints = {
        //TODO: add a more complex constraint considering overlapping periods
        //project(unique: ['user', 'role'], nullable: false)
        //user(unique: ['project', 'role'], nullable: false)
        //role(unique: ['project', 'user'], nullable: false)
        startDate(nullable: false)
        endDate(nullable: false, validator: { val, obj ->
            obj.properties['startDate'] <= val ? null : 'default.invalid.validator.message'
        })
        description(nullable: true, size: 0..512)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
