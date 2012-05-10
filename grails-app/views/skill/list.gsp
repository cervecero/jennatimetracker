<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="skill.show" default="Skill List"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id" titleKey="skill.id"/>

                <th><g:message code="skill.technology" default="Technology"/></th>

                <g:sortableColumn property="level" title="Level" titleKey="skill.level"/>

                <g:sortableColumn property="addToMe" title="Add to Me" titleKey="skill.addtome"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${skillInstanceList}" status="i" var="skillInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td><g:link action="show"
                            id="${skillInstance.id}">${fieldValue(bean: skillInstance, field: "id")}</g:link></td>

                <td>${fieldValue(bean: skillInstance, field: "technology")}</td>

                <td>${fieldValue(bean: skillInstance, field: "level")}</td>
                <td>
                    <g:form>
                        <g:link action="addToMe" params="[id: skillInstance.id, offset: params.offset, max : params.max]">Add to me</g:link>
                    </g:form>
                </td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <span class="menuButton"><g:link class="create" action="create"><g:message code="skill.new"
                                                                                   default="New Skill"/></g:link></span>
        <g:paginate total="${skillInstanceTotal}"/>
    </div>
</div>
</body>
</html>
