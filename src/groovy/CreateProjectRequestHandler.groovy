import java.util.regex.Matcher
import java.text.ParseException

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 2, 2009
 * Time: 4:51:58 AM
 */
class CreateProjectRequestHandler extends RequestHandler {

    def accepts(Conversation _conversation) {
        return _conversation.context.project || super.accepts(_conversation)
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        Wizard wizard = _conversation.context.wizard
        def args
        if (!wizard) {
            _conversation.context.project = new Project()
            wizard = new CreateProjectWizard()
            _conversation.context.wizard = wizard
            def arguments = _conversation.context.arguments
            Matcher matcher = arguments =~ /^(\w[\w\d\s]*)(?:\s*)?(\d\d\/\d\d\/\d\d\d\d)?(?:\s*)?(\d\d\/\d\d\/\d\d\d\d)?(?:\s*)?(?:[\w\d,]*)?$/
            args = arguments && matcher.matches() ? matcher[0][1..-1] : null
        } else if (_conversation.actualRequest.message) {
            args = []
            args << _conversation.actualRequest.message
        } else {
            args = null
        }
        wizard.execute(_conversation, args)
        if (wizard.finished) {
            saveProject(_conversation)
            _conversation.context.clear()
        }
    }

    def saveProject(Conversation _conversation) {
        Project project = _conversation.context.project
        project.company = _conversation.actualRequest.user.company
        project.validate()
        if (project.hasErrors()) {
            project.errors.allErrors.each { error ->
                _conversation.responses << new Response(message: error)
            }
        } else {
            project.save()
            _conversation.context.clear()
            _conversation.responses << Response.build('CreateProjectRequestHandler.ok', [project.name])
        }
    }
}

class CreateProjectWizard extends Wizard {

    CreateProjectWizard() {
        steps = [SetProjectName.INSTANCE, SetProjectStartDate.INSTANCE, SetProjectEndDate.INSTANCE]
    }
}

class SetProjectName extends WizardStep {

    static final INSTANCE = new SetProjectName()

    SetProjectName() {
        msgCode = 'CreateProjectRequestHandler.askName'
        errorMsgCode = msgCode
    }


    boolean execute(Conversation conversation, String value) {
        Project project = conversation.context.project
        project.name = value
        return true
    }
}

class SetProjectStartDate extends WizardStep {

    static final INSTANCE = new SetProjectStartDate()

    SetProjectStartDate() {
        msgCode = 'CreateProjectRequestHandler.askStartDate'
        errorMsgCode = 'CreateProjectRequestHandler.invalidDate'
    }


    boolean execute(Conversation conversation, String value) {
        Project project = conversation.context.project
        try {
            project.startDate = conversation.parseDate(value)
            return true
        } catch (ParseException ex) {
            return false
        }
    }
}

class SetProjectEndDate extends WizardStep {

    static final INSTANCE = new SetProjectEndDate()

    SetProjectEndDate() {
        msgCode = 'CreateProjectRequestHandler.askEndDate'
        errorMsgCode = 'CreateProjectRequestHandler.invalidDate'
    }


    boolean execute(Conversation conversation, String value) {
        Project project = conversation.context.project
        try {
            project.endDate = conversation.parseDate(value)
            return true
        } catch (ParseException ex) {
            return false
        }
    }
}
