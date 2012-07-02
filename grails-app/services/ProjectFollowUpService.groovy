import reports.ProjectFollowUpRecord
import reports.ProjectFollowUpGroupedRecord
import reports.Formatters
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import de.andreasschmitt.export.ExportService
import org.apache.commons.io.FileUtils

class ProjectFollowUpService {

    boolean transactional = false

    MessageSource messageSource
    ExportService exportService
    EmailerService emailerService

    def listEffortsGrouped(project, minDate, maxDate) {
        def effortsGrouped = User.executeQuery(
                '''select u.name, r.name, sum(e.timeSpent)
from Effort e join e.assignment a join a.role r join e.user u
where a.project = :project and e.deleted = false and a.deleted = false and e.date >= :minDate and e.date < :maxDate
group by u.id, r.id
having sum(e.timeSpent) > 0
order by u.name asc, r.name asc''',
                [project: project, minDate: minDate, maxDate: maxDate])
        def result = []
        effortsGrouped.each { e ->
            def record = new ProjectFollowUpGroupedRecord(userName: e[0], roleName: e[1], timeSpent: e[2])
            result << record
        }
        return result
    }

    def listEfforts(project, minDate, maxDate) {
        def efforts = User.executeQuery(
                '''select e.date, e.timeSpent, e.comment, u.id, u.name, r.name
from Effort e join e.assignment a join a.user u join a.role r
where a.project = :project and e.deleted = false and a.deleted = false and e.date >= :minDate and e.date < :maxDate and e.timeSpent > 0
order by e.date desc, u.name asc''',
                [project: project, minDate: minDate, maxDate: maxDate])
        def moods = UserMood.executeQuery(
                '''select m.date, m.value, u.id
from UserMood m join m.user u
where exists (from Assignment a join a.user v where v = u and a.project = :project and a.deleted =false)
and m.date >= :minDate and m.date < :maxDate and m.deleted = false
''',
                [project: project, minDate: minDate, maxDate: maxDate]
        )
        def avgMoods = UserMood.executeQuery(
                '''select avg(m.value), u.id
from UserMood m join m.user u
where exists (from Assignment a join a.user v where v = u and a.project = :project and a.deleted = false)
and m.deleted = false
group by u.id
''',
                [project: project]
        )
        def result = []
        efforts.each { e ->
            def record = new ProjectFollowUpRecord(date: e[0], userName: e[4], timeSpent: e[1], comment: e[2], roleName: e[5])
            def mood = moods.find { m ->
                m[2] == e[3] && m[0].clearTime() == e[0].clearTime()
            }
            if (mood) {
                record.mood = mood[1]
            }
            def avgMood = avgMoods.find { m ->
                m[1] == e[3]
            }
            if (avgMood) {
                record.avgMood = avgMood[0]
            }
            result << record
        }
        return result
    }

    def getAvgMood(project, minDate, maxDate) {
        def avgMood = UserMood.executeQuery(
                '''select avg(m.value)
from UserMood m
where exists (from Assignment a join a.user u where u = m.user and a.project = :project and a.deleted = false)
and m.date >= :minDate and m.date < :maxDate and m.deleted = false
''',
                [project: project, minDate: minDate, maxDate: maxDate]
        )[0]
        return avgMood
    }

    def exportProjectFollowUp(format, locale, outputStream, projectName, efforts) {
        List fields = ['date', 'userName', 'roleName', 'timeSpent', 'mood', 'avgMood', 'comment']
        Map labels = [
                'date': getMessage(locale, 'effort.date'),
                'userName': getMessage(locale, 'user.name'),
                'roleName': getMessage(locale, 'assignment.role'),
                'timeSpent': getMessage(locale, 'effort.timeSpent'),
                'mood': getMessage(locale, 'report.mood'),
                'avgMood': getMessage(locale, 'report.generalMood'),
                'comment': getMessage(locale, 'effort.comment')]
        Formatters.locale = locale
        Map formatters = [date: Formatters.dateFormatter, timeSpent: Formatters.floatFormatter, mood: Formatters.intFormatter, avgMood: Formatters.floatFormatter]
        Map parameters = [title: projectName, 'column.widths': [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.4]]
        exportService.export(format, outputStream, efforts, fields, labels, formatters, parameters)
    }

    def sendEmailsToTeamLeaders(company) {
        Project.withTransaction {
            def maxDate = new Date().clearTime()
            def minDate = maxDate - 7
            def projects = Project.executeQuery(
                    '''select p
from Project p
where p.company = :company and p.active = true and p.deleted = false
and p.startDate <= :maxDate and p.endDate >= :minDate''',
                    [company: company, minDate: minDate, maxDate: maxDate])
            projects.each { Project p ->
                def efforts = listEfforts(p, minDate, maxDate)
                if (efforts) {
                    def reportFile = null
                    try {
                        reportFile = File.createTempFile(p.name, '.pdf')
                        def outputStream = new FileOutputStream(reportFile)
                        exportProjectFollowUp('pdf', p.teamLeader.locale, outputStream, p.name, efforts)
                        def email = [
                                to: [p.teamLeader.account],
                                subject: getMessage(p.teamLeader.locale, 'email.projectFollowUp.subject', [p.name] as Object[]),
                                from: getMessage(p.teamLeader.locale, 'application.email'),
                                text: getMessage(p.teamLeader.locale, 'email.projectFollowUp.body', [p.name, minDate, maxDate] as Object[]),
                                attachments: [reportFile]
                        ]
                        emailerService.sendEmails([email])
                    } finally {
                        FileUtils.deleteQuietly(reportFile)
                    }
                }
            }
        }
    }

    String getMessage(Locale _locale, String _msgKey, Object[] _args = null) {
        try {
            return messageSource.getMessage(_msgKey, _args, _locale)
        } catch (NoSuchMessageException ex) {
            return "Missing message: $_msgKey"
        }
    }
}
