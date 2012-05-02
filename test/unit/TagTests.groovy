import grails.test.*

class TagTests extends GrailsUnitTestCase {

    void testInsert() {
        def tag = new Tag(name: 'test')
        assert tag.name == 'test'
        assert tag.soundex == null
        tag.save()
        assert tag.soundex != null
        assert tag.soundex.startsWith('T')
    }
}
