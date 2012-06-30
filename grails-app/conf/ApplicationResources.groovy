modules = {
    'core' {
        resource id: 'css', url: [dir: 'css', file: 'jquery-ui-1.7.3.custom.css']
        resource id: 'css', url: [dir: 'css', file: 'fg.menu.css']
        resource id: 'css', url: [dir: 'css', file: 'jquery.jgrowl.css']
        resource id: 'css', url: [dir: 'css', file: 'style.css']

        resource id: 'js', url: [dir: 'js', file: 'jquery/jquery-1.7.js'], disposition: 'head'
        resource id: 'js', url: [dir: 'js', file: 'jquery-ui/jquery-ui-1.8.16.custom.js'], disposition: 'head'
        resource id: 'js', url: [dir: 'js', file: 'fg.menu.js'], disposition: 'head'
        resource id: 'js', url: [dir: 'js', file: 'jquery.query-2.1.7.js'], disposition: 'head' // FIXME: Remove when paging/sorting parameters are handled correctly
        resource id: 'js', url: [dir: 'js', file: 'jgrowl/jquery.jgrowl.js'], disposition: 'head'
    }

    'calendar' {
        dependsOn 'core'
        resource id: 'css', url: [dir: 'css', file: 'fullcalendar.css']

        resource id: 'js', url: [dir: 'js', file: 'calendar/fullcalendar.js']
        resource id: 'js', url: [dir: 'js', file: 'qtip/jquery.qtip-1.0.0-rc3.js']
    }

    'jquery-form' {
        dependsOn 'core'
        resource id: 'js', url: [dir: 'js', file: 'jquery-ui/jquery.form.js']
    }

    'jquery-ganttview' {
        dependsOn 'core'
        resource id: 'css', url: [dir: 'css', file: 'jquery.ganttView.css']

        resource id: 'js', url: [dir: 'js', file: 'jquery.ganttView.js'], disposition: 'head'
        resource id: 'js', url: [dir: 'js', file: 'date.js'], disposition: 'head'
    }

    'highcharts' {
        dependsOn 'core'
        resource id: 'js', url: 'http://code.highcharts.com/highcharts.js', disposition: 'head'
        resource id: 'js', url: 'http://code.highcharts.com/modules/exporting.js', disposition: 'head'
    }

    'detecttimezone' {
        resource id: 'js', url: [dir: 'js', file: 'detect_timezone.js'], disposition: 'head'
    }
}
