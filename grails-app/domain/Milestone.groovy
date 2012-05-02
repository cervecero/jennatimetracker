class Milestone {

    String name
    String description
    Date dueDate

    boolean deleted = false

    String toString() {
        "Milestone $name for ${project?.name} is due on $dueDate"
    }

    static belongsTo = [project: Project]

    static constraints = {
        name(nullable: false, blank: false, size: 1..255)
        description(nullable: true, blank: false, size: 1..255)
        dueDate(nullable: false)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
