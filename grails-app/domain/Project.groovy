class Project {

    String name
    String description
    Date startDate
    Date endDate
    Company company
    Boolean active
    Boolean billable
    Client client
    Account account
    User teamLeader
    Mode mode

    boolean deleted = false

    String toString() {
        name
    }

    static hasMany = [tags: Tag, milestones: Milestone, assignments: Assignment, technologies: Technology]
    //static hasMany = [asignaciones: Asignacion, estimaciones: Estimacion]

    //static transients = ['xxxx']

    static mapping = {
        company cascade: 'save-update'
        milestones sort: 'dueDate'
        //estimaciones cascade: 'all,delete-orphan'
    }

    static belongsTo = Tag

    static constraints = {
        name(nullable: false, blank: false, unique: ['company'], size: 2..50)
        description(nullable: true, size: 0..512)
        startDate(nullable: false)
        endDate(nullable: false, validator: { val, obj ->
            obj.properties['startDate'] <= val ? null : 'default.invalid.validator.message'
        })
        company(nullable: false)
        client(nullable: false)
        account(nullable: false)
        teamLeader(nullable: false)
       // mode(nullable: false)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
