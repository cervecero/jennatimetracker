

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
            <h1><span class="style7"><g:message code="milestone.show" default="Show Milestone" /></span></h1>
            <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${milestoneInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="milestone.id" default="Id" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: milestoneInstance, field: "id")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="milestone.name" default="Name" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: milestoneInstance, field: "name")}</td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="milestone.description" default="Description" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: milestoneInstance, field: "description")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="milestone.dueDate" default="Due Date" />:</td>

                                <td valign="top" class="value"><g:formatDate date="${milestoneInstance?.dueDate}" /></td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="milestone.project" default="Project" />:</td>

                                <td valign="top" class="value"><g:link controller="project" action="show" id="${milestoneInstance?.project?.id}">${milestoneInstance?.project?.encodeAsHTML()}</g:link></td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                  <span class="menuButton"><g:link class="list" action="list"><g:message code="milestone.list" default="Milestone List" /></g:link></span>
                  <span class="menuButton"><g:link class="create" action="create"><g:message code="milestone.new" default="New Milestone" /></g:link></span>
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
