<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.reports.mood"/></title>
    </head>
    <body>

    <form action="mood" method="POST" id="updateMoodReport">
	    <fieldset>
		    <label for="selectedMonth"><g:message code="reports.mood.select.month"/>:</label>
		    <g:select name="selectedMonth" from="${1..12}"  style="width:100px" />

	        <label for="selectedYear"><g:message code="reports.mood.select.year"/>:</label>
	        <g:select name="selectedYear" from="${yearList}" style="width:100px" />

	        <g:ifAnyGranted role="ROLE_COMPANY_ADMIN,ROLE_PROJECT_LEADER">
	            <label for="selectedYear"><g:message code="reports.mood.select.user"/>:</label>
	            <g:select name="selectedUser" from="${usersList}" optionKey="id" value="name" style="width:200px" />
	        </g:ifAnyGranted>
	    </fieldset>
	    <input type="submit" class="ui-button ui-state-default ui-corner-all" value="${message(code:"reports.reload.graphic")}" />
    </form>

    <div style="text-align:center">
        <img src="/projectguide/${imagePath}">
    </div>

    </body>
</html>
