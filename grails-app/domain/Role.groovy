class Role {

    String name
    String description
    Company company

    boolean deleted = false

    String toString() {
        name
    }

    static hasMany = [assignments: Assignment]

    static mapping = {
      assignments cascade:"all"
    }

    static belongsTo = Company

    static constraints = {
        name(nullable: false, blank: false, unique: ['company'], size: 2..255)
        description(nullable: true, size: 0..512)
        company(nullable: false)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
