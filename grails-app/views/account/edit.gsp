<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="role.show" default="Edit Role"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${accountInstance}">
        <div class="errors">
            <g:renderErrors bean="${accountInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${accountInstance?.id}"/>
        <g:hiddenField name="version" value="${accountInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="account.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'name', 'errors')}">
                        <g:textField name="name" value="${fieldValue(bean: accountInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="country"><g:message code="account.country" default="Country"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'country', 'errors')}">
                        <g:textField name="country" value="${fieldValue(bean: accountInstance, field: 'country')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="clients"><g:message code="account.clients" default="Clients"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'clients', 'errors')}">

                        <ul>
                            <g:each in="${accountInstance?.clients}" var="clientInstance">
                                <li><g:link controller="client" action="show"
                                            id="${clientInstance.id}">${clientInstance?.encodeAsHTML()}</g:link></li>
                            </g:each>
                        </ul>
                        <g:link controller="client" params="['account.id': accountInstance?.id]"
                                action="create"><g:message code="client.new" default="New Client"/></g:link>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="razonSocial"><g:message code="account.razonSocial" default="Razon Social"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'razonSocial', 'errors')}">
                        <g:textField name="razonSocial"
                                     value="${fieldValue(bean: accountInstance, field: 'razonSocial')}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="account.list"
                                                                                                           default="Account List"/></g:link></span>
                                    <span class="menuButton"><g:link class="create" action="create"><g:message code="account.new"
                                                                                                               default="New Account"/></g:link></span>
            <span class="button"><g:actionSubmit class="save" action="update"
                                                 value="${message(code: 'update', 'default': 'Update')}"/></span>
            <span class="button"><g:actionSubmit class="delete" action="delete"
                                                 value="${message(code: 'delete', 'default': 'Delete')}"
                                                 onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/></span>
        </div>
    </g:form>
</div>
</body>
</html>
