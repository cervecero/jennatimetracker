class Account {

    String name
    String country
    String razonSocial
    boolean deleted = false

    static constraints = {
        name(blank: false, nullable: false)
        country(blank: false, nullable: false)
        razonSocial(blank: false, nullable: false)
    }

    static hasMany = [clients: Client]

    static mapping = {
        clients cascade: 'all,delete-orphan'
    }

    String toString() {
        "$name from $country"
    }

    static hibernateFilters = {
        enabledFilter(condition: 'deleted=0', default: true)
    }
}