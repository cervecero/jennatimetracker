import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * @author Leandro Larroulet
 * Date: Aug 13, 2010
 * Time: 15:00:00 PM
 */
class InviteCoworkersJob {

    def group = 'jenna-jobs'
    def name = 'invite-coworkers-job'

    def sessionRequired = false
    def concurrent = false

    def grailsApplication
    def cronExpression = ConfigurationHolder.config['chat']['cronExpression']
/*
    static triggers = {
        cron startDelay: 10000, cronExpression: '0 0/15 0-23 ? * MON-FRI'
    }
*/

    JabberService jabberService

    def execute() {
        Date now = new Date()
        int hours = now.hours
        int minutes = now.minutes



      /**
        * la idea es que pregunte 10 minutos m�s tarde de la hora configurada.
       * Si minutos es menor a 10, que pregunte a la hora anterior (hora -1) minutos = 60 - minutos.
       * Si justo eran las 0 hs, que pregunte a las 23.
       */
        if (minutes < 10){
          if (hours == 0){
            hours = 23
          } else {
            hours--
          }
          minutes = 50 + minutes
        } else {
          minutes = minutes - 10
        }
        String currentTimeExpression = "${hours < 10 ? '0' + hours : hours}:${minutes < 10 ? '0' + minutes : minutes}"
        User.withTransaction {

            // Busco a todos los que sean un s�lo usuario en la empresa.
            // Y que no tengan un registro que diga, no me jod�s m�s Jenna.
            def users = User.findAllByLocalChatTime(currentTimeExpression)

            Permission permission = Permission.findByName(Permission.ROLE_COMPANY_ADMIN)
            List owners = new ArrayList()

            users.each { User user ->
              if (user.permissions.contains(permission) && (user.remindToInviteCoworkers== null || user.remindToInviteCoworkers)){
                def usersInCompany = User.countByCompany(user.company)
                if (usersInCompany == 1)
                  owners.add(user)
              }
            }

            if (owners?.size()>0)
              jabberService.queryUsersToInviteCoworkers(owners)

        }
    }

}