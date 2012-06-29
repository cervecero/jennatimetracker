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
        <div class="message">${flash.message}</div>
    </g:if>

    <table>
        <tbody>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="skill.id" default="Id"/>:</td>
            <td valign="top" class="value">${skillInstance.id}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="skill.technology" default="Technology"/>:</td>
            <td valign="top" class="value">
                <g:link controller="technology" action="show" id="${skillInstance?.technology?.id}">
                    ${skillInstance?.technology?.encodeAsHTML()}
                </g:link>
            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="skill.level" default="Level"/>:</td>
            <td valign="top" class="value">${skillInstance.level}</td>
        </tr>

        </tbody>
    </table>

    <div class="buttons">
        <span class="menuButton">
            <g:link class="list" action="list"><g:message code="default.list"/></g:link>
        </span>
        <span class="menuButton">
            <g:link class="create" action="create"><g:message code="default.create"/></g:link>
        </span>
        <span class="button">
            <g:link class="edit" action="edit" id="${skillInstance.id}"><g:message code="default.edit"/></g:link>
        </span>
        <span class="button">
            <g:link class="delete" action="delete" id="${skillInstance.id}" onclick="return confirm('${message(code: 'default.confirm')}');">
                <g:message code="default.delete"/>
            </g:link>
        </span>
    </div>
</div>
</body>
</html>
