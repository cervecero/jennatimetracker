import grails.test.*

class EffortRequestHandlerTests extends GrailsUnitTestCase {

    void testRegExp() {
        def tests = [
                '2': ['2', null, null],
                '2 ': ['2', null, null],
                '2:30': ['2:30', null, null],
                '2:30 ': ['2:30', null, null],
                '2:30 pdm': ['2:30', 'pdm', null],
                '2:30,pdm': ['2:30', 'pdm', null],
                '2:30 pdm,balanza': ['2:30', 'pdm,balanza', null],
                '2:30 pdm, balanza': ['2:30', 'pdm, balanza', null],
                '2 (hola)': ['2', null, 'hola'],
                '2,(hola)': ['2', null, 'hola'],
                '2:30 (hola)': ['2:30', null, 'hola'],
                '2:30,(hola)': ['2:30', null, 'hola'],
                '2:30 pdm (hola)': ['2:30', 'pdm ', 'hola'],
                '2:30,pdm (hola)': ['2:30', 'pdm ', 'hola'],
                '2:30 pdm,balanza (hola chau)': ['2:30', 'pdm,balanza ', 'hola chau']
        ]
        tests.each { k, v ->
            def matcher = k =~ /^(\d+(?:\:\d{2})?)(?:[\s\,]*)([\w\d][\w\d\.\, ]+)*(?:[\s]*)(?:\((.*)\))?$/
            assertTrue(matcher.matches())
            assertEquals(v, matcher[0][1..-1])
        }
    }

    void testParseTime() {
        EffortRequestHandler handler = new EffortRequestHandler()
        double time = handler.parseTime('1:30')
        
        assertEquals(1.5, time, 0)
        time = handler.parseTime('1.50')
        assertEquals(1.5, time, 0)
        time = handler.parseTime('0:15')
        assertEquals(0.25, time, 0)
        time = handler.parseTime('0.25')
        assertEquals(0.25, time, 0)
    }
}
