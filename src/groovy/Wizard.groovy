/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 4, 2009
 * Time: 2:08:29 AM
 */
class Wizard {

    List<WizardStep> steps
    int actualStepIndex
    String[] parameters = null

    def execute(Conversation _conversation, Iterable<String> args) {
        Queue<String> values = args ? new LinkedList<String>(args) : new LinkedList<String>()
        boolean lastResult = true
        while (lastResult && actualStepIndex < steps.size() && values.peek()) {
            lastResult = actualStep.execute(_conversation, values.poll())
            if (lastResult) {
                actualStepIndex++
                skipStep(_conversation)
            }
        }
        if (!lastResult) {
            _conversation.responses << Response.build(actualStep.errorMsgCode, parameters)
        }
        if (!finished) {
            _conversation.responses << Response.build(actualStep.msgCode, parameters)
        }
    }

    WizardStep getActualStep() {
        return actualStepIndex < steps.size() ? steps[actualStepIndex] : null
    }

    void skipStep(Conversation _conversation){
        if (actualStep == null)
          return
        else if (actualStep.skipStep(_conversation) && actualStepIndex < steps.size())
            actualStepIndex++
    }

    boolean isFinished() {
        return actualStepIndex == steps.size()
    }

    boolean acceptsList() {
        return !isFinished() && actualStep instanceof ListableWizardStep
    }

    void list(Conversation _conversation) {
        actualStep.list(_conversation)
    }
}

abstract class WizardStep {

    String msgCode
    String errorMsgCode

    abstract boolean execute(Conversation _conversation, String _value)



  /**
    * This method is used in order to skip a Wizard step
    * @returns
   *    TRUE: Step must be skipped.
   *    FALSE: Step must be processed. 
   */

    boolean skipStep(Conversation _conversation){
      return false
    }
}

abstract class ListableWizardStep extends WizardStep {

    void list(Conversation _conversation) {
        List<ListItem> items = getListing(_conversation)
        if (items) {
            _conversation.context.items = items
            items.eachWithIndex { ListItem item, int idx ->
                _conversation.responses << new Response(text: "$idx - $item.value")
            }
        } else {
            _conversation.responses << Response.build('nothingFound')
        }
    }

    abstract List<ListItem> getListing(Conversation _conversation)

    final boolean execute(Conversation _conversation, String _value) {
        if (_conversation.context.items) {
            try {
                int index = _value.toInteger()
                if (index >= 0 && index < _conversation.context.items.size()) {
                    boolean result = acceptItem(_conversation, _conversation.context.items[index])
                    if (result) {
                        _conversation.context.items = null
                    }
                    return result
                } else {
                    _conversation.responses << Response.build('invalidIndex')
                }
            } catch (Exception ex) {
                _conversation.responses << Response.build('invalidIndex')
            }
            return false
        } else {
            return executeNonList(_conversation, _value)
        }
    }

    abstract boolean acceptItem(Conversation _conversation, ListItem _item)

    abstract boolean executeNonList(Conversation _conversation, String _value)
}

class ListItem {

    long id
    String value
}
