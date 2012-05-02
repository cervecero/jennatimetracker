import java.text.SimpleDateFormat

class Problem {

    User user
    String description
    Assignment assignment
    Company company
    Date date
    Integer sequence
    Integer timeSpent

    boolean deleted = false

    static belongsTo = Assignment

    //static belongsTo = [User, Project, Assignment]

    static constraints = {
      user(nullable: false)
      company(nullable: false)
      date(nullable: false)
      assignment(nullable: false)
      sequence(nullable: false)
      description(nullable: false, size: 0..512)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}

