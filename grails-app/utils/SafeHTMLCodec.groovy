import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

/**
 * Behaves like {@link HTMLCodec}, but allows some tags, as well as it converts
 * liks to <a> so they're followable.
 * 
 * Specially useful if you want to show some user input's HTML, but disallow js injection or DOM manipulation
 * @author Mariano Simone
 *
 */
public class SafeHTMLCodec {
	static SAFE_ENTITIES = [
		"&lt;b&gt;": "<b>",
		"&lt;/b&gt;": "</b>",
		"\n": "<br>",
		/(?:http|https|ftp)\:\S+/: { "<a href=\'${it}\' target='_blank'>${it}</a>" }
		]

	static encode = { obj ->
		String rv = HTMLCodec.encode(obj.toString())
		SAFE_ENTITIES.each() { sanitized, normal -> rv = rv.replaceAll(sanitized, normal) };
		return rv
	}

	static decode = { obj ->
		HTMLCodec.decode(obj.toString())
	}
}
