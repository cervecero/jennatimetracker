<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="technology.show" default="Technology List"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id" titleKey="technology.id"/>

                <g:sortableColumn property="name" title="Name" titleKey="technology.name"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${technologyInstanceList}" status="i" var="technologyInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${technologyInstance.id}">${fieldValue(bean: technologyInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: technologyInstance, field: "name")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <span class="menuButton"><g:link class="create" action="create"><g:message code="technology.new"
                                                                                   default="New Technology"/></g:link></span>
        <g:paginate total="${technologyInstanceTotal}"/>
    </div>
</div>
</body>
</html>
