

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="effort.show" default="Create Effort" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${effortInstance}">
            <div class="errors">
                <g:renderErrors bean="${effortInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="date"><g:message code="effort.date" default="Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: effortInstance, field: 'date', 'errors')}">
                                    <g:datePicker name="date" value="${effortInstance?.date}"  />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="timeSpent"><g:message code="effort.timeSpent" default="Time Spent" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: effortInstance, field: 'timeSpent', 'errors')}">
                                    <g:textField name="timeSpent" value="${fieldValue(bean: effortInstance, field: 'timeSpent')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="comment"><g:message code="effort.comment" default="Comment" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: effortInstance, field: 'comment', 'errors')}">
                                    <g:textArea name="comment" rows="5" cols="40" value="${fieldValue(bean: effortInstance, field: 'comment')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="user"><g:message code="effort.user" default="User" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: effortInstance, field: 'user', 'errors')}">
                                    <g:select name="user.id" from="${User.list()}" optionKey="id" value="${effortInstance?.user?.id}"  />

                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="effort.list" default="Effort List" /></g:link></span>
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
