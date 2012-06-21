import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.ConfigurationHolder

security {

	active = true

	loginUserDomainClass = 'User'
	userName = 'account'
	password = 'password'
	enabled = 'enabled'
	relationalAuthorities = 'permissions'

	authorityDomainClass = 'Permission'
	authorityField = 'name'

	/** authenticationProcessingFilter */
	authenticationFailureUrl = '/login/authfail?login_error=1'
	ajaxAuthenticationFailureUrl = '/login/authfail?ajax=true'
	defaultTargetUrl = '/'
	alwaysUseDefaultTargetUrl = false
	filterProcessesUrl = '/j_spring_security_check'

	/** anonymousProcessingFilter */
	key = 'foo'
	userAttribute = 'anonymousUser,ROLE_ANONYMOUS'

	/** authenticationEntryPoint */
	loginFormUrl = '/login/auth'
	forceHttps = 'false'
	ajaxLoginFormUrl = '/login/authAjax'

	/** logoutFilter */
	afterLogoutUrl = '/'

	/** accessDeniedHandler
	 *  set errorPage to null, if you want to get error code 403 (FORBIDDEN).
	 */
	errorPage = '/login/denied'
	ajaxErrorPage = '/login/deniedAjax'
	ajaxHeader = 'X-Requested-With'

	/** passwordEncoder */
	//The digest algorithm to use.
	//Supports the named Message Digest Algorithms in the Java environment.
	//http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA
	algorithm = 'MD5' // Ex. MD5 SHA
	//use Base64 text ( true or false )
	encodeHashAsBase64 = false

	/** rememberMeServices */
	cookieName = 'jenna_remember_me'
	alwaysRemember = false
	tokenValiditySeconds = 1209600 //14 days
	parameter = '_spring_security_remember_me'
	rememberMeKey = 'jennaRocks' // FIXME: Make a config

	/** LoggerListener
	 * ( add 'log4j.logger.org.springframework.security=info,stdout'
	 * to log4j.*.properties to see logs )
	 */
	useLogger = true

	/** use RequestMap from DomainClass */
	useRequestMapDomainClass = false

	/** use annotations from Controllers to define security rules */
	useControllerAnnotations = false
	controllerAnnotationsMatcher = 'ant' // or 'regex'
	controllerAnnotationsMatchesLowercase = true
	controllerAnnotationStaticRules = [:]
	controllerAnnotationsRejectIfNoRule = false

	requestMapString = """
		CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
		PATTERN_TYPE_APACHE_ANT

		/login/**=IS_AUTHENTICATED_ANONYMOUSLY
		/admin/**=${Permission.ROLE_SYSTEM_ADMIN}
		/quartz/**=${Permission.ROLE_SYSTEM_ADMIN}
		/effort/**=${Permission.ROLE_USER},${Permission.ROLE_PROJECT_LEADER},${Permission.ROLE_COMPANY_ADMIN},${Permission.ROLE_SYSTEM_ADMIN}
		/project/**=${Permission.ROLE_PROJECT_LEADER}
		/assignment/**=${Permission.ROLE_PROJECT_LEADER}
		/role/**=${Permission.ROLE_PROJECT_LEADER}
		/tags/**=${Permission.ROLE_PROJECT_LEADER}
		/chart/**=${Permission.ROLE_PROJECT_LEADER}
		/report/**=${Permission.ROLE_PROJECT_LEADER},${Permission.ROLE_COMPANY_ADMIN},${Permission.ROLE_SYSTEM_ADMIN}
		/company/**=${Permission.ROLE_SYSTEM_ADMIN}
		/user/**=${Permission.ROLE_USER},${Permission.ROLE_PROJECT_LEADER},${Permission.ROLE_COMPANY_ADMIN},${Permission.ROLE_SYSTEM_ADMIN}
		/**=IS_AUTHENTICATED_ANONYMOUSLY
	"""
	/**
	 * if useRequestMapDomainClass is false, set request map pattern in string
	 * see example below
	 */

	// basic auth
	// FIXME: Is this still used?
	realmName = 'Jenna Realm'

	/** use basicProcessingFilter */
	basicProcessingFilter = false
	/** use switchUserProcessingFilter */
	switchUserProcessingFilter = false
	swswitchUserUrl = '/j_spring_security_switch_user'
	swexitUserUrl = '/j_spring_security_exit_user'
	swtargetUrl = '/'

	/**use email notification while registration and report sending FIXME?*/
    useMail = ConfigurationHolder.config['mailSender']['useMail']
	mailHost = ConfigurationHolder.config['mailSender']['mailHost']
	mailPort = ConfigurationHolder.config['mailSender']['mailPort']
	mailUsername = ConfigurationHolder.config['mailSender']['mailUsername']
	mailPassword = ConfigurationHolder.config['mailSender']['mailPassword']
	mailProtocol = ConfigurationHolder.config['mailSender']['mailProtocol']
	mailFrom = ConfigurationHolder.config['mailSender']['mailFrom']
	javaMailProperties = ConfigurationHolder.config['mailSender']['javaMailProperties']

	/** default user's role for user registration */
	defaultRole = 'ROLE_USER'

	useOpenId = false

	// LDAP/ActiveDirectory
	useLdap = false

	// Kerberos
	useKerberos = false

	// HttpSessionEventPublisher
	useHttpSessionEventPublisher = false

	// user caching
	cacheUsers = !GrailsUtil.isDevelopmentEnv()

	// CAS
	useCAS = false

	// NTLM
	useNtlm = false

	// port mappings
	httpPort = 8080
	httpsPort = 8443

	// secure channel filter (http/https)
	secureChannelDefinitionSource = ''
	channelConfig = [secure: [], insecure: []]

	// ip restriction filter
	ipRestrictions = [:]

	// Facebook Connect
	useFacebook = false
}
