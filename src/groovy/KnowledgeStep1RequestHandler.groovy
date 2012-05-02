/**
 * @author Leandro Larroulet
 * Date: Sep 13, 2010
 * Time: 13:05
 */

class KnowledgeStep1RequestHandler extends RequestHandler {


    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.askForKnowledge
      return accepted
    }


    def doHandle(Conversation _conversation, ChatService _chatService) {
        _conversation.context.clear()
        _conversation.context.askForKnowledge2=true
        _conversation.responses << Response.build('KnowledgeStep1RequestHandler')
    }
}