

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="project.show" default="Create Project" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${projectInstance}">
            <div class="errors">
                <g:renderErrors bean="${projectInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="project.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="50" value="${fieldValue(bean: projectInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="project.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" rows="5" cols="40" value="${fieldValue(bean: projectInstance, field: 'description')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="startDate"><g:message code="project.startDate" default="Start Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'startDate', 'errors')}">
                                  <jquery:datePicker name="startDate" value="${projectInstance?.startDate}" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="endDate"><g:message code="project.endDate" default="End Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'endDate', 'errors')}">
                                  <jquery:datePicker name="endDate" value="${projectInstance?.endDate}" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="projectTagName"><g:message code="projectTagName" default="Project Tag Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectTagInstance, field: 'name', 'errors')}">
                                    <g:textField name="projectTagName" maxlength="50" value="${fieldValue(bean: projectInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="clientTagName"><g:message code="clientTagName" default="Client Tag Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: clientTagInstance, field: 'name', 'errors')}">
                                    <g:textField name="clientTagName" maxlength="50" value="${fieldValue(bean: clientTagInstance, field: 'name')}" />

                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="project.list" default="Project List" /></g:link></span>
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
