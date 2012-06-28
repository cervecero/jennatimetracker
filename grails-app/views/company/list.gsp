<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="company.show" default="Company List" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
	                   	    <g:sortableColumn property="id" title="Id" titleKey="company.id" />
	                   	    <g:sortableColumn property="name" title="Name" titleKey="company.name" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${companyInstanceList}" status="i" var="companyInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="show" id="${companyInstance.id}">${fieldValue(bean: companyInstance, field: "id")}</g:link></td>
                            <td>${fieldValue(bean: companyInstance, field: "name")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="company.new" default="New Company" /></g:link></span>
                <g:paginate total="${companyInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
