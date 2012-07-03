<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.reports.mood"/></title>
        <r:require modules="highcharts"/>
        <r:script>
        var chart;
        $(document).ready(function() {
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'moodChart',
                },
                 title: {
                    text: ''
                },
                credits: {
                      enabled: false,
                },
                yAxis: {
		            title: {
		                text: '<g:message code="app.menu.administration.reports.mood" />'
		            },
		            min: 0,
	           },
	           xAxis: {
	               tickInterval: 1,
	           },
               series: [{
                    type: 'line',
                    name: '${user}',
                    data: [
                        <g:each in="${userMood}" var="mood">
	                        {
	                           x: ${mood.key },
	                           y: ${mood.value},
	                        },
                        </g:each>
                    ]
                },
                {
                    type: 'line',
                    name: '${ user.company }',
                    data: [
                          <g:each in="${companyMood}" var="mood">
                          {
                              x: ${mood.key },
                              y: ${mood.value},
                          },
                          </g:each>
                    ]
                }]
            });
        });

    </r:script>
    </head>
    <body>

    <form action="mood" method="POST" id="updateMoodReport">
	    <fieldset>
		    <label for="selectedMonth"><g:message code="reports.mood.select.month"/>:</label>
		    <g:select name="selectedMonth" from="${1..12}" value="${month}" style="width:100px" />

	        <label for="selectedYear"><g:message code="reports.mood.select.year"/>:</label>
	        <g:select name="selectedYear" from="${yearList}" value="${year}" style="width:100px" />

	        <g:ifAnyGranted role="ROLE_COMPANY_ADMIN,ROLE_PROJECT_LEADER">
	            <label for="selectedYear"><g:message code="reports.mood.select.user"/>:</label>
	            <g:select name="selectedUser" from="${usersList}" optionKey="id" value="${user.id}"  style="width:200px" />
	        </g:ifAnyGranted>
	    </fieldset>
	    <input type="submit" class="ui-button ui-state-default ui-corner-all" value="${message(code:"reports.reload.graphic")}" />
    </form>

    <div id="moodChart">
    </div>

    </body>
</html>
