

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
            <h1><span class="style7"><g:message code="effort.show" default="Show Effort" /></span></h1>
            <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${effortInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="effort.id" default="Id" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: effortInstance, field: "id")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="effort.date" default="Date" />:</td>

                                <td valign="top" class="value"><g:formatDate date="${effortInstance?.date}" /></td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="effort.timeSpent" default="Time Spent" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: effortInstance, field: "timeSpent")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="effort.comment" default="Comment" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: effortInstance, field: "comment")}</td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="effort.tags" default="Tags" />:</td>
                                <td  valign="top" style="text-align: left;" class="value">
                                    <ul>
                                    <g:each in="${effortInstance?.tags}" var="tagInstance">
                                        <li><g:link controller="tag" action="show" id="${tagInstance.id}">${tagInstance.encodeAsHTML()}</g:link></li>
                                    </g:each>
                                    </ul>
                                </td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="effort.user" default="User" />:</td>

                                <td valign="top" class="value"><g:link controller="user" action="show" id="${effortInstance?.user?.id}">${effortInstance?.user?.encodeAsHTML()}</g:link></td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                  <span class="menuButton"><g:link class="list" action="list"><g:message code="effort.list" default="Effort List" /></g:link></span>
                  <span class="menuButton"><g:link class="create" action="create"><g:message code="effort.new" default="New Effort" /></g:link></span>
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
