

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="milestone.show" default="Milestone List" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>

                   	    <g:sortableColumn property="id" title="Id" titleKey="milestone.id" />

                   	    <g:sortableColumn property="name" title="Name" titleKey="milestone.name" />

                   	    <g:sortableColumn property="description" title="Description" titleKey="milestone.description" />

                   	    <g:sortableColumn property="dueDate" title="Due Date" titleKey="milestone.dueDate" />

                   	    <th><g:message code="milestone.project" default="Project" /></th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${milestoneInstanceList}" status="i" var="milestoneInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="show" id="${milestoneInstance.id}">${fieldValue(bean: milestoneInstance, field: "id")}</g:link></td>

                            <td>${fieldValue(bean: milestoneInstance, field: "name")}</td>

                            <td>${fieldValue(bean: milestoneInstance, field: "description")}</td>

                            <td><g:formatDate date="${milestoneInstance.dueDate}" /></td>

                            <td>${fieldValue(bean: milestoneInstance, field: "project")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="milestone.new" default="New Milestone" /></g:link></span>
                <g:paginate total="${milestoneInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
