class Project {

    String name
    String description
    Date startDate
    Date endDate
    Company company
    Boolean active
    Boolean billable
    User teamLeader
    Mode mode

    boolean deleted = false

    String toString() {
        name
    }

    static hasMany = [tags: Tag, assignments: Assignment, technologies: Technology]

    static mapping = {
        company cascade: 'save-update'
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
        teamLeader(nullable: false)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
