class Client {

    String name
    String lastName
    String email
    java.sql.Date birthday
    Account account
    boolean deleted = false


    static constraints = {
        name(nullable: false, blank: false, unique: false, size: 2..255)
        email(nullable: false, blank: false, email: true, unique: true, size: 5..255)
        lastName(nullable: false, blank: false, unique: false, size: 2..255)
        account(nullable: true)
    }

    String toString() {
        "$name"
    }

    static hibernateFilters = {
        enabledFilter(condition: 'deleted=0', default: true)
    }
}