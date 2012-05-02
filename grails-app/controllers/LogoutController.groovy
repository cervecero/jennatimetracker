/**
 * Logout Controller (Example).
 */
class LogoutController {

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		redirect(uri: '/j_spring_security_logout')
	}
}
