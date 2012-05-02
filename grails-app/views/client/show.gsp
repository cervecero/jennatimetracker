<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
        <h1><span class="style7"><g:message code="client.show" default="Show Client"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:form>
        <g:hiddenField name="id" value="${clientInstance?.id}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="client.id" default="Id"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: clientInstance, field: "id")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="client.name" default="Name"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: clientInstance, field: "name")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="client.email" default="Email"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: clientInstance, field: "email")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="client.lastName" default="Last Name"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: clientInstance, field: "lastName")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="client.account" default="Account"/>:</td>

                    <td valign="top" class="value"><g:link controller="account" action="show"
                                                           id="${clientInstance?.account?.id}">${clientInstance?.account?.encodeAsHTML()}</g:link></td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="client.birthday" default="Birthday"/>:</td>

                    <td valign="top" class="value"><g:formatDate date="${clientInstance?.birthday}"/></td>

                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="button"><g:actionSubmit class="edit" action="edit"
                                                 value="${message(code: 'edit', 'default': 'Edit')}"/></span>
            <span class="button"><g:actionSubmit class="delete" action="delete"
                                                 value="${message(code: 'delete', 'default': 'Delete')}"
                                                 onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/></span>
        </div>
    </g:form>
</div>
</body>
</html>
