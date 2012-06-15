import java.text.SimpleDateFormat

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 28, 2009
 * Time: 10:47:56 PM
 */
class TimeZoneUtil {

    def static getAvailableTimeZones() {
        TimeZone.availableIDs.sort()
    }

    def static getAvailablePromptTimes() {
        def promptTimes = []
        (0..23).each { hour ->
            4.times { quarter ->
                String momento = "${hour < 10 ? '0' + hour: hour}:${quarter == 0 ? '00' : quarter * 15}"
                promptTimes << momento
            }
        }
        return promptTimes
    }

    def static getTimeZone(tz) {
        if (tz instanceof TimeZone) {
            return tz
        }
        def timeZone = tz ? TimeZone.getTimeZone(tz) : systemTimeZone
        timeZone ?: systemTimeZone
    }

    def static getSystemTimeZone() {
        TimeZone.default
    }

    def static extractHHMM(value) {
        def pattern = /^(\d\d):(\d\d)$/
        def groups = (value =~ pattern)
        def hours = groups[0][1].toInteger()
        def mins = groups[0][2].toInteger()
        return [hours, mins]
    }

    def static toSystemTime(value, tz) {
        if (!value || !tz) {
            return null
        }
        // short circuiting if destination time zone is the system time zone
        if (tz == getSystemTimeZone() || tz == getSystemTimeZone().getID()) {
            return value
        }
        try {
            def hhmm = extractHHMM(value)
            def hours = hhmm[0]
            def mins = hhmm[1]
            def calendar = Calendar.getInstance(getTimeZone(tz))
            calendar.set Calendar.HOUR_OF_DAY, hours
            calendar.set Calendar.MINUTE, mins
            return new SimpleDateFormat('HH:mm').format(calendar.time)
        } catch (IndexOutOfBoundsException ex) {
            // User has no local time chat.
            return null
        }
    }

    def static toSystemDate(date, time, tz) {
        if (!date || !tz) {
            return null
        }
        // short circuiting if destination time zone is the system time zone
        if (tz == getSystemTimeZone() || tz == getSystemTimeZone().getID()) {
            return date
        }
        def pattern = /^(\d\d\d\d)-(\d\d)-(\d\d)$/
        def groups = (date =~ pattern)
        try {
            def year = groups[0][1].toInteger()
            def month = groups[0][2].toInteger()
            def day = groups[0][3].toInteger()
            def hhmm = extractHHMM(time)
            def hours = hhmm[0]
            def mins = hhmm[1]
            def calendar = Calendar.getInstance(getTimeZone(tz))
            calendar.set Calendar.YEAR, year
            calendar.set Calendar.MONTH, month - 1
            calendar.set Calendar.DAY_OF_MONTH, day
            calendar.set Calendar.HOUR_OF_DAY, hours
            calendar.set Calendar.MINUTE, mins
            return new SimpleDateFormat('yyyy-MM-dd').format(calendar.time)
        } catch (IndexOutOfBoundsException ex) {
            return null
        }
    }

    def static fromSystemTime(value, tz) {
        if (!value || !tz) {
            return null
        }
        // short circuiting if the value is already expressed in the system time zone
        if (tz == getSystemTimeZone() || tz == getSystemTimeZone().getID()) {
            return value
        }
        def pattern = /^(\d\d):(\d\d)$/
        def groups = (value =~ pattern)
        def hours = groups[0][1].toInteger()
        def mins = groups[0][2].toInteger()
        def calendar = Calendar.instance
        calendar.set Calendar.HOUR_OF_DAY, hours
        calendar.set Calendar.MINUTE, mins
        def format = new SimpleDateFormat('HH:mm')
        format.setTimeZone getTimeZone(tz)
        format.format(calendar.time)
    }

    /**
     * @param tz TimeZone from which to get the current date and time
     * @return a Date object, representing the current date and time in the tz
     */
    def static getCurrentDateInTimeZone(tz) {
        final Calendar targetCalendar = GregorianCalendar.getInstance(tz)
        Calendar systemCalendar = GregorianCalendar.getInstance()
        systemCalendar.set(targetCalendar.get(Calendar.YEAR), targetCalendar.get(Calendar.MONTH), targetCalendar.get(Calendar.DAY_OF_MONTH),
            targetCalendar.get(Calendar.HOUR), targetCalendar.get(Calendar.MINUTE))
        return systemCalendar.getTime()
    }
}
