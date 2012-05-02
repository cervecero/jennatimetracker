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
    <g:hasErrors bean="${roleInstance}">
        <div class="errors">
            <g:renderErrors bean="${roleInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${roleInstance?.id}"/>
        <g:hiddenField name="version" value="${roleInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="role.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'name', 'errors')}">
                        <g:textArea name="name" rows="5" cols="40"
                                    value="${fieldValue(bean: roleInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop odd">
                    <td valign="top" class="name">
                        <label for="description"><g:message code="role.description" default="Description"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'description', 'errors')}">
                        <g:textArea name="description" rows="5" cols="40"
                                    value="${fieldValue(bean: roleInstance, field: 'description')}"/>

                    </td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name">
                        <label for="company"><g:message code="role.company" default="Company"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'company', 'errors')}">
                        <g:select name="company.id" from="${Company.list()}" optionKey="id"
                                  value="${roleInstance?.company?.id}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="role.list"
                                                                                   default="Role List"/></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="role.new"
                                                                                       default="New Role"/></g:link></span>
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
