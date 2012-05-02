// Default configuration for the DataSource, using in-memory DB
// To customize it, copy DataSourcePerEnvironment.groovy.example as DataSourcePerEnvironment.groovy and modify accordingly
dataSource {
    pooled = true
    driverClassName = 'org.h2.Driver'
    dbCreate = 'update'
    url = 'jdbc:h2:mem:devDb'
    configClass = org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
    show_sql = false
    dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
}

