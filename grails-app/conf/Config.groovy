// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.config.locations = [
                            CustomDataSource, // Environment-specific DB config
                            CustomJabberBot, // How the bot connects
							CustomEmailNotificationsConfig, // connection credentials and config for email sending
							CustomSearchable
                          ]

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
        html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        pdf: 'application/pdf',
        rtf: 'application/rtf',
        excel: 'application/vnd.ms-excel',
        ods: 'application/vnd.oasis.opendocument.spreadsheet',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

grails.dbconsole.enabled = false
grails.dbconsole.urlRoot = '/admin/dbconsole'

jenna.availableHumours = ['sweet', 'angry']
jenna.availableLanguages = ['en', 'es']
jenna.defaultHumour = 'sweet'
jenna.defaultLanguage = 'en'
jenna.authorizations = [
        'CreateProjectRequestHandler': [Permission.ROLE_SYSTEM_ADMIN,  Permission.ROLE_PROJECT_LEADER],
        'CreateAssignmentRequestHandler': [Permission.ROLE_SYSTEM_ADMIN,  Permission.ROLE_PROJECT_LEADER],
        'ActiveAssignmentsRequestHandler': [Permission.ROLE_USER]
]

// log4j configuration
log4j = { root ->

    root.level = org.apache.log4j.Level.WARN

    appenders {
        //console name: 'stdout', layout: pattern(conversionPattern: '%d{dd MMM yyyy HH:mm:ss,SSS} [%15.15t] %-5p %30.30c %x - %m%n')
        rollingFile name: 'file', maxFileSize: 1024 * 1024 * 1024, maxBackupIndex: 10, file: '/tmp/jenna.log', layout: pattern(conversionPattern: '%d{dd MMM yyyy HH:mm:ss,SSS} [%15.15t] %-5p %30.30c %x - %m%n')
    }

    error  file: 'org.codehaus.groovy.grails.web.servlet',  //  controllers
	       'org.codehaus.groovy.grails.web.pages', //  GSP
	       'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	       'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
	       'org.codehaus.groovy.grails.web.mapping', // URL mapping
	       'org.codehaus.groovy.grails.commons', // core / classloading
	       'org.codehaus.groovy.grails.plugins', // plugins
	       'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	       'org.springframework',
	       'org.hibernate',
           'org.apache.commons.digester'
    warn   file: 'org.mortbay.log'
    info   file: 'grails.app'
    info   file: 'log4j.logger.org.springframework.security'
}
