<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="account.show" default="Account List"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id" titleKey="account.id"/>

                <g:sortableColumn property="name" title="Name" titleKey="account.name"/>

                <g:sortableColumn property="country" title="Country" titleKey="account.country"/>

                <g:sortableColumn property="razonSocial" title="Razon Social" titleKey="account.razonSocial"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${accountInstanceList}" status="i" var="accountInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${accountInstance.id}">${fieldValue(bean: accountInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: accountInstance, field: "name")}</td>

                    <td>${fieldValue(bean: accountInstance, field: "country")}</td>

                    <td>${fieldValue(bean: accountInstance, field: "razonSocial")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <span class="menuButton"><g:link class="create" action="create"><g:message code="account.new"
                                                                                           default="New Account"/></g:link></span>
        <g:paginate total="${accountInstanceTotal}"/>
    </div>
</div>
</body>
</html>
