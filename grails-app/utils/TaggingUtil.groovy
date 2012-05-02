import org.apache.commons.codec.language.Soundex

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Jul 30, 2009
 * Time: 12:19:16 AM
 */
class TaggingUtil {

    private static Soundex soundexCalculator = new Soundex()

    def static normalize(tagName) {
        tagName?.trim().toLowerCase()
    }

    def static calculateSoundex(tagName) {
        soundexCalculator.soundex(tagName)
    }
}
