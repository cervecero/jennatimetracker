import groovy.sql.Sql
import java.text.SimpleDateFormat

class DatabaseService {

    boolean transactional = true

    javax.sql.DataSource dataSource // here the dataSource will be injected by Spring

    def saveRelationShip(Long projectId, Long tagId) {
        def sql = new Sql(dataSource)

        def query = "insert into project_tag (project_tags_id, tag_id) values ( ?, ? ) "
        sql.execute(query, [projectId, tagId])

    }

    def getReport = {String startDate, String endDate, String projectId, String roleId, String userId, Company company ->
        def sql = new Sql(dataSource)
        def query = "select  pro.name as project, " +
                " ef.id as effortId, " +
                " ef.date as date, " +
                " ef.time_spent as timeSpent, " +
                " us.name as user, " +
                " ro.name as role, " +
                " ass.start_date as assignmentStartDate, " +
                " ass.end_date as assignmentEndDate, " +
                " ef.comment as comment" +
                " from    project_guide.effort ef, " +
                " project_guide.user us, " +
                " project_guide.role ro, " +
                " project_guide.assignment ass, " +
                " project_guide.project pro " +
                " where   ef.user_id = us.id " +
                " and     us.id = ass.user_id " +
                " and     ass.id = ef.assignment_id " +
                " and     ro.id = ass.role_id " +
                " and     ass.project_id = pro.id " +
                " and     ef.assignment_id is not null " +
                " and     ef.time_spent > 0 " +
                " and     ef.deleted = 0"

        if (startDate)
            query += " and     ef.date > '${startDate}' "

        if (endDate)
            query += " and     ef.date < '${endDate}' "

        if (userId)
            query += " and     us.id = ${userId} "

        if (roleId)
            query += " and     ro.id = ${roleId} "

        if (projectId)
            query += " and     pro.id = ${projectId} "

        query += " and     us.company_id = ${company.id}"

        query += " order by pro.name asc, role asc, user asc, ef.date desc "

        def rows = sql.rows(query)
        return rows
    }

    def getMoodReport = {Long userId ->

        def sql = new Sql(dataSource)
        def query = '''select um.date as date,
                um.value as value
                from   user_mood um
                where um.user_id = ?
                and    week(um.date) = week(CURRENT_DATE)
                and year(um.date)= year(CURRENT_DATE)
                order by date'''
        def rows = sql.rows(query, [userId])
        return rows
    }

    def getWeeWorkReport = {Long userId ->

        def sql = new Sql(dataSource)
        def query = '''select distinct e.date as date,
                e.time_spent as effort,
                p.name as project,
                e.comment as comment
                from effort e, assignment a, user u, project p
                where a.user_id = ?
                and    e.assignment_id = a.id
                and    a.project_id = p.id
                and    e.time_spent > 0
                and    e.deleted = 0
                and    week(e.date) = week(CURRENT_DATE)
                and    month(e.date) = month(CURRENT_DATE)
                and    year(e.date) = year(CURRENT_DATE)
                order   by date'''
        def rows = sql.rows(query, [userId])
        return rows
    }

    def getKnowledge = {Long userId ->

        def sql = new Sql(dataSource)
        def query = '''select  l.date as date, l.description as knowledge
                    from learning l
                    where l.user_id = ?
                    and week(l.date) = week(CURRENT_DATE) and year(l.date)=year(CURRENT_DATE)'''
        def rows = sql.rows(query, [userId])
        return rows
    }

    def getVotedKnowledge = {Long userId ->

        def sql = new Sql(dataSource)
        def query = '''select distinct l.description gettedKnowledge,
                     u.name votedUserName ,lv.date date     from learning l,
                     learning_vote lv, user u
                     where lv.learning_id = l.id and  u.id=l.user_id
                     and week(lv.date) = week(CURRENT_DATE) and year(lv.date)=year(CURRENT_DATE)  and lv.user_id = ?'''
        def rows = sql.rows(query, [userId])
        return rows
    }


    public List<Integer> findAvailableYears() {

        def sql = new Sql(dataSource)
        List<Integer> years = new ArrayList<Integer>();

        def query = "select distinct YEAR(date) as year from user_mood";
        def rows = sql.rows(query)

        rows.each {
            Integer year = it.getAt("year")
            years.add(year)
        }
        return years
    }

    public Map<Integer, Double> getCompanyMood(user, startDate, endDate) {

        def sql = new Sql(dataSource)

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        String sd = sdf.format(startDate)
        String ed = sdf.format(endDate)

        def query = "select DAY(date) as date, avg(value) as value from user_mood " +
                " where     date >= '${sd}' " +
                " and     date <= '${ed}' " +
                " and     company_id = '${user.company.id}' " +
                " group by date order by date";
        def rows = sql.rows(query)

        return rows.collectEntries {
            [it.getAt("date"), it.getAt("value")]
        }
    }


    public List<ReportRankItem> getReportRanking(String startDate, String endDate, Company company) {

        def List rankingList = new ArrayList()
        def sql = new Sql(dataSource)
        def query = " select us.name, sum(sc.points) score from score sc, user us " +
                " where sc.company_id = ${company.id} " +
                " and sc.user_id = us.id " +
                " and sc.company_id = us.company_id " +
                " and sc.date <= '${endDate}" + " 23:59:59'" +
                " and sc.date >= '${startDate}' " +
                " group by sc.user_id " +
                " order by score desc ";

        def rows = sql.rows(query)

        rows.each {
            ReportRankItem ri = new ReportRankItem()
            ri.user = it.getAt("name")
            ri.points = it.getAt("score")
            rankingList.add(ri)
        }

        return rankingList
    }


    public getReportRankingTotalScore(String startDate, String endDate, Company company) {
        def rankingTotal = 0
        def sql = new Sql(dataSource)
        def query = " select sum(points) score from score sc " +
                " where sc.company_id = ${company.id} " +
                " and date <= '${endDate}' " +
                " and date >= '${startDate}' ";

        def rows = sql.rows(query)

        rows.each {
            rankingTotal = it.getAt("score")
        }

        return rankingTotal
    }

    public Integer getReportUserHistoricRanking(User user) {

        def sql = new Sql(dataSource)
        def query = " select id, name, company_id from ( " +
                " select us.id id, us.name name, us.company_id company_id, sum(sc.points) score from user us " +
                " left join score sc on us.id = sc.user_id " +
                " group by us.id " +
                " order by score desc, us.name asc " +
                " ) as subselect " +
                " where company_id = ${user.company.id} ";

        def rows = sql.rows(query)


        Integer ranking = 0;
        Integer count = 0;
        rows.each {
            count++
            if (user.id == it.getAt("id"))
                ranking = count
        }

        return ranking
    }

}