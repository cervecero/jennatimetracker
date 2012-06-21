import grails.converters.JSON

class DashboardController extends BaseController {

    def beforeInterceptor = [action: this.&auth]

    def companyService
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
        def activeProjects = companyService.countActiveProjects(company, startDate, endDate)
        def timeSpent = companyService.sumTimeSpent(company, startDate, endDate)
        def timeSpentByProject = companyService.sumTimeSpentByProject(company, startDate, endDate)
        def newUsers = companyService.listNewUsers(company, startDate, endDate)
        def newProjects = companyService.listNewProjects(company, startDate, endDate)
        def weekMood = companyService.avgMood(company, startDate, endDate)
        def mood = companyService.avgMood(company)
        def newLearnings = companyService.listNewLearnings(company, startDate, endDate)
        def newAssignments = companyService.listNewAssignments(company, startDate, endDate)
        def nextWeekStart = endDate
        def nextWeekEnd = nextWeekStart + 7
        def anniversaries = companyService.listAnniversaries(company, nextWeekStart, nextWeekEnd)
        def birthdays = companyService.listBirthdays(company, nextWeekStart, nextWeekEnd)
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
        def project = Project.get(params.projectId)
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
