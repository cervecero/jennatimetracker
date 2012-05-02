class UserMood {

    java.sql.Date   date
    String          status
    User            user
    Company         company
    int             value

    boolean deleted = false

    static belongsTo = User

     static constraints = {
        status(nullable: false)
        date(nullable: false)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}