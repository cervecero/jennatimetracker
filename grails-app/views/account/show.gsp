<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="account.show" default="Show Account"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:form>
        <g:hiddenField name="id" value="${accountInstance?.id}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="account.id" default="Id"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: accountInstance, field: "id")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="account.name" default="Name"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: accountInstance, field: "name")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="account.country" default="Country"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: accountInstance, field: "country")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="account.clients" default="Clients"/>:</td>

                    <td valign="top" style="text-align: left;" class="value">
                        <ul>
                            <g:each in="${accountInstance?.clients}" var="clientInstance">
                                <g:if test="${!clientInstance.deleted}">
                                <li><g:link controller="client" action="show"
                                            id="${clientInstance.id}">${clientInstance.encodeAsHTML()}</g:link></li>
                                    </g:if>
                            </g:each>
                        </ul>
                    </td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="account.razonSocial" default="Razon Social"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: accountInstance, field: "razonSocial")}</td>

                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="account.list"
                                                                                                         default="Account List"/></g:link></span>
                                  <span class="menuButton"><g:link class="create" action="create"><g:message code="account.new"
                                                                                                             default="New Account"/></g:link></span>
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
