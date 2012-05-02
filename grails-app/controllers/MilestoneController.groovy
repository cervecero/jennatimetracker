import org.json.JSONObject
import java.text.DateFormat
import org.springframework.web.servlet.support.RequestContextUtils

class MilestoneController extends BaseController {

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        def user = findLoggedUser()
        def milestoneInstanceTotal = Milestone.createCriteria().get {
            projections {
                countDistinct('id')
            }
            project {
                eq('company', user.company)
            }
        }
        setUpDefaultPagingParams(params)
        def milestoneInstanceList = Milestone.withCriteria() {
            maxResults(params.max.toInteger())
            firstResult(params.offset.toInteger())
            project {
                eq('company', user.company)
            }
            order(params.sort, params.order)
        }
        [milestoneInstanceList: milestoneInstanceList, milestoneInstanceTotal: milestoneInstanceTotal]
    }

    def create = {
        def milestoneInstance = new Milestone()
        milestoneInstance.properties = params
        return [milestoneInstance: milestoneInstance]
    }

    def save = {
        def milestoneInstance = new Milestone(params)
        if (!milestoneInstance.hasErrors() && milestoneInstance.save()) {
            flash.message = "milestone.created"
            flash.args = [milestoneInstance.id]
            flash.defaultMessage = "Milestone ${milestoneInstance.id} created"
            redirect(action: "show", id: milestoneInstance.id)
        }
        else {
            render(view: "create", model: [milestoneInstance: milestoneInstance])
        }
    }

    def ajaxSave = {
        JSONObject jsonResponse = params.id ? update(request, params) : create(request, params)
        render jsonResponse.toString()
    }

    private JSONObject create(request, params) {
        JSONObject jsonResponse
        Milestone milestoneInstance = new Milestone(params)
        //milestoneInstance.company = findLoggedUser().company
        if (!milestoneInstance.hasErrors() && milestoneInstance.save()) {
            jsonResponse = buildJsonOkResponse(request, buildMessageSourceResolvable('confirm'), buildMessageSourceResolvable('milestone.created', [milestoneInstance.name] as Object[]))
        } else {
            jsonResponse = buildJsonErrorResponse(request, milestoneInstance.errors)
        }
        return jsonResponse
    }

    private JSONObject update(request, params) {
        JSONObject jsonResponse
        def milestoneInstance = Milestone.get(params.id)
        if (milestoneInstance && milestoneInstance.project.company.id == findLoggedUser().company.id) {
            def version = params.version.toLong()
            if (milestoneInstance.version > version) {
                milestoneInstance.errors.rejectValue("version", "milestone.optimistic.locking.failure")
                jsonResponse = buildJsonErrorResponse(request, milestoneInstance.errors)
            } else {
                milestoneInstance.properties = params
                if (!milestoneInstance.hasErrors() && milestoneInstance.save()) {
                    jsonResponse = buildJsonOkResponse(request, buildMessageSourceResolvable('confirm'), buildMessageSourceResolvable('milestone.updated', [milestoneInstance.name] as Object[]))
                } else {
                    jsonResponse = buildJsonErrorResponse(request, milestoneInstance.errors)
                }
            }
        } else {
            jsonResponse = buildJsonErrorResponse(request, buildMessageSourceResolvable('milestone.not.found'))
        }
        return jsonResponse
    }

    def show = {
        def milestoneInstance = Milestone.get(params.id)
        if (!milestoneInstance) {
            flash.message = "milestone.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Milestone not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [milestoneInstance: milestoneInstance]
        }
    }

    def edit = {
        def milestoneInstance = Milestone.get(params.id)
        if (!milestoneInstance) {
            flash.message = "milestone.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Milestone not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [milestoneInstance: milestoneInstance]
        }
    }

    def update = {
        def milestoneInstance = Milestone.get(params.id)
        if (milestoneInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (milestoneInstance.version > version) {

                    milestoneInstance.errors.rejectValue("version", "milestone.optimistic.locking.failure", "Another user has updated this Milestone while you were editing")
                    render(view: "edit", model: [milestoneInstance: milestoneInstance])
                    return
                }
            }
            milestoneInstance.properties = params
            if (!milestoneInstance.hasErrors() && milestoneInstance.save()) {
                flash.message = "milestone.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Milestone ${params.id} updated"
                redirect(action: "show", id: milestoneInstance.id)
            }
            else {
                render(view: "edit", model: [milestoneInstance: milestoneInstance])
            }
        }
        else {
            flash.message = "milestone.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Milestone not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def milestoneInstance = Milestone.get(params.id)
        if (milestoneInstance) {
            try {
                milestoneInstance.delete()
                flash.message = "milestone.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Milestone ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "milestone.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Milestone ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "milestone.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Milestone not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def ajaxEdit = {
        def milestoneInstance = Milestone.get(params.id)
        if (!milestoneInstance || milestoneInstance.project.company != findLoggedUser().company) {
            flash.message = "project.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Project not found with id ${params.id}"
            redirect(action: "list")
        } else {
            JSONObject jsonResponse = new JSONObject()
            jsonResponse.put('ok', true)
            jsonResponse.put('id', milestoneInstance.id)
            jsonResponse.put('version', milestoneInstance.version)
            jsonResponse.put('name', milestoneInstance.name)
            jsonResponse.put('description', milestoneInstance.description)
            jsonResponse.put('dueDate', formatDate(request, milestoneInstance.dueDate))
            render jsonResponse.toString()
        }
    }
}
