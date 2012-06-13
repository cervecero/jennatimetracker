import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * ConfigScript super class which allows to include pieces of configuration from other files.
 *
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: 6/11/12
 * Time: 4:54 PM
 *
 * see http://naleid.com/blog/2009/07/30/modularizing-groovy-config-files-with-a-dash-of-meta-programming/
 */
public abstract class ComposedConfigScript extends Script {
    private static Log log = LogFactory.getLog(ComposedConfigScript.class);
    
    def includeScript(scriptClass) {
        try {
            def scriptInstance = scriptClass.newInstance()
            scriptInstance.metaClass = this.metaClass
            scriptInstance.binding = new ConfigBinding(this.getBinding().callable)
            scriptInstance.&run.call()
        } catch (Exception e) {
            log.warn("Couldn't load composed config script for class " + scriptClass, e)
        }
    }
}