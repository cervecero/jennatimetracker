class SkillController extends BaseController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [skillInstanceList: Skill.list(params), skillInstanceTotal: Skill.count()]
    }

    def create = {
        def skillInstance = new Skill()
        skillInstance.properties = params
        def levels = ["SR", "SSR", "JR", "Basic"]
        return [skillInstance: skillInstance, levelList: levels]
    }

    def save = {
        def skillInstance = new Skill(params)
        if (!skillInstance.hasErrors() && skillInstance.save()) {
            flash.message = "skill.created"
            flash.args = [skillInstance.id]
            flash.defaultMessage = "Skill ${skillInstance.id} created"
            redirect(action: "show", id: skillInstance.id)
        }
        else {
            render(view: "create", model: [skillInstance: skillInstance])
        }
    }

    def show = {
        def skillInstance = Skill.get(params.id)
        if (!skillInstance) {
            flash.message = "skill.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Skill not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [skillInstance: skillInstance]
        }
    }

    def edit = {
        def skillInstance = Skill.get(params.id)
        if (!skillInstance) {
            flash.message = "skill.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Skill not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [skillInstance: skillInstance]
        }
    }

    def update = {
        def skillInstance = Skill.get(params.id)
        if (skillInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (skillInstance.version > version) {

                    skillInstance.errors.rejectValue("version", "skill.optimistic.locking.failure", "Another user has updated this Skill while you were editing")
                    render(view: "edit", model: [skillInstance: skillInstance])
                    return
                }
            }
            skillInstance.properties = params
            if (!skillInstance.hasErrors() && skillInstance.save()) {
                flash.message = "skill.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Skill ${params.id} updated"
                redirect(action: "show", id: skillInstance.id)
            }
            else {
                render(view: "edit", model: [skillInstance: skillInstance])
            }
        }
        else {
            flash.message = "skill.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Skill not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def skillInstance = Skill.get(params.id)
        if (skillInstance) {
            try {
                skillInstance.delete()
                flash.message = "skill.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Skill ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "skill.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Skill ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "skill.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Skill not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def addToMe = {
        def theSkill = Skill.findById(params.id)

        if (!theSkill){
            flash.message = "Is not a correct Skill"
            render view: 'list', model: [skillInstanceList: Skill.list(params), skillInstanceTotal: Skill.count()]
            return
        }
        def user = findLoggedUser()
        if (!user) {
            flash.message = "Not user logged"
            render view: 'list', model: [skillInstanceList: Skill.list(params), skillInstanceTotal: Skill.count()]
            return
        }
        user.addToSkills(theSkill)
        flash.message = "Skill added"
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        render view: 'list', model: [skillInstanceList: Skill.list(params), skillInstanceTotal: Skill.count()]
    }
}