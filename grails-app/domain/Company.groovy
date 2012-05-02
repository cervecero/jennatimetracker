class Company {

    String name

    boolean deleted  = false

    String toString() {
        name
    }

    static hasMany = [employees: User]

    static mapping = {
        employees cascade:'all,delete-orphan'
    }

    static constraints = {
        name(nullable: false, blank: false, unique: true, size: 2..255)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
