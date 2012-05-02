class TechnologyController extends BaseController{

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
        [technologyInstanceList: Technology.list(params), technologyInstanceTotal: Technology.count()]
    }

    def create = {
        def technologyInstance = new Technology()
        technologyInstance.properties = params
        return [technologyInstance: technologyInstance]
    }

    def save = {
        def technologyInstance = new Technology(params)
        if (!technologyInstance.hasErrors() && technologyInstance.save()) {
            flash.message = "technology.created"
            flash.args = [technologyInstance.id]
            flash.defaultMessage = "Technology ${technologyInstance.id} created"
            redirect(action: "show", id: technologyInstance.id)
        }
        else {
            render(view: "create", model: [technologyInstance: technologyInstance])
        }
    }

    def show = {
        def technologyInstance = Technology.get(params.id)
        if (!technologyInstance) {
            flash.message = "technology.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Technology not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [technologyInstance: technologyInstance]
        }
    }

    def edit = {
        def technologyInstance = Technology.get(params.id)
        if (!technologyInstance) {
            flash.message = "technology.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Technology not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [technologyInstance: technologyInstance]
        }
    }

    def update = {
        def technologyInstance = Technology.get(params.id)
        if (technologyInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (technologyInstance.version > version) {

                    technologyInstance.errors.rejectValue("version", "technology.optimistic.locking.failure", "Another user has updated this Technology while you were editing")
                    render(view: "edit", model: [technologyInstance: technologyInstance])
                    return
                }
            }
            technologyInstance.properties = params
            if (!technologyInstance.hasErrors() && technologyInstance.save()) {
                flash.message = "technology.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Technology ${params.id} updated"
                redirect(action: "show", id: technologyInstance.id)
            }
            else {
                render(view: "edit", model: [technologyInstance: technologyInstance])
            }
        }
        else {
            flash.message = "technology.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Technology not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def technologyInstance = Technology.get(params.id)
        if (technologyInstance) {
            try {
                def listSkills = Skill.findAllByTechnology(technologyInstance)
                listSkills?.each {
                    it.delete()
                }
                technologyInstance.delete()
                flash.message = "technology.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Technology ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "technology.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Technology ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "technology.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Technology not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
