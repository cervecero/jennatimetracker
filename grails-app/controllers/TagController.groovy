class TagController extends BaseController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action:this.&auth]

    def auth() {
         try {
          findLoggedUser()
          return true
         } catch (Exception e){
          redirect(controller:'login', action:'auth')
          return false
         }
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [tagInstanceList: Tag.findAllByCompany(findLoggedUser().company, params), tagInstanceTotal: Tag.countByCompany(findLoggedUser().company)]
    }
  
    def create = {
        def tagInstance = new Tag()
        tagInstance.properties = params
        return [tagInstance: tagInstance]
    }

    def save = {
        def tagInstance = new Tag(params)
        tagInstance.company=findLoggedUser().company;
        if (!tagInstance.hasErrors() && tagInstance.save()) {
            flash.message = "tag.created"
            flash.args = [tagInstance.id]
            flash.defaultMessage = "Tag ${tagInstance.id} created"
            redirect(action: "show", id: tagInstance.id)
        }
        else {
            render(view: "create", model: [tagInstance: tagInstance])
        }
    }

    def show = {
        def tagInstance = Tag.get(params.id)
        if (!tagInstance) {
            flash.message = "tag.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Tag not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [tagInstance: tagInstance]
        }
    }

    def edit = {
        def tagInstance = Tag.get(params.id)
        if (!tagInstance) {
            flash.message = "tag.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Tag not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [tagInstance: tagInstance]
        }
    }

    def update = {
        def tagInstance = Tag.get(params.id)
        if (tagInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (tagInstance.version > version) {
                    
                    tagInstance.errors.rejectValue("version", "tag.optimistic.locking.failure", "Another user has updated this Tag while you were editing")
                    render(view: "edit", model: [tagInstance: tagInstance])
                    return
                }
            }
            tagInstance.properties = params
            if (!tagInstance.hasErrors() && tagInstance.save()) {
                flash.message = "tag.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Tag ${params.id} updated"
                redirect(action: "show", id: tagInstance.id)
            }
            else {
                render(view: "edit", model: [tagInstance: tagInstance])
            }
        }
        else {
            flash.message = "tag.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Tag not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def tagInstance = Tag.get(params.id)
        if (tagInstance) {
            try {
                tagInstance.delete()
                flash.message = "tag.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Tag ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "tag.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Tag ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "tag.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Tag not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
