class Skill {

    Technology technology
    String level
    boolean deleted = false

    static constraints = {
        technology(nullable: false)
        level(blank: false, nullable: false)
    }

    String toString() {
        "$technology $level"
    }

    static hibernateFilters = {
        enabledSkillFilter(condition: 'deleted=0', default: true)
    }
}