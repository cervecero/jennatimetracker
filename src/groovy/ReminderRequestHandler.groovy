import java.text.SimpleDateFormat

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 1, 2009
 * Time: 1:20:23 AM
 */
class ReminderRequestHandler extends RequestHandler {

    def doHandle(Conversation _conversation, ChatService _chatService) {
        if (_conversation.context.arguments) {
            def matcher = _conversation.context.arguments.trim() =~ /(\d\d?\:\d\d?)(?:\s+)(.+)/
            if (matcher.matches()) {
                def f = new SimpleDateFormat('HH:mm')
                def d = f.parse(matcher[0][1])
                if ((d.minutes % 5) == 0) {
                    def moment = f.format(d)
                    def localMoment = TimeZoneUtil.toSystemTime(moment, _conversation.actualRequest.user.timeZone)
                    def reminder = new Reminder(moment: moment, localMoment: localMoment, what: matcher[0][2], done: false)
                    _conversation.actualRequest.user.addToReminders(reminder)
                    _conversation.responses << Response.build('ReminderRequestHandler.ok', [d])
                } else {
                    _conversation.responses << Response.build('ReminderRequestHandler.badTime')
                }
            } else {
                _conversation.responses << Response.build('ReminderRequestHandler.badFormat')
            }
        } else {
            _conversation.context.clear()
            def reminders = Reminder.findAllByDoneAndUser(false, _conversation.actualRequest.user)
            if (reminders) {
                _conversation.responses << Response.build('ReminderRequestHandler.show', [reminders.join('\n')])
            } else {
                _conversation.responses << Response.build('ReminderRequestHandler.empty')
            }
        }
        _conversation.context.clear()
    }
}
