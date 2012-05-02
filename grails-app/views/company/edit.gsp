

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
        <div class="body">
          <h1><span class="style7"><g:message code="company.show" default="Edit Company" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${companyInstance}">
            <div class="errors">
                <g:renderErrors bean="${companyInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${companyInstance?.id}" />
                <g:hiddenField name="version" value="${companyInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="company.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'name', 'errors')}">
                                    <g:textArea name="name" rows="5" cols="40" value="${fieldValue(bean: companyInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="employees"><g:message code="company.employees" default="Employees" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'employees', 'errors')}">

<ul>
<g:each in="${companyInstance?.employees}" var="userInstance">
    <li><g:link controller="user" action="show" id="${userInstance.id}">${userInstance?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="user" params="['company.id': companyInstance?.id]" action="create"><g:message code="user.new" default="New User" /></g:link>


                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="company.list" default="Company List" /></g:link></span>
                      <span class="menuButton"><g:link class="create" action="create"><g:message code="company.new" default="New Company" /></g:link></span>
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'update', 'default': 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
