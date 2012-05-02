class TagCategory {

    static String CATEGORY_CLIENT = 'client'
    static String CATEGORY_PROJECT = 'project'
    static String CATEGORY_TASK = 'task'

    String name

    boolean deleted = false

    String toString() {
        name
    }

    static constraints = {
        name(nullable: false, blank: false, unique: true)
    }

    static mapping = {
        cache 'read-write'
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
