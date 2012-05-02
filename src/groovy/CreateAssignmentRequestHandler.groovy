import java.util.regex.Matcher
import java.text.ParseException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DataAccessException

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 2, 2009
 * Time: 4:51:58 AM
 */
class CreateAssignmentRequestHandler extends RequestHandler {

    def accepts(Conversation _conversation) {
        return _conversation.context.assignment || super.accepts(_conversation)
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        Wizard wizard = _conversation.context.wizard
        def args
        if (!wizard) {
            _conversation.context.assignment = new Assignment()
            wizard = new CreateAssignmentWizard()
            _conversation.context.wizard = wizard
            def arguments = _conversation.context.arguments
            Matcher matcher = arguments =~ /^(\w[\w\d\s]*)(?:\s*)?(\w[\w\d\s]*)?(?:\s*)?(\w[\w\d\s]*)?(?:\s*)?(\d\d\/\d\d\/\d\d\d\d)?(?:\s*)?(\d\d\/\d\d\/\d\d\d\d)?$/
            args = arguments && matcher.matches() ? matcher[0][1..-1] : null
        } else if (_conversation.actualRequest.message) {
            args = []
            args << _conversation.actualRequest.message
        } else {
            args = null
        }
        wizard.execute(_conversation, args)
        if (wizard.finished) {
            saveAssignment(_conversation)
            _conversation.context.clear()
        }
    }

    def saveAssignment(Conversation _conversation) {
        Assignment assignment = _conversation.context.assignment
        assignment.validate()
        if (assignment.hasErrors()) {
            assignment.errors.allErrors.each { error ->
                _conversation.responses << new Response(message: error)
            }
        } else {
            try {
                assignment.save()
                _conversation.responses << Response.build('CreateAssignmentRequestHandler.ok')
            } catch (DataAccessException ex) {
                assignment.discard()
                _conversation.responses << Response.build('CreateAssignmentRequestHandler.error', [ex.message])
            }

        }
    }
}

class CreateAssignmentWizard extends Wizard {

    CreateAssignmentWizard() {
        steps = [SetAssignmentProject.INSTANCE, SetAssignmentUser.INSTANCE, SetAssignmentRole.INSTANCE, SetAssignmentStartDate.INSTANCE, SetAssignmentEndDate.INSTANCE]
    }
}

class SetAssignmentProject extends ListableWizardStep {

    static final INSTANCE = new SetAssignmentProject()

    SetAssignmentProject() {
        msgCode = 'CreateAssignmentRequestHandler.askProject'
        errorMsgCode = 'CreateAssignmentRequestHandler.projectNotFound'
    }

    List<ListItem> getListing(Conversation _conversation) {
        def projects = Project.withCriteria() {
            eq('company', _conversation.actualRequest.user.company)
            order('name', 'asc')
        }
        def items = []
        projects.each { Project project ->
            items << new ListItem(id: project.id, value: project.name)
        }
        return items
    }

    boolean acceptItem(Conversation _conversation, ListItem _item) {
        Project project = Project.get(_item.id)
        Assignment assignment = _conversation.context.assignment
        assignment.project = project
        return true
    }

    boolean executeNonList(Conversation _conversation, String _value) {
        Project project = Project.findByNameAndCompany(_value, _conversation.actualRequest.user.company)
        if (project) {
            Assignment assignment = _conversation.context.assignment
            assignment.project = project
            return true
        } else {
            return false
        }
    }
}

class SetAssignmentUser extends ListableWizardStep {

    static final INSTANCE = new SetAssignmentUser()

    SetAssignmentUser() {
        msgCode = 'CreateAssignmentRequestHandler.askUser'
        errorMsgCode = 'CreateAssignmentRequestHandler.userNotFound'
    }

    List<ListItem> getListing(Conversation _conversation) {
        def users = User.withCriteria() {
            eq('company', _conversation.actualRequest.user.company)
            order('name', 'asc')
        }
        def items = []
        users.each { User user ->
            items << new ListItem(id: user.id, value: user.name)
        }
        return items
    }

    boolean acceptItem(Conversation _conversation, ListItem _item) {
        User user = User.get(_item.id)
        Assignment assignment = _conversation.context.assignment
        assignment.user = user
        return true
    }

    boolean executeNonList(Conversation _conversation, String _value) {
        User user = User.findByAccountAndCompany(_value, _conversation.actualRequest.user.company)
        if (user) {
            Assignment assignment = _conversation.context.assignment
            assignment.user = user
            return true
        } else {
            return false
        }
    }
}

class SetAssignmentRole extends ListableWizardStep {

    static final INSTANCE = new SetAssignmentRole()

    SetAssignmentRole() {
        msgCode = 'CreateAssignmentRequestHandler.askRole'
        errorMsgCode = 'CreateAssignmentRequestHandler.roleNotFound'
    }

    List<ListItem> getListing(Conversation _conversation) {
        def roles = Role.withCriteria() {
            eq('company', _conversation.actualRequest.user.company)
            order('name', 'asc')
        }
        def items = []
        roles.each { Role role ->
            items << new ListItem(id: role.id, value: role.name)
        }
        return items
    }

    boolean acceptItem(Conversation _conversation, ListItem _item) {
        Role role = Role.get(_item.id)
        Assignment assignment = _conversation.context.assignment
        assignment.role = role
        return true
    }

    boolean executeNonList(Conversation _conversation, String _value) {
        Role role = Role.findByNameAndCompany(_value, _conversation.actualRequest.user.company)
        if (role) {
            Assignment assignment = _conversation.context.assignment
            assignment.role = role
            return true
        } else {
            return false
        }
    }
}

class SetAssignmentStartDate extends WizardStep {

    static final INSTANCE = new SetAssignmentStartDate()

    SetAssignmentStartDate() {
        msgCode = 'CreateAssignmentRequestHandler.askStartDate'
        errorMsgCode = 'CreateAssignmentRequestHandler.invalidDate'
    }

    boolean execute(Conversation conversation, String value) {
        Assignment assignment = conversation.context.assignment
        try {
            assignment.startDate = conversation.parseDate(value)
            return true
        } catch (ParseException ex) {
            return false
        }
    }
}

class SetAssignmentEndDate extends WizardStep {

    static final INSTANCE = new SetAssignmentEndDate()

    SetAssignmentEndDate() {
        msgCode = 'CreateAssignmentRequestHandler.askEndDate'
        errorMsgCode = 'CreateAssignmentRequestHandler.invalidDate'
    }

    boolean execute(Conversation conversation, String value) {
        Assignment assignment = conversation.context.assignment
        try {
            assignment.endDate = conversation.parseDate(value)
            return true
        } catch (ParseException ex) {
            return false
        }
    }
}
