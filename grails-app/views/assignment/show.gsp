

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
            <h1><span class="style7"><g:message code="assignment.show" default="Show Assignment" /></span></h1>
            <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${assignmentInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="assignment.id" default="Id" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: assignmentInstance, field: "id")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="assignment.project" default="Project" />:</td>

                                <td valign="top" class="value"><g:link controller="project" action="show" id="${assignmentInstance?.project?.id}">${assignmentInstance?.project?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="assignment.user" default="User" />:</td>

                                <td valign="top" class="value"><g:link controller="user" action="show" id="${assignmentInstance?.user?.id}">${assignmentInstance?.user?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="assignment.role" default="Role" />:</td>

                                <td valign="top" class="value"><g:link controller="role" action="show" id="${assignmentInstance?.role?.id}">${assignmentInstance?.role?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="assignment.startDate" default="Start Date" />:</td>

                                <td valign="top" class="value"><g:formatDate date="${assignmentInstance?.startDate}" /></td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="assignment.endDate" default="End Date" />:</td>

                                <td valign="top" class="value"><g:formatDate date="${assignmentInstance?.endDate}" /></td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="assignment.description" default="Description" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: assignmentInstance, field: "description")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="assignment.active" default="Active" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: assignmentInstance, field: "active")}</td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                  <span class="menuButton"><g:link class="list" action="list"><g:message code="assignment.list" default="Assignment List" /></g:link></span>
                  <span class="menuButton"><g:link class="create" action="create"><g:message code="assignment.new" default="New Assignment" /></g:link></span>
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
