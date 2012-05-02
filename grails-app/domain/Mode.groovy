class Mode {

    String name
    boolean deleted = false

    static hibernateFilters = {
        enabledFilter(condition: 'deleted=0', default: true)
    }

    static constraints = {
    }

    String toString(){
        "$name"
    }

    static Mode getHumanCapital() {
        return Mode.findByName('Human Capital')
    }
}
