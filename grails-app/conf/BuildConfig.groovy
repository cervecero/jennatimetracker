grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.war.file = "target/${appName}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn"
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment these to enable remote dependency resolution
        // from public Maven repositories
        mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile',
        // 'runtime', 'test' or 'provided' scopes eg.

        compile 'javax.activation:activation:1.1'
        compile 'commons-digester:commons-digester:2.0'
        compile 'commons-logging:commons-logging:1.1.1'
        compile 'javax.mail:mail:1.4'
        compile 'org.apache.poi:poi:3.2-FINAL'
        compile 'ar.com.fdvs:DynamicJasper:3.0.13'
		compile 'org.markdownj:markdownj:0.3.0-1.0.2b4'
        compile 'jivesoftware:smack:3.0.4'
        compile 'jivesoftware:smackx:3.0.4'
        runtime 'mysql:mysql-connector-java:5.1.10'
    }

    plugins {
        compile ":hibernate:$grailsVersion"
        compile ":acegi:0.5.3.2"
        compile ":avatar:0.3"
        compile ":hibernate-filter:0.3.1"
        compile ":i18n-templates:1.1.0.1"
        compile ":quartz:0.4.2"
        compile ":quartz-monitor:0.2"
        compile ":session-temp-files:1.0"
        compile ":export:1.1"

        compile ':database-migration:1.1'
        runtime ":resources:1.2-RC1"
        runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:$grailsVersion"
    }
}
