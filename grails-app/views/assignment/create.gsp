

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="assignment.show" default="Create Assignment" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${assignmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${assignmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="assignment.project" default="Project" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${projectList}" optionKey="id" value="${assignmentInstance?.project?.id}"  />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="user"><g:message code="assignment.user" default="User" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'user', 'errors')}">
                                    <g:select name="user.id" from="${userList}" optionKey="id" value="${assignmentInstance?.user?.id}"  />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="role"><g:message code="assignment.role" default="Role" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'role', 'errors')}">
                                    <g:select name="role.id" from="${roleList}" optionKey="id" value="${assignmentInstance?.role?.id}"  />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="startDate"><g:message code="assignment.startDate" default="Start Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'startDate', 'errors')}">
                                    <g:datePicker name="startDate" value="${assignmentInstance?.startDate}"  />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="endDate"><g:message code="assignment.endDate" default="End Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'endDate', 'errors')}">
                                    <g:datePicker name="endDate" value="${assignmentInstance?.endDate}"  />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="assignment.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" rows="5" cols="40" value="${fieldValue(bean: assignmentInstance, field: 'description')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="active"><g:message code="assignment.active" default="Active" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: assignmentInstance, field: 'active', 'errors')}">
                                    <g:checkBox name="active" value="${fieldValue(bean: assignmentInstance, field: 'active')}" />

                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="assignment.list" default="Assignment List" /></g:link></span>
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
