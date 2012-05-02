class Tag {

    String name
    String soundex
    TagCategory category
    Company company

    boolean deleted = false

    void setName(String name) {
        this.@name = TaggingUtil.normalize(name)
        soundex = TaggingUtil.calculateSoundex(this.@name)
    }

    String toString() {
        name
    }

    static mapping = {
        efforts cascade:"all"
        projects cascade:"all"
        cache 'read-write'
    }


    static hasMany = [efforts: Effort, projects: Project]

    static belongsTo = [TagCategory, Company]

    static searchable = {
        only = ['name', 'soundex']
    }

    static constraints = {
        name(nullable: false, blank: false, unique: ['company'])
        soundex(nullable: name!=null?false:true, blank: name!=null?false:true)
        category(nullable: false)
        company(nullable: false)
    }

    def beforeDelete = {

        this.efforts.each{
          it.removeFromTags(this)
        }
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }
}