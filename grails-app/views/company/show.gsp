

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
            <h1><span class="style7"><g:message code="company.show" default="Show Company" /></span></h1>
            <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${companyInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="company.id" default="Id" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "id")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="company.name" default="Name" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "name")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="company.employees" default="Employees" />:</td>

                                <td  valign="top" style="text-align: left;" class="value">
                                    <ul>
                                    <g:each in="${companyInstance?.employees}" var="userInstance">
                                        <li><g:link controller="user" action="show" id="${userInstance.id}">${userInstance.encodeAsHTML()}</g:link></li>
                                    </g:each>
                                    </ul>
                                </td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                  <span class="menuButton"><g:link class="list" action="list"><g:message code="company.list" default="Company List" /></g:link></span>
                  <span class="menuButton"><g:link class="create" action="create"><g:message code="company.new" default="New Company" /></g:link></span>
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
