import org.springframework.web.servlet.support.RequestContextUtils as RCU

import java.text.DateFormat
import java.text.SimpleDateFormat

class JQueryTagLib {

    static namespace = 'jquery'

    def datePicker = {attrs ->
        Locale locale = RCU.getLocale(request)
        def xdefault = attrs['default']
        if (xdefault == null) {
            xdefault = new Date()
        } else if (xdefault.toString() != 'none') {
            if (xdefault instanceof String) {
                xdefault = DateFormat.getInstance().parse(xdefault)
            } else if (!(xdefault instanceof Date)) {
                throwTagError("Tag [datePicker] requires the default date to be a parseable String or a Date")
            }
        } else {
            xdefault = null
        }

        def value = attrs['value']
        if (value.toString() == 'none') {
            value = null
        } else if (!value) {
            value = xdefault
        }
        def name = attrs['name']
        def id = attrs['id'] ? attrs['id'] : name

        def noSelection = attrs['noSelection']
        if (noSelection != null) {
            noSelection = noSelection.entrySet().iterator().next()
        }

        def day
        def month
        def year
        def dfs = new java.text.DateFormatSymbols(locale)

        def c = null
        if (value instanceof Calendar) {
            c = value
        } else if (value != null) {
            c = new GregorianCalendar();
            c.setTime(value)
        }

        if (c) {
            day = c.get(GregorianCalendar.DAY_OF_MONTH)
            month = c.get(GregorianCalendar.MONTH) + 1
            year = c.get(GregorianCalendar.YEAR)
        }

        out << "<input type=\"hidden\" id=\"${id}\" name=\"${name}\" value=\"struct\" />"
        out << "<input type=\"hidden\" id=\"${id}_day\" name=\"${name}_day\" value=\"${day}\" />"
        out << "<input type=\"hidden\" id=\"${id}_month\" name=\"${name}_month\" value=\"${month}\" />"
        out << "<input type=\"hidden\" id=\"${id}_year\" name=\"${name}_year\" value=\"${year}\" />"

        def format = attrs.format ?: DateFormat.getDateInstance(DateFormat.SHORT, locale).pattern
        def jsformat = attrs.jsformat ?: DateFormat.getDateInstance(DateFormat.SHORT, locale).pattern.toLowerCase()
        def dateTxt = c ? new SimpleDateFormat(format).format(c.time) : ''
        out << "<input type=\"text\" id=\"${id}_datePicker\" name=\"${name}_datePicker\" value=\"${dateTxt}\" />"

        out << """
        <script>
            \$(document).ready(function() {
                \$('#${id}_datePicker').datepicker({dateFormat: '$jsformat'});
                \$('#${id}_datePicker').bind('change', function() {
                    var d = \$.datepicker.parseDate('$jsformat', this.value)
                    \$('#${id}_day').attr('value', d.getDate());
                    \$('#${id}_month').attr('value', d.getMonth() + 1);
                    \$('#${id}_year').attr('value', d.getFullYear());
                });
            });
        </script>
        """
    }
}