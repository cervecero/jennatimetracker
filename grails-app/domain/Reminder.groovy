class Reminder {

    String moment
    String localMoment
    String what
    boolean done

    boolean deleted = false

    String toString() {
        "Remember to '$what' at $moment"
    }

    static belongsTo = [user: User]

    static constraints = {
        moment(nullable: false)
        localMoment(nullable: false)
        what(nullable: false, blank: false, unique: true, size: 1..255)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
