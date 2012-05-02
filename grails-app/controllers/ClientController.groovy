import java.lang.reflect.Array

class ClientController extends BaseController {

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
        [clientInstanceList: Client.list(params), clientInstanceTotal: Client.count()]
    }

    def create = {
        def clientInstance = new Client()
        clientInstance.properties = params
        return [clientInstance: clientInstance]
    }

    def save = {
        def clientInstance = new Client(params)
        if (!clientInstance.hasErrors() && clientInstance.save()) {
            Account account = Account.findById(clientInstance.account.id)
            account.addToClients(clientInstance)
            flash.message = "client.created"
            flash.args = [clientInstance.id]
            flash.defaultMessage = "Client ${clientInstance.id} created"
            redirect(action: "show", id: clientInstance.id)
        }
        else {
            render(view: "create", model: [clientInstance: clientInstance])
        }
    }

    def show = {
        def clientInstance = Client.get(params.id)
        if (!clientInstance) {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [clientInstance: clientInstance]
        }
    }

    def edit = {
        def clientInstance = Client.get(params.id)
        if (!clientInstance) {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [clientInstance: clientInstance]
        }
    }

    def update = {
        def clientInstance = Client.get(params.id)
        if (clientInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (clientInstance.version > version) {

                    clientInstance.errors.rejectValue("version", "client.optimistic.locking.failure", "Another user has updated this Client while you were editing")
                    render(view: "edit", model: [clientInstance: clientInstance])
                    return
                }
            }
            clientInstance.properties = params
            if (!clientInstance.hasErrors() && clientInstance.save()) {
                flash.message = "client.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Client ${params.id} updated"
                redirect(action: "show", id: clientInstance.id)
            }
            else {
                render(view: "edit", model: [clientInstance: clientInstance])
            }
        }
        else {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def clientInstance = Client.get(params.id)
        if (clientInstance) {
            try {
                clientInstance.delete()
                flash.message = "client.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Client ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "client.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Client ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
