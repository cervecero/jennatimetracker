class ProfileController extends BaseController {

    def authenticateService
    DatabaseService databaseService

    def index = { redirect(action: "show", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [update: "POST", delete: "POST"]
    def beforeInterceptor = [action: this.&auth]

    def auth() {
        try {
            findLoggedUser()
            return true
        } catch (Exception e) {
            redirect(controller: 'login', action: 'auth')
            return false
        }
    }


    def show = {
        def user = findLoggedUser()
        def userInstance = User.get(user.id)

        def assignments = user.listActiveAssignments()
        def totalUsers = User.countByCompany(user.company)
        def userRanking = databaseService.getReportUserHistoricRanking(user)

        return [userInstance: userInstance, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales(), assignments: assignments, totalUsers: totalUsers, userRanking: userRanking]

    }

    boolean mustChangePassword(String pass, String repass) {

        if (!pass || !repass)
            return Boolean.FALSE

        if (pass.equals(repass))
            return Boolean.TRUE
    }

    def update = {
        def userInstance = User.get(params.id)
        def password = params.newpassword
        def repassword = params.newrepassword

        if (!("".equals(password)) || !("".equals(repassword))) {
            if (mustChangePassword(password, repassword)) {
                userInstance.setPassword(authenticateService.encodePassword(password))
            } else {
                flash.message = "user.password.change.error"
                redirect(action: "show", id: userInstance.id)
                return
            }
        }

        if (userInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userInstance.version > version) {

                    userInstance.errors.rejectValue("version", "user.optimistic.locking.failure", "Please refresh this page and try saving again.")
                    render(view: "show", model: [userInstance: userInstance, assignments: userInstance.listActiveAssignments()])
                    return
                }
            }
            userInstance.properties = params
            userInstance.localChatTime = TimeZoneUtil.toSystemTime(userInstance.chatTime, userInstance.timeZone)
            if (!userInstance.hasErrors() && userInstance.save()) {
                flash.message = "user.updated"
                flash.args = [userInstance.name]
                flash.defaultMessage = "User ${userInstance.name} updated"
                redirect(action: "show", id: userInstance.id)
            }
            else {
                render(view: "show", model: [userInstance: userInstance, assignments: userInstance.listActiveAssignments()])
            }
        }
    }
}