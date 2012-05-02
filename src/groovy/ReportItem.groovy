
import eliza.ElizaMain
import java.text.DateFormat
import org.springframework.context.MessageSource

/**
 * @author Leandro Larroulet
 * Date: Aug 24, 2010
 * Time: 12:00:12 PM
 */

class ReportItem {
    String project
    Integer timeSpent
    String user
    String role
    Date date
    Date assignmentStartDate
    Date assignmentEndDate
    String comment
    Integer moodValue
    String  knowledge
}