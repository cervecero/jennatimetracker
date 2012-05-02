class Permission {

    static String ROLE_USER = 'ROLE_USER'
    static String ROLE_COMPANY_ADMIN = 'ROLE_COMPANY_ADMIN'
    static String ROLE_PROJECT_LEADER = 'ROLE_PROJECT_LEADER'
    static String ROLE_SYSTEM_ADMIN = 'ROLE_SYSTEM_ADMIN'

    String name
    String description
    boolean deleted  = false

    String toString() {
        name
    }

    static hasMany = [users: User]

    static constraints = {
        name(nullable: false, blank: false, unique: true, size: 2..255)
        description(nullable: true, size: 0..512)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
