/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: 11/25/11
 * Time: 3:24 PM
 */
class TimeZoneUtilTest extends GroovyTestCase {

    static final DEFAULT_TZ_OFFSET = -3

    static final REFERENCE_DATE = '2011-11-21'
    static final DAY_BEFORE_REFERENCE_DATE = '2011-11-20'
    static final DAY_AFTER_REFERENCE_DATE = '2011-11-22'

    @Override
    protected void setUp() {
        super.setUp()
        TimeZone.default = buildTimeZoneFromOffsetInHours(DEFAULT_TZ_OFFSET)
    }

    void testSameDayWithTZEastern() {
        def myTimeZone = buildTimeZoneFromOffsetInHours(DEFAULT_TZ_OFFSET + 6)
        def systemDate = TimeZoneUtil.toSystemDate(REFERENCE_DATE, '06:30', myTimeZone)
        assertEquals(REFERENCE_DATE, systemDate)
    }

    void testDayBeforeWithTZEastern() {
        def myTimeZone = buildTimeZoneFromOffsetInHours(DEFAULT_TZ_OFFSET + 6)
        def systemDate = TimeZoneUtil.toSystemDate(REFERENCE_DATE, '05:30', myTimeZone)
        assertEquals(DAY_BEFORE_REFERENCE_DATE, systemDate)
    }

    void testSameDayWithTZWestern() {
        def myTimeZone = buildTimeZoneFromOffsetInHours(DEFAULT_TZ_OFFSET - 6)
        def systemDate = TimeZoneUtil.toSystemDate(REFERENCE_DATE, '17:30', myTimeZone)
        assertEquals(REFERENCE_DATE, systemDate)
    }

    void testDayAfterWithTZWestern() {
        def myTimeZone = buildTimeZoneFromOffsetInHours(DEFAULT_TZ_OFFSET - 6)
        def systemDate = TimeZoneUtil.toSystemDate(REFERENCE_DATE, '18:30', myTimeZone)
        assertEquals(DAY_AFTER_REFERENCE_DATE, systemDate)
    }

    TimeZone buildTimeZoneFromOffsetInHours(hours) {
        return new SimpleTimeZone(hoursToMillis(hours), "GMT${hours < 0 ? '-' : '+'}${Math.abs(hours) < 10 ? '0' : ''}${hours}00")
    }

    int hoursToMillis(hs) {
        return hs * 60 * 60 * 1000
    }
}
