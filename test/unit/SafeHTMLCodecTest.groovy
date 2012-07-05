import grails.test.*
import SafeHTMLCodec

class SafeHTMLCodecTest extends GrailsUnitTestCase {

    void testStringWithoutSpecialCharsIsLeftAsItIs() {
        def message = "I am a safe string myself"
		def safeMessage = SafeHTMLCodec.encode(message)
        assertEquals(message, safeMessage)
    }

	void testStringWithAScriptIsSanitized() {
		def message = "I want to hack you! <script>alert('Hello World!');</script>"
		def safeMessage = SafeHTMLCodec.encode(message)
		assert !safeMessage.contains('<script>')
	}

	void testStringWithAScriptAndParametersIsSanitized() {
		def message = "I want to hack you! <script type='text/javascript'>alert('Hello World!');</script>"
		def safeMessage = SafeHTMLCodec.encode(message)
		assert !safeMessage.contains('<script>')
	}

	void testHighlightingIsAllowed() {
		def message = "I want to <b>hack</b> you!, and you'll let me"
		def safeMessage = SafeHTMLCodec.encode(message)
		assert safeMessage.contains('<b>')
		assert safeMessage.contains('</b>')
	}

	void testLinksAreConverted() {
		def message = "I really like http://9gag.com"
		def safeMessage = SafeHTMLCodec.encode(message)
		assert safeMessage.contains("<a href='http://9gag.com'")
		assert safeMessage.contains('</a>')
	}

	void testLinkWithJavascriptOnClickGetsSanitized() {
		def message = "I really like <a href='http://9gag.com' onclick='javascript:alert(\'Hello World!\')'>9gag</a"
		def safeMessage = SafeHTMLCodec.encode(message)
		assert safeMessage.contains("I really like &lt;a href=")
        assert safeMessage.contains("<a href='http://9gag.com'")
        assert safeMessage.contains('</a>')
	}
}
