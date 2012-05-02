class Dictionary {

    String word
    String type
    String category
    Locale locale

    boolean deleted = false

    static constraints = {
        word(nullable: false, size: 0..32)
        type(nullable: false, size: 0..32)
        category(nullable: false, size: 0..32)
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}