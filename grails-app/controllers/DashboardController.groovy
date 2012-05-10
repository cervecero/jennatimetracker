import grails.converters.JSON

class DashboardController extends BaseController {

    def beforeInterceptor = [action: this.&auth]

    def dashboardService
    def projectFollowUpService
    def grailsApplication

    def auth() {
        try {
            findLoggedUser()
            return true
        } catch (Exception e) {
            redirect(controller: 'login', action: 'auth')
            return false
        }
    }

    def index = {
        def today = new Date().onlyDate
        [startDateValue: today - 6, endDateValue: today + 1]
    }

    def dashboard = {
        def startDate = new Date(params.dateStart)
        def endDate = new Date(params.dateEnd)
        def company = findLoggedCompany()
        def activeProjects = dashboardService.countActiveProjects(company, startDate, endDate)
        def timeSpent = dashboardService.sumTimeSpent(company, startDate, endDate)
        def timeSpentByProject = dashboardService.sumTimeSpentByProject(company, startDate, endDate)
        def newUsers = dashboardService.listNewUsers(company, startDate, endDate)
        def newProjects = dashboardService.listNewProjects(company, startDate, endDate)
        def weekMood = dashboardService.avgMood(company, startDate, endDate)
        def mood = dashboardService.avgMood(company)
        def newLearnings = dashboardService.listNewLearnings(company, startDate, endDate)
        def newAssignments = dashboardService.listNewAssignments(company, startDate, endDate)
        def nextWeekStart = endDate
        def nextWeekEnd = nextWeekStart + 7
        def anniversaries = dashboardService.listAnniversaries(company, nextWeekStart, nextWeekEnd)
        def birthdays = dashboardService.listBirthdays(company, nextWeekStart, nextWeekEnd)
        return [activeProjects: activeProjects, timeSpent: timeSpent, timeSpentByProject: timeSpentByProject, newUsers: newUsers, newProjects: newProjects,
                weekMood: weekMood, mood: mood, newLearnings: newLearnings, newAssignments: newAssignments, anniversaries: anniversaries, birthdays: birthdays]
    }

    def ajaxProjectName = {
        def term = params.term
        def projects = Project.findAllByCompanyAndNameLike(findLoggedCompany(), "%$term%")
        def data = projects.collect { [id: it.id, value: it.name, label: it.name] }
        render(data as JSON)
    }

    def project = {
        def today = new Date().onlyDate
        def projects = projectFollowUpService.listProjects(findLoggedCompany())
        [startDateValue: today - 6, endDateValue: today + 1, projects: projects]
    }

    def projectFollowUp = {
        def startDate = new Date(params.dateStart)
        def endDate = new Date(params.dateEnd)
        def company = findLoggedCompany()
        def project = Project.get(params.project)
		if (!project) {
			flash.message = "default.select.mandatory"
			flash.args = ['project']
			redirect(action: "project")
			return
		}
        if (project.company.id != company.id) {
            return [:]
        }
        def efforts = projectFollowUpService.listEfforts(project, startDate, endDate)
        def effortsGrouped = projectFollowUpService.listEffortsGrouped(project, startDate, endDate)
        def avgMood = projectFollowUpService.getAvgMood(project, startDate, endDate)
        if (params?.format && params.format != 'html') {
            exportProjectFollowUp(params, request, response, project, efforts)
        } else {
            return [project: project, efforts: efforts, effortsGrouped: effortsGrouped, avgMood: avgMood]
        }
    }

    private void exportProjectFollowUp(params, request, response, project, efforts) {
        response.contentType = grailsApplication.config.grails.mime.types[params.format]
        response.setHeader('Content-disposition', "attachment; filename=${project.name}.${params.extension}")
        projectFollowUpService.exportProjectFollowUp(params.format, getLocale(request), response.outputStream, project.name, efforts)
    }
}
