class Learning {

    Company company
    User user
    Date date
    String description
    Integer points

    boolean deleted = false

    static belongsTo = User

    static searchable = true

    String getDescripcionConLinks() {
        def encodedDescripcion = description.encodeAsHTML()
        def descripcionConLinks = encodedDescripcion.replaceAll(/(?:http|https|ftp)\:\S+/, { "<a href=\'${it}\' target='_blank'>${it}</a>" })
        return descripcionConLinks
    }

    // static hasMany = LearningVotes

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }

    static constraints = {
      description(nullable: true)
      user(nullable:false)
      company(nullable:false)
    }

    static mapping = {
        description type: 'text'
    }

    static transients = ['descripcionConLinks']
}
