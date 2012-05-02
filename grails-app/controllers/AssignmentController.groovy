import org.json.JSONObject

class AssignmentController extends BaseController {

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


    def list = { AssignmentFilterCommand cmd ->
        def query = 'select a from Assignment a, Project pro '
        query += 'where a.project.id = pro.id and a.deleted = 0 and pro.deleted =0'
        query += 'and pro.company = :company'
        def queryTotal = 'select count(a) from Assignment a, Project pro '
        queryTotal += 'where a.project.id = pro.id and a.deleted = 0 and pro.deleted =0'
        queryTotal += 'and pro.company = :company'
        Map qparams = [
                company: findLoggedUser().company,
                max: Math.min(params.max ? params.max.toInteger() : 10, 100),
                offset: Double.valueOf(params.offset ? params.offset : 0)
        ]
        Map qparamsTotal = [
                company: findLoggedUser().company,
        ]
        if (cmd.project) {
            query += ' and pro.id = :project'
            queryTotal += ' and pro.id = :project'
            qparams['project'] = cmd.project
            qparamsTotal['project'] = cmd.project
        }
        if (cmd.user) {
            query += ' and a.user.id = :user'
            queryTotal += ' and a.user.id = :user'
            qparams['user'] = cmd.user
            qparamsTotal['user'] = cmd.user
        }
        if (cmd.startDate) {
            query += ' and a.startDate >= :startDate'
            queryTotal += ' and a.startDate >= :startDate'
            qparams['startDate'] = cmd.startDate
            qparamsTotal['startDate'] = cmd.startDate
        }
        if (cmd.endDate) {
            query += ' and a.endDate <= :endDate'
            queryTotal += ' and a.endDate <= :endDate'
            qparams['endDate'] = cmd.endDate
            qparamsTotal['endDate'] = cmd.endDate
        }
        query += ' order by a.project.name asc, a.role.name asc, a.user.name asc'
        List assignmentList = Assignment.executeQuery(query, qparams)
        long assignmentListTotal = Assignment.executeQuery(queryTotal, qparamsTotal)[0]
        def projectList = Project.findAllByCompany(findLoggedUser().company, [sort: "name", order: "asc"])
        def userList = User.findAllByCompany(findLoggedUser().company, [sort: "name", order: "asc"])
        [assignmentInstanceList: assignmentList, assignmentInstanceTotal: assignmentListTotal, projectList: projectList, userList: userList, project: cmd.project, user: cmd.user, startDate: cmd.startDate, endDate: cmd.endDate]
    }

    def create = {
        def assignmentInstance = new Assignment()
        assignmentInstance.properties = params
        def roleList = Role.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])
        def userList = User.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])
        def projectList = Project.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])

        return [assignmentInstance: assignmentInstance, roleList: roleList, userList: userList, projectList: projectList]
    }

    def save = {
        def assignmentInstance = new Assignment(params)
        assignmentInstance.active = params.active != null
        if (!assignmentInstance.hasErrors() && assignmentInstance.save()) {
            flash.message = "assignment.created"
            flash.args = [assignmentInstance.id]
            flash.defaultMessage = "Assignment ${assignmentInstance.id} created"
            redirect(action: "show", id: assignmentInstance.id)
        }
        else {
            render(view: "create", model: [assignmentInstance: assignmentInstance])
        }
    }

    def ajaxSave = {
        JSONObject jsonResponse = params.id ? update(request, params) : create(request, params)
        render jsonResponse.toString()
    }

    private JSONObject create(request, params) {
        JSONObject jsonResponse
        Assignment assignmentInstance = new Assignment(params)
        if (!assignmentInstance.hasErrors() && assignmentInstance.save()) {
            jsonResponse = buildJsonOkResponse(request, buildMessageSourceResolvable('confirm'), buildMessageSourceResolvable('assignment.created'))
        } else {
            jsonResponse = buildJsonErrorResponse(request, assignmentInstance.errors)
        }
        return jsonResponse
    }

    private JSONObject update(request, params) {
        JSONObject jsonResponse
        Assignment assignment = Assignment.get(params.id)
        if (assignment && assignment.project.company.id == findLoggedUser().company.id) {
            def version = params.version.toLong()
            if (assignment.version > version) {
                assignment.errors.rejectValue('version', 'assignment.optimistic.locking.failure')
                jsonResponse = buildJsonErrorResponse(request, assignment.errors)
            } else {
                assignment.properties = params
                assignment.active = params.active != null
                if (!assignment.hasErrors() && assignment.save()) {
                    jsonResponse = buildJsonOkResponse(request, buildMessageSourceResolvable('confirm'), buildMessageSourceResolvable('assignment.updated'))
                } else {
                    jsonResponse = buildJsonErrorResponse(request, assignment.errors)
                }
            }
        } else {
            jsonResponse = buildJsonErrorResponse(request, buildMessageSourceResolvable('assignment.not.found'))
        }
        return jsonResponse
    }

    def show = {
        def assignmentInstance = Assignment.get(params.id)
        if (!assignmentInstance) {
            flash.message = "assignment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Assignment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [assignmentInstance: assignmentInstance]
        }
    }

    def edit = {
        def assignmentInstance = Assignment.get(params.id)
        if (!assignmentInstance) {
            flash.message = "assignment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Assignment not found with id ${params.id}"

            redirect(action: "list")
        }
        else {
          def roleList = Role.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])
          def userList = User.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])
          def projectList = Project.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])

          return [assignmentInstance: assignmentInstance, rolesList: roleList, usersList: userList, projectsList: projectList]
        }
    }

    def update = {
        def assignmentInstance = Assignment.get(params.id)
        if (assignmentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (assignmentInstance.version > version) {

                    assignmentInstance.errors.rejectValue("version", "assignment.optimistic.locking.failure", "Another user has updated this Assignment while you were editing")
                    render(view: "edit", model: [assignmentInstance: assignmentInstance])
                    return
                }
            }
            assignmentInstance.properties = params
            assignmentInstance.active = params.active != null

            if (!assignmentValidation(assignmentInstance)){
              flash.message = "assignment.errorInDates"
              flash.args = [params.id]

                def roleList = Role.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])
                def userList = User.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])
                def projectList = Project.findAllByCompany(findLoggedUser().company, [sort:"name", order:"asc"])

              render(view: "edit", model: [assignmentInstance: assignmentInstance, rolesList: roleList, usersList: userList, projectsList: projectList])
              return
            }

            if (!assignmentInstance.hasErrors() && assignmentInstance.save()) {
                flash.message = "assignment.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Assignment ${params.id} updated"
                redirect(action: "show", id: assignmentInstance.id)
            }
            else {
                render(view: "edit", model: [assignmentInstance: assignmentInstance])
            }
        }
        else {
            flash.message = "assignment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Assignment not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    private Boolean assignmentValidation(Assignment assignment){
      Boolean result = Boolean.FALSE;
      User user = findLoggedUser();

      def efforts = Effort.findAll("from Effort ef where ef.assignment = ? and (ef.date < ? or ef.date > ?) ", [assignment,assignment.startDate, assignment.endDate]);
      //roles = Role.executeQuery("select distinct ro from Role as ro, Assignment as ass where  ass.role = ro and ass.project = ? order by ro.name asc", [Project.get(params.projectId)])
      if (!efforts)
        result = Boolean.TRUE

      return result;

    }

    def delete = {
        def assignmentInstance = Assignment.get(params.id)
        if (assignmentInstance) {
            try {
                assignmentInstance.delete()
                flash.message = "assignment.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Assignment ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "assignment.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Assignment ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "assignment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Assignment not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def ajaxEdit = {
        def assignment = Assignment.get(params.id)
        if (!assignment || assignment.project.company != findLoggedUser().company) {
            flash.message = "assignment.not.found"
            //TODO
            redirect(action: "list")
        } else {
            JSONObject jsonResponse = new JSONObject()
            jsonResponse.put('ok', true)
            jsonResponse.put('id', assignment.id)
            jsonResponse.put('version', assignment.version)
            jsonResponse.put('userId', assignment.user.id)
            jsonResponse.put('roleId', assignment.role.id)
            jsonResponse.put('description', assignment.description)
            jsonResponse.put('startDate', formatDate(request, assignment.startDate))
            jsonResponse.put('endDate', formatDate(request, assignment.endDate))
            jsonResponse.put('active', assignment.active)
            render jsonResponse.toString()
        }
    }
}

class AssignmentFilterCommand {

    Long project
    Long user
    Date startDate
    Date endDate
}
