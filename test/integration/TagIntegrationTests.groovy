import grails.test.*

class TagIntegrationTests extends GrailsUnitTestCase {

    void testInsert() {
        def tag = new Tag(name: 'test')
        assert tag.name == 'test'
        assert tag.soundex == null
        tag.save(flush: true)
        assert tag.soundex != null
        assert tag.soundex.startsWith('T')
    }
}
