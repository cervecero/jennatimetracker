class DashboardService {

    boolean transactional = true

    def countActiveProjects(company, minDate, maxDate) {
        def result = Project.executeQuery(
                '''select count(p)
from Project p
where p.company = :company and p.active = true and p.deleted = false
and (p.startDate <= :maxDate or p.endDate >= :minDate)''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result[0]
    }

    def sumTimeSpent(company, minDate, maxDate) {
        //TODO: se suman tambien las de proyectos HC?
        def result = Effort.executeQuery(
                '''select sum(e.timeSpent)
from Effort e join e.user u
where u.company = :company and e.deleted = false and e.date >= :minDate and e.date < :maxDate''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result[0]
    }

    def sumTimeSpentByProject(company, minDate, maxDate) {
        def result = Effort.executeQuery(
                '''select a.project, sum(e.timeSpent)
from Effort e join e.user u join e.assignment a
where u.company = :company and e.deleted = false and e.date >= :minDate and e.date < :maxDate
group by a.project
having sum(e.timeSpent) > 0''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result.collect {
            new Expando(project: it[0], timeSpent: it[1])
        }
    }

    def listNewUsers(company, minDate, maxDate) {
        def result = User.executeQuery(
                '''select u
from User u
where u.company = :company and u.deleted = false and u.enabled = true and u.joined >= :minDate and u.joined < :maxDate''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result
    }

    def listNewProjects(company, minDate, maxDate) {
        def result = Project.executeQuery(
                '''select p
from Project p
where p.company = :company and p.deleted = false and p.active = true and p.startDate >= :minDate and p.startDate < :maxDate''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result
    }

    def avgMood(company, minDate, maxDate) {
        def result = UserMood.executeQuery(
                '''select avg(um.value)
from UserMood um
where um.company = :company and um.deleted = false and um.date >= :minDate and um.date < :maxDate''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result[0]
    }

    def avgMood(company) {
        def result = UserMood.executeQuery(
                '''select avg(um.value)
from UserMood um
where um.company = :company and um.deleted = false''',
                [company: company])
        return result[0]
    }

    def listNewLearnings(company, minDate, maxDate) {
        def result = Learning.executeQuery(
                '''select l
from Learning l
where l.company = :company and l.deleted = false and l.date >= :minDate and l.date < :maxDate
order by l.points desc''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result
    }

    def listNewAssignments(company, minDate, maxDate) {
        def result = Assignment.executeQuery(
                '''select a
from Assignment a join a.user u
where u.company = :company and a.active = true and a.startDate >= :minDate and a.startDate < :maxDate''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result
    }

    def listBirthdays(company, minDate, maxDate) {
        def result = User.executeQuery(
                '''select u
from User u
where u.company = :company and u.enabled = true and u.deleted = false
and (
(day(:minDate) + (month(:minDate) - 1) * 31 <= day(:maxDate) + (month(:maxDate) - 1) * 31 and day(u.birthday) + (month(u.birthday) - 1) * 31 between day(:minDate) + (month(:minDate) - 1) * 31 and day(:maxDate) + (month(:maxDate) - 1) * 31)
or
(day(:minDate) + (month(:minDate) - 1) * 31 > day(:maxDate) + (month(:maxDate) - 1) * 31 and not day(u.birthday) + (month(u.birthday) - 1) * 31 between day(:maxDate) + (month(:maxDate) - 1) * 31 and day(:minDate) + (month(:minDate) - 1) * 31)
)''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result
    }

    def listAnniversaries(company, minDate, maxDate) {
        def result = User.executeQuery(
                '''select u
from User u
where u.company = :company and u.enabled = true and u.deleted = false
and (
(day(:minDate) + (month(:minDate) - 1) * 31 <= day(:maxDate) + (month(:maxDate) - 1) * 31 and day(u.joined) + (month(u.joined) - 1) * 31 between day(:minDate) + (month(:minDate) - 1) * 31 and day(:maxDate) + (month(:maxDate) - 1) * 31)
or
(day(:minDate) + (month(:minDate) - 1) * 31 > day(:maxDate) + (month(:maxDate) - 1) * 31 and not day(u.joined) + (month(u.joined) - 1) * 31 between day(:maxDate) + (month(:maxDate) - 1) * 31 and day(:minDate) + (month(:minDate) - 1) * 31)
)''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result
    }

    def listProjectsByUser(user, minDate, maxDate) {
        def result = Project.executeQuery(
                '''select p
from Assignment a join a.project p
where a.user = :user and a.deleted = false and a.active = true and a.startDate < :maxDate and a.endDate >= :minDate
order by p.name''',
                [user: user, minDate: minDate, maxDate: maxDate])
        return result
    }

    def listPartners(user, minDate, maxDate) {
        def result = Project.executeQuery(
                '''select u
from User u
where exists (
    select a from Assignment a where a.deleted = false and a.active = true and a.startDate < :maxDate and a.endDate >= :minDate and a.user = u and a.project in (
        select p from Assignment b join b.project p join p.mode m where b.deleted = false and b.active = true and b.startDate < :maxDate and b.endDate >= :minDate and b.user = :user and m.name != :humanCapital
    )
)
and u != :user
order by u.name''',
                [user: user, minDate: minDate, maxDate: maxDate, humanCapital: 'Human Capital'])
        return result
    }

    def listKnowledge(company, minDate, maxDate) {
        def result = Project.executeQuery(
                '''select u.name, sum(s.points)
from Score s join s.user u
where s.deleted = false and s.company = :company and s.date >= :minDate and s.date < :maxDate
group by u.name
order by 2 desc''',
                [company: company, minDate: minDate, maxDate: maxDate])
        return result.collect() { row ->
            new Expando(user: row[0], points: row[1])
        }
    }
}
