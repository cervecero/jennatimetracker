/**
 * @author Leandro Larroulet
 * Date: Sep 13, 2010
 * Time: 13:05
 */

class KnowledgeStep2RequestHandler extends RequestHandler {

    def accepts(Conversation _conversation) {
      boolean accepted = _conversation.context.askForKnowledge2
      return accepted
    }

    def doHandle(Conversation _conversation, ChatService _chatService) {
      _conversation.context.clear()

      if (_conversation.isAffirmative()){
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerDidLearnSomething')
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerAskKnowledge')
        _conversation.context.askForKnowledge3=true
      } else if (_conversation.isNegative()){
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerDidNotLearnAnything')
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerDidNotLearnAnythingPunish')
      } else {
        _conversation.context.askForKnowledge2=true
        _conversation.responses << Response.build('KnowledgeStep2RequestHandlerAskKnowledgeAgain')
      }
    }
}