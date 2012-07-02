import javax.jws.WebParam
import grails.converters.XML
import org.grails.plugins.springsecurity.service.AuthenticateService
/**
 * @author Leandro Larroulet
 * Date: 12/10/2010
 * Time: 15:08:49
 */
class TeroLoginService {
    static expose=['xfire']

    boolean transactional = true
    AuthenticateService authenticateService

   def String[] findUser(String account, String password) {

     String pass = authenticateService.encodePassword(password)

     User user = User.findByAccountAndPassword(account, pass)

     if (user){
       String[] r = new String[1];
       r[0] = user.id+","+user.account+","+user.name+","+user.company.id
       return r;
       //return us as XML
     } else {
       return new String[1]
     }
   }
}
