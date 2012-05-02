/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 31, 2009
 * Time: 12:28:14 AM
 */
class EffortRequestHandler extends BaseEffortRequestHandler {

    def accepts(Conversation _conversation) {
        def matcher = _conversation.actualRequest.message =~ /^(\d+(?:[\:\.]\d{2})?)(?:[\s\,]*)([\w\d][\w\d\.\, ]+)*(?:[\s]*)(?:\((.*)\))?$/
        _conversation.context.matcher = matcher
        return matcher.matches() && (_conversation.context.assignments || matcher[0].size() > 2 && matcher[0][2] != null)
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
        def matcher = _conversation.context.matcher
        _conversation.context.matcher = null
        def message = new EffortMessage()
        message.timeSpent = parseTime(matcher[0][1])
        def tags = matcher[0][2]?.split(',')
        def normalizedTags = []
        def problematicMessage = 0
        tags.eachWithIndex { it, idx ->
            def normalizedTag = TaggingUtil.normalize(it)
            normalizedTags << normalizedTag
            def tag = Tag.findByName(normalizedTag)
            if (!tag) {
                def similarTag = Tag.findBySoundex(TaggingUtil.calculateSoundex(normalizedTag))
                if (similarTag) {
                    message.problematicTagIndex = idx
                    message.suggestion = similarTag.name
                    problematicMessage = 1
                }
            }
        }
        message.tags = normalizedTags
        if (matcher[0].size() >= 4 && matcher[0][3]?.trim()) {
            message.comment = matcher[0][3].trim()
        }
        _conversation.context.effortMessage = message
        if (problematicMessage) {
            _conversation.responses << Response.build('EffortRequestHandler.disambiguation', [message.suggestion])
        } else {
            saveEffortMessage(_conversation)
            if (!_conversation.context.assignments) {
                _conversation.context.clear()
            }
        }
    }

    double parseTime(String _value) {
        String[] parts = _value.split(':')
        return Double.valueOf(parts[0]) + (parts.size() > 1 ? Double.valueOf(parts[1]) / 60 : 0)
    }
}
