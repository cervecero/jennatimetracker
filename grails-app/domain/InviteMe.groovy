class InviteMe {

    String name
    String email
    Company company
    Date requested
    Date invited

    boolean deleted = false

    static constraints = {
        name(nullable: false)
        email(nullable: false, blank: false, email: true, unique: true, size: 5..255)
        company(nullable: false)
        requested(nullable: false)
        invited(nullable: true)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}