// Default configuration for the DataSource
// To customize it, copy CusotmDataSource.groovy.example as customconf/CustomDataSource.groovy and modify accordingly
dataSource {
    driverClassName = 'com.mysql.jdbc.Driver'
    configClass = org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration
    pooled = true
    dbCreate = 'update'
}

hibernate {
    cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    show_sql = false
	dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
}

// Making tests run in-memory
environments {
    test {
        dataSource {
            driverClassName = 'org.h2.Driver'
            dbCreate = 'create'
            url = 'jdbc:h2:mem:devDb'
        }
        hibernate {
            cache.use_second_level_cache = true
            cache.use_query_cache = true
            cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
            show_sql = true
            dialect = "org.hibernate.dialect.H2Dialect"
        }
    }
}
