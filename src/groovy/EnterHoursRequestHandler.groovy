
class EnterHoursRequestHandler extends RequestHandler {

  /**
   * This Request Handler will accept conversations invoked by this command: '' or if there are assignments in
   * context map.
   * @param _conversation
   * @return
   */

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.assignments || super.accepts(_conversation)
      return accepted
    }

  /**
    * This method will create a Wizard for each assignment in context map.
    * This Wizard will ask for hours worked for each assignment and what had the user been doing.
    *  
    * @param _conversation
    * @param _chatService
    * @return
    */
    def doHandle(Conversation _conversation, ChatService _chatService) {
        User user = _conversation.actualRequest.user
        Wizard wizard = _conversation.context.wizard
        def args
        def assignments
        def newWizard = false

        if (!wizard) {

            // If there is no created WIZARD, create it.
            assignments = user.listActiveAssignments()

            // If there are assignments, put them into a map in context.
            if (assignments) {
                Queue queue = new LinkedList(assignments*.id)
                _conversation.context.assignments = queue

                // Take the first one and put the project name into the arguments parameter used to create proper message.
                Assignment assignment = Assignment.get(queue.peek())
                // _conversation.responses << Response.build('requestTracking.assignment', [assignment.project.name, assignment.role.name])
              
                args = []
                args << assignment.project.name
                args << assignment.role.name
            }
            else {
                _conversation.responses << Response.build('requestTracking.noAssignment')
                _conversation.context.clear()
                return
            }

            _conversation.context.effort = new Effort()

            wizard = new EnterHoursWizard()
            newWizard = true
            wizard.parameters = args
            args = null

            _conversation.context.wizard = wizard
          
        } else if (_conversation.actualRequest.message && !newWizard) {
            args = []
            args << _conversation.actualRequest.message
        } else {
            args = null
        }

        wizard.execute(_conversation, args)

        if (wizard.finished) {

          // If the wizard for the current assignment has finished, then save proper Effort.
          saveEffort(_conversation)

          // And if there are any assigment left to process, then create a new Wizard.
          if (_conversation.context.assignments){
              _conversation.context.effort = new Effort()
              wizard = new EnterHoursWizard()
              newWizard = true
              _conversation.context.wizard = wizard

              Assignment assignment = Assignment.get(_conversation.context.assignments.peek())
                // _conversation.responses << Response.build('requestTracking.assignment', [assignment.project.name, assignment.role.name])

              args = []
              args << assignment.project.name
              args << assignment.role.name

              wizard.parameters = args
              args = null
              wizard.execute(_conversation, args)
          } else {
            _conversation.context.clear()
            _conversation.responses << Response.build('EnterHoursRequestHandler.ok')

            // Iniciamos el siguiente handler :)
            _conversation.context.askForKnowledge=true
            KnowledgeStep1RequestHandler handler = new KnowledgeStep1RequestHandler()
            handler.handle(_conversation, _chatService)
          }
          return
        }
    }

    def saveEffort(Conversation _conversation) {
        Effort effort = _conversation.context.effort
        effort.date = TimeZoneUtil.getCurrentDateInTimeZone(_conversation.actualRequest.user.timeZone)
        effort.assignment= new Assignment()
        effort.assignment= Assignment.get(_conversation.context.assignments.peek())
        effort.user= _conversation.actualRequest.user
      
        effort.validate()
        if (effort.hasErrors()) {
            effort.errors.allErrors.each { error ->
                _conversation.responses << new Response(message: error)
            }
        } else {
            saveEffortMessage(_conversation)
        }

    }

    def saveEffortMessage(Conversation _conversation) {
        Effort effortToSave = _conversation.context.effort

          if (effortToSave.timeSpent >= 0) {
            def tags = new HashSet()
            if (_conversation.context.assignments) {
                Assignment assignment = Assignment.get(_conversation.context.assignments.poll())
                tags.addAll(assignment.project.tags)
                if (!_conversation.context.assignments) {
                    _conversation.context.assignments = null
                }
            }

            /**
             *  We won't update existing efforts. If user keep entering efforts for a given date. We won't update it. 
             */
            Effort effort = new Effort()
            effort.timeSpent = effortToSave.timeSpent
            effort.comment = effortToSave.comment
            effort.date = effortToSave.date
            effort.assignment = effortToSave.assignment

            tags.each { Tag tag ->
              tag.addToEfforts(effort)
            }

            // Now, we save the effort, associated to this user. (This goes to efforts table).
            User user = _conversation.actualRequest.user
            user.addToEfforts(effort)
            
        } else if (_conversation.context.assignments) {
            _conversation.context.assignments.poll()
      }
    }
}

class EnterHoursWizard extends Wizard {

    EnterHoursWizard() {
        steps = [SetEnterHoursName.INSTANCE, SetEnterHoursComment.INSTANCE]
    }
}

class SetEnterHoursName extends WizardStep {

    static final INSTANCE = new SetEnterHoursName()

    SetEnterHoursName() {
        msgCode = 'EnterHoursRequestHandler.askAssignment'
        errorMsgCode = 'EnterHoursRequestHandler.askAssignmentError'
    }

    boolean execute(Conversation conversation, String value) {
        Effort effort = conversation.context.effort

        Double doubleValue
        try {
          doubleValue = new Double(value)
          if (doubleValue > 24 || doubleValue < 0)
            return false
        } catch (NumberFormatException nfe){
          return false
        }

        effort.timeSpent = doubleValue
        return true
    }

    boolean skipStep(Conversation _conversation) {
      return false;

    }
}

class SetEnterHoursComment extends WizardStep {

    static final INSTANCE = new SetEnterHoursComment()

    SetEnterHoursComment() {
        msgCode = 'EnterHoursRequestHandler.askAssignmentComment'
        errorMsgCode = msgCode
    }


    boolean execute(Conversation conversation, String value) {
        Effort effort = conversation.context.effort
        effort.comment = value
        return true
    }

  boolean skipStep(Conversation _conversation) {
    Effort effort = _conversation.context.effort

    if (effort == null || effort.timeSpent ==0 )
      return true;

    return false;
  }
}