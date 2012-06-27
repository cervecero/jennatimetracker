import org.springframework.web.servlet.support.RequestContextUtils

class CalendarTagLib {

    static namespace = 'cal'

    def showMonth = { attrs, body ->
        Date date = attrs.date ?: new Date()
        attrs.remove('date')

        Locale locale = RequestContextUtils.getLocale(request)

        Calendar calStart = Calendar.getInstance(locale)
        calStart.time = date
        calStart.set(Calendar.DAY_OF_MONTH, 1)
        int fdow = calStart.firstDayOfWeek
        while (calStart.get(Calendar.DAY_OF_WEEK) != fdow) {
            calStart.add(Calendar.DAY_OF_MONTH, -1)
        }

        int ldow = fdow == Calendar.SUNDAY ? Calendar.SATURDAY : fdow - 1
        Calendar calEnd = Calendar.getInstance(locale)
        calEnd.time = date
        calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH))
        while (calEnd.get(Calendar.DAY_OF_WEEK) != ldow) {
          calEnd.add(Calendar.DAY_OF_MONTH, 1)
        }

        pageScope.calendarStartDate = calStart
        pageScope.calendarEndDate = calEnd

        out << '<table class="cal">'
        out << '<thead><tr><td/>'
        ((calStart.time)..(calStart.time + 6)).each { d ->
            out << '<th width="14%"><g:formatDate date="${date}" format="EEEE"/></th>'
            out << formatDate(date: d, format: 'EEEE')
            out << '</th>'
        }
        out << '</tr></thead>'
        out << '<tbody>'

        Date today = new Date().clearTime()

        Calendar auxCal = Calendar.getInstance(locale)
        ((calStart.time)..(calEnd.time)).eachWithIndex { d, idx ->
            if (idx % 7 == 0) {
                auxCal.time = d
                out << '<tr><td>'
                out << auxCal.get(Calendar.WEEK_OF_YEAR)
                out << '</td>'
            }
            out << body(date)
            if (idx % 7 == 5) {
                out << '</tr>'
            }
        }
        out << '</tbody>'
        out << '</table>'
    }


}
