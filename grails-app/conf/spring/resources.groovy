// Place your Spring DSL code here
beans = {
    localeResolver(UserLocaleResolver)
	/**
	 * RequestHandlers for the chat bot
	 */
	humourRequestHandler(HumourRequestHandler,availableHumours: grailsApplication.config['jenna']['availableHumours'])
	inviteCoworkersStep1RequestHandler(InviteCoworkersStep1RequestHandler)
	cancelRequestHandler(CancelRequestHandler)
	enterYesterdayHoursRequestHandler(EnterYesterdayHoursRequestHandler)
	enterHoursRequestHandler(EnterHoursRequestHandler)
	activeAssignmentsRequestHandler(ActiveAssignmentsRequestHandler)
	helpRequestHandler(HelpRequestHandler)
	languageRequestHandler(LanguageRequestHandler, availableLanguages: grailsApplication.config['jenna']['availableLanguages'])
	salutationStep2RequestHandler(SalutationStep2RequestHandler)
	salutationStep3RequestHandler(SalutationStep3RequestHandler)
	salutationStep4RequestHandler(SalutationStep4RequestHandler)
	salutationRequestHandler(SalutationRequestHandler)
	problemRequestHandler(ProblemRequestHandler)
	problemStep2RequestHandler(ProblemStep2RequestHandler)
	problemStep3RequestHandler(ProblemStep3RequestHandler)
	problemStep4RequestHandler(ProblemStep4RequestHandler)
	knowledgeStep1RequestHandler(KnowledgeStep1RequestHandler)
	knowledgeStep2RequestHandler(KnowledgeStep2RequestHandler)
	knowledgeStep3RequestHandler(KnowledgeStep3RequestHandler)
	todayRequestHandler(TodayRequestHandler)
	yesterdayRequestHandler(YesterdayRequestHandler)
	inviteCoworkersStep1RequestHandler(InviteCoworkersStep1RequestHandler)
	inviteCoworkersStep2RequestHandler(InviteCoworkersStep2RequestHandler)
	inviteCoworkersStep3RequestHandler(InviteCoworkersStep3RequestHandler, grailsApplication: grailsApplication)
	chatService(ChatService) {
		chatHandlers = [
			humourRequestHandler,
			languageRequestHandler,
			inviteCoworkersStep1RequestHandler,
			cancelRequestHandler,
			enterYesterdayHoursRequestHandler,
			enterHoursRequestHandler,
			activeAssignmentsRequestHandler,
			helpRequestHandler,
			salutationStep2RequestHandler,
			salutationStep3RequestHandler,
			salutationStep4RequestHandler,
			salutationRequestHandler,
			problemRequestHandler,
			problemStep2RequestHandler,
			problemStep3RequestHandler,
			problemStep4RequestHandler,
			knowledgeStep1RequestHandler,
			knowledgeStep2RequestHandler,
			knowledgeStep3RequestHandler,
			todayRequestHandler,
			yesterdayRequestHandler,
			inviteCoworkersStep1RequestHandler,
			inviteCoworkersStep2RequestHandler,
			inviteCoworkersStep3RequestHandler
		]
	}
}
