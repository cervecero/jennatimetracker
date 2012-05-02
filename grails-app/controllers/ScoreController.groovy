import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

class ScoreController extends BaseController {

    def index = { redirect(action: "list", params: params) }

    def beforeInterceptor = [action:this.&auth]

    def auth() {
         try {
          findLoggedUser()
          return true
         } catch (Exception e){
          redirect(controller:'login', action:'auth')
          return false
         }
    }

    def list = {
      User user = findLoggedUser()
      params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)

      def scoreCategories = ScoreCategory.findAllByCompany(user.company)

      [scoreCategories: scoreCategories, scoreCategoriesTotal: scoreCategories.size()]
    }

    def update = {

      User user = findLoggedUser()
      def scoreCategories = ScoreCategory.findAllByCompany(user.company)


      scoreCategories.each { ScoreCategory sc ->
        String id = "scores_${sc.id}";
        int newPoints = Integer.parseInt(params[id])
        sc.points = newPoints 
      }

      flash.message="Points Updated!"

      render(view: 'list', model: [scoreCategories: scoreCategories, scoreCategoriesTotal: scoreCategories.size()])
    }
}