/**
 * @author Leandro Larroulet
 * Date: Aug 13, 2010
 * Time: 15:00:00 PM
 */
class InviteCoworkersJob {

    def group = 'jenna-jobs'
    def name = 'invite-coworkers-job'

    def sessionRequired = true
    def concurrent = false

    JabberService jabberService

    static triggers = {
    }

    def execute() {
        Date now = new Date()
        int hours = now.hours
        int minutes = now.minutes

        /**
        * The idea is to ask 10 minutes after the configured time for the user.
        * If minutes is less than 10, we have to move the hour by 1 and add 50 to the difference of minutes to the hour.
        */
        if (minutes < 10) {
			if (hours > 0) {
				hours--
			} else {
				hours = 23 // special case if it's 0hs
			}
			minutes = 50 + minutes
        } else {
        	minutes = minutes - 10
        }
        String currentTimeExpression = "${hours < 10 ? '0' + hours : hours}:${minutes < 10 ? '0' + minutes : minutes}"
        User.withTransaction {
            def users = User.findAllByLocalChatTime(currentTimeExpression)
            Permission permission = Permission.findByName(Permission.ROLE_COMPANY_ADMIN)
            List owners = new ArrayList()
			/*
			 * Find users that:
			 *  - Are Company Admins
			 *  - Are the only members of their organizations
			 *  - Didn't ask not to be bothered about this
			 */
            users.each { User user ->
              if (user.permissions.contains(permission) && (user.remindToInviteCoworkers == null || user.remindToInviteCoworkers)){
                def usersInCompany = User.countByCompany(user.company)
                if (usersInCompany == 1)
                  owners.add(user)
              }
            }

            if (owners?.size() > 0) {
				jabberService.queryUsersToInviteCoworkers(owners)
            }
        }
    }

}