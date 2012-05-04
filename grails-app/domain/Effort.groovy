import groovy.sql.Sql

class Effort {

    Date date
    Double timeSpent
    String comment
    Assignment assignment
    User user

    boolean deleted  = false

    String toString() {
        "${user?.name} did $timeSpent $date for ${tags?.join(', ')}"
    }

    static belongsTo = [User, Assignment, Tag]

    static hasMany = [tags: Tag]

    static constraints = {
        date(nullable: false)
        timeSpent(nullable: false, min: 0d)
        comment(nullable: true, blank: true, size: 0..1000)
        assignment(nullable: true)
    }

    def beforeDelete = {
     def sql = new Sql(dataSource)

      def query="insert into project_tag (project_tags_id, tag_id) values ( ?, ? ) "
      sql.execute(query, [projectId, tagId])
      /*
      this.tags?.each{
        it.removeFromEfforts(this)
      }
      */
    }

    static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }


}
