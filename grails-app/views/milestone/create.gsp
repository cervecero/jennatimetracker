

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="milestone.show" default="Create Milestone" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${milestoneInstance}">
            <div class="errors">
                <g:renderErrors bean="${milestoneInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="milestone.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: milestoneInstance, field: 'name', 'errors')}">
                                    <g:textArea name="name" rows="5" cols="40" value="${fieldValue(bean: milestoneInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="milestone.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: milestoneInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" rows="5" cols="40" value="${fieldValue(bean: milestoneInstance, field: 'description')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="dueDate"><g:message code="milestone.dueDate" default="Due Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: milestoneInstance, field: 'dueDate', 'errors')}">
                                    <g:datePicker name="dueDate" value="${milestoneInstance?.dueDate}"  />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="milestone.project" default="Project" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: milestoneInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${Project.list()}" optionKey="id" value="${milestoneInstance?.project?.id}"  />

                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="milestone.list" default="Milestone List" /></g:link></span>
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
