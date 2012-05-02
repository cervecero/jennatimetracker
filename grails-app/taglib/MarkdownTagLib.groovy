import com.petebevin.markdown.MarkdownProcessor

class MarkdownTagLib {

    static namespace = "markdown"

    def renderHtml = { attrs, body ->
        def text = attrs.text ?: body()
        MarkdownProcessor m = new MarkdownProcessor()
        out << m.markdown(text)
    }
}
