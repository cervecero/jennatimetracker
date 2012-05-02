class AccountController extends BaseController {

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
        [accountInstanceList: Account.list(params), accountInstanceTotal: Account.count()]
    }

    def create = {
        def accountInstance = new Account()
        accountInstance.properties = params
        def countries = [] as SortedSet

        Locale.availableLocales.displayCountry.each {
            if (it) {
                countries << it
            }
        }
        return [accountInstance: accountInstance, countryList: countries]
    }

    def save = {
        def accountInstance = new Account(params)
        if (!accountInstance.hasErrors() && accountInstance.save()) {
            flash.message = "account.created"
            flash.args = [accountInstance.id]
            flash.defaultMessage = "Account ${accountInstance.id} created"
            redirect(action: "show", id: accountInstance.id)
        }
        else {
            render(view: "create", model: [accountInstance: accountInstance])
        }
    }

    def show = {
        def accountInstance = Account.get(params.id)
        if (!accountInstance) {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [accountInstance: accountInstance]
        }
    }

    def edit = {
        def accountInstance = Account.get(params.id)
        if (!accountInstance) {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [accountInstance: accountInstance]
        }
    }

    def update = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (accountInstance.version > version) {

                    accountInstance.errors.rejectValue("version", "account.optimistic.locking.failure", "Another user has updated this Account while you were editing")
                    render(view: "edit", model: [accountInstance: accountInstance])
                    return
                }
            }
            accountInstance.properties = params
            if (!accountInstance.hasErrors() && accountInstance.save()) {
                flash.message = "account.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Account ${params.id} updated"
                redirect(action: "show", id: accountInstance.id)
            }
            else {
                render(view: "edit", model: [accountInstance: accountInstance])
            }
        }
        else {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            try {
                accountInstance.delete()
                flash.message = "account.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Account ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "account.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Account ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
