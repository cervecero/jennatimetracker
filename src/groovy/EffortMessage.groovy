/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 30, 2009
 * Time: 1:47:19 AM
 */
class EffortMessage {

    def timeSpent
    def tags
    def comment
    def problematicTagIndex
    def suggestion

    def isProblematic() {
        problematicTagIndex
    }

    def acceptSuggestion() {
        tags[problematicTagIndex] = suggestion
    }
}
