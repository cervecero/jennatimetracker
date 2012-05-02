class Technology {

    String name
    boolean deleted = false

    static constraints = {
        name(blank: false, nullable: false)
    }

    String toString(){
        "$name"
    }

    static hibernateFilters = {
        enabledFilter(condition: 'deleted=0', default: true)
    }
}
