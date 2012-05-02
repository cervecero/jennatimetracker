class Event {

    String name
    String description
    Date startDate
    String notificationDate
    String notificationTime
    String localNotificationDate
    String localNotificationTime
    Company company
    boolean deleted

    static hasMany = [participants: User]

    static mapping = {
        participants cascade: 'all'
    }

    static constraints = {
        name(blank: false, size: 0..100)
        description(blank: false, size: 0..1024)
        startDate(nullable: true)
        notificationDate(nullable: true)
        notificationTime(nullable: true)
        localNotificationDate(nullable: true)
        localNotificationTime(nullable: true)
        company(nullable: false)
    }

    static hibernateFilters = {
        enabledFilter(condition: 'deleted=0', default: true)
    }
}
