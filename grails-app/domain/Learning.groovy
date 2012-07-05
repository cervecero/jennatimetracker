class Learning {

    Company company
    User user
    Date date
    String description
    Integer points

    boolean deleted = false

    static belongsTo = User

    static searchable = true

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }

    static constraints = {
      description(nullable: true) // FIXME: Description should be mandatory, shouldn't it?
      user(nullable:false)
      company(nullable:false)
    }

    static mapping = {
        description type: 'text'
    }
}
