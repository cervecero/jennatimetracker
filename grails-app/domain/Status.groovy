class Status {

    Date    date
    String  status
    User    user
    String  learned
    String  category

    boolean deleted = false

    /*
    String toString() {
        "${user?.name} is $status on $date and learnt ${learn}"
    }
    */
     static constraints = {
        status(nullable: false)
        date(nullable: false)
        learned(nullable: true)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}
