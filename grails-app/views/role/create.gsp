<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="role.show" default="Create Role"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${roleInstance}">
        <div class="errors">
            <g:renderErrors bean="${roleInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form action="save" method="post">
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="role.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'name', 'errors')}">
                        <g:textField name="name" size="35" value="${fieldValue(bean: roleInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop odd">
                    <td valign="top" class="name">
                        <label for="description"><g:message code="role.description" default="Description"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'description', 'errors')}">
                        <g:textArea name="description" rows="5" cols="35"
                                    value="${fieldValue(bean: roleInstance, field: 'description')}"/>

                    </td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="company.id"><g:message code="role.company" default="Company"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'company', 'errors')}">
                        <g:select name="company.id" from="${availableCompanies}" optionKey="id"
                                  value="${roleInstance?.company?.id}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="role.list"
                                                                                   default="Role List"/></g:link></span>
            <span class="button"><g:submitButton name="create" class="save"
                                                 value="${message(code: 'create', 'default': 'Create')}"/></span>
        </div>
    </g:form>
</div>
</body>
</html>
