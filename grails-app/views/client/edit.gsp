<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="client.edit" default="Edit Client"/></title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home"/></a>
    </span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="client.list"
                                                                           default="Client List"/></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="client.new"
                                                                               default="New Client"/></g:link></span>
</div>

<div class="body">
    <h1><g:message code="client.edit" default="Edit Client"/></h1>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${clientInstance}">
        <div class="errors">
            <g:renderErrors bean="${clientInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${clientInstance?.id}"/>
        <g:hiddenField name="version" value="${clientInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="client.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'name', 'errors')}">
                        <g:textArea name="name" rows="5" cols="40"
                                    value="${fieldValue(bean: clientInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="email"><g:message code="client.email" default="Email"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'email', 'errors')}">
                        <g:textArea name="email" rows="5" cols="40"
                                    value="${fieldValue(bean: clientInstance, field: 'email')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="lastName"><g:message code="client.lastName" default="Last Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'lastName', 'errors')}">
                        <g:textArea name="lastName" rows="5" cols="40"
                                    value="${fieldValue(bean: clientInstance, field: 'lastName')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="account"><g:message code="client.account" default="Account"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'account', 'errors')}">
                        <g:select name="account.id" from="${Account.list()}" optionKey="id"
                                  value="${clientInstance?.account?.id}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="birthday"><g:message code="client.birthday" default="Birthday"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'birthday', 'errors')}">
                        <g:datePicker name="birthday" value="${clientInstance?.birthday}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
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
