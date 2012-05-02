<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>

<div class="body">
        <h1><span class="style7"><g:message code="client.show" default="Client List"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id" titleKey="client.id"/>

                <g:sortableColumn property="name" title="Name" titleKey="client.name"/>

                <g:sortableColumn property="email" title="Email" titleKey="client.email"/>

                <g:sortableColumn property="lastName" title="Last Name" titleKey="client.lastName"/>

                <th><g:message code="client.account" default="Account"/></th>

                <g:sortableColumn property="birthday" title="Birthday" titleKey="client.birthday"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${clientInstanceList}" status="i" var="clientInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${clientInstance.id}">${fieldValue(bean: clientInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: clientInstance, field: "name")}</td>

                    <td>${fieldValue(bean: clientInstance, field: "email")}</td>

                    <td>${fieldValue(bean: clientInstance, field: "lastName")}</td>

                    <td>${fieldValue(bean: clientInstance, field: "account")}</td>

                    <td><g:formatDate date="${clientInstance.birthday}"/></td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <span class="menuButton"><g:link class="create" action="create"><g:message code="client.new"
                                                                                                  default="New Client"/></g:link></span>

        <g:paginate total="${clientInstanceTotal}"/>
    </div>
</div>
</body>
</html>
