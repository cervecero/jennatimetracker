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
    <g:hasErrors bean="${skillInstance}">
        <div class="errors">
            <g:renderErrors bean="${skillInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${skillInstance?.id}"/>
        <g:hiddenField name="version" value="${skillInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="technology"><g:message code="skill.technology" default="Technology"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: skillInstance, field: 'technology', 'errors')}">
                        <g:select name="technology.id" from="${Technology.list()}" optionKey="id"
                                  value="${skillInstance?.technology?.id}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="level"><g:message code="skill.level" default="Level"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: skillInstance, field: 'level', 'errors')}">
                        <g:textField name="level" value="${fieldValue(bean: skillInstance, field: 'level')}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="skill.list"
                                                                                               default="Skill List"/></g:link></span>
                        <span class="menuButton"><g:link class="create" action="create"><g:message code="skill.new"
                                                                                                   default="New Skill"/></g:link></span>

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
