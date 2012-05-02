<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>

<div class="body">
    <h1><span class="style7"><g:message code="client.show" default="Create Client"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${clientInstance}">
        <div class="errors">
            <g:renderErrors bean="${clientInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form action="save" method="post">
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="client.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'name', 'errors')}">
                        <g:textField name="name" rows="5" cols="40"
                                     value="${fieldValue(bean: clientInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="email"><g:message code="client.email" default="Email"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'email', 'errors')}">
                        <g:textField name="email" rows="5" cols="40"
                                     value="${fieldValue(bean: clientInstance, field: 'email')}"/>

                    </td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="lastName"><g:message code="client.lastName" default="Last Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'lastName', 'errors')}">
                        <g:textField name="lastName" rows="5" cols="40"
                                     value="${fieldValue(bean: clientInstance, field: 'lastName')}"/>

                    </td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="account"><g:message code="client.account" default="Account"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'account', 'errors')}">
                        <g:select name="account.id" from="${Account.list()}" optionKey="id"
                                  value="${clientInstance?.account?.id}"/>

                    </td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="birthday"><g:message code="user.birthday" default="Birthday"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'birthday', 'errors')}">
                        <jquery:datePicker name="birthday" format="MM/dd/yyyy"
                                           value="${userInstance?.birthday}"/> (MM/dd/yyyy)
                    </td>
                </tr>

                <!--<tr class="prop">
                    <td valign="top" class="name">
                        <label for="birthday"><g:message code="client.birthday" default="Birthday"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'birthday', 'errors')}">
                        <g:datePicker name="birthday" value="${clientInstance?.birthday}"/>

                    </td>
                </tr>-->

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="client.list"
                                                                                   default="Client List"/></g:link></span>

            <span class="button"><g:submitButton name="create" class="save"
                                                 value="${message(code: 'create', 'default': 'Create')}"/></span>
        </div>
    </g:form>
</div>
</body>
</html>
