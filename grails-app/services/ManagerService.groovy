class ManagerService {

    boolean isChattingDay(Date _date) {
        //TODO implement according to cron expression
        return true
    }

    Date getPreviousChattingDay(Date _date) {
        //TODO implement according to cron expression
        return (_date - 1).onlyDate
    }
}
