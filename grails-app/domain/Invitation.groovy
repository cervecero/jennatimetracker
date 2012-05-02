class Invitation {

    User inviter
    String invitee
    String code
    Date invited
    Date accepted

    boolean deleted  = false

    static constraints = {
        inviter(nullable: false)
        invitee(nullable: false, blank: false, email: true, size: 5..255)
        code(nullable: false, blank: false, size: 5..255)
        invited(nullable: false)
        accepted(nullable: true)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
