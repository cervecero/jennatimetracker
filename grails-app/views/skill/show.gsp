<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
     <h1><span class="style7"><g:message code="skill.show" default="Show Skill"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:form>
        <g:hiddenField name="id" value="${skillInstance?.id}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="skill.id" default="Id"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: skillInstance, field: "id")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="skill.technology" default="Technology"/>:</td>

                    <td valign="top" class="value"><g:link controller="technology" action="show"
                                                           id="${skillInstance?.technology?.id}">${skillInstance?.technology?.encodeAsHTML()}</g:link></td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="skill.level" default="Level"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: skillInstance, field: "level")}</td>

                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="skill.list"
                                                                                               default="Skill List"/></g:link></span>
                        <span class="menuButton"><g:link class="create" action="create"><g:message code="skill.new"
                                                                                                   default="New Skill"/></g:link></span>
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
