<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="role.show" default="Role List"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id" titleKey="role.id"/>

                <g:sortableColumn property="name" title="Name" titleKey="role.name"/>

                <g:sortableColumn property="description" title="Description" titleKey="role.description"/>

                <th><g:message code="role.company" default="Company"/></th>

            </tr>
            </thead>
            <tbody>
            <g:each in="${roleInstanceList}" status="i" var="roleInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${roleInstance.id}">${fieldValue(bean: roleInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: roleInstance, field: "name")}</td>

                    <td>${fieldValue(bean: roleInstance, field: "description")}</td>

                    <td>${fieldValue(bean: roleInstance, field: "company")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <span class="menuButton"><g:link class="create" action="create"><g:message code="role.new"
                                                                                   default="New Role"/></g:link></span>
        <g:paginate total="${roleInstanceTotal}"/>
    </div>
</div>
</body>
</html>
