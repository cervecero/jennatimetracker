<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
     <h1><span class="style7"><g:message code="skill.show" default="Show Technology"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:form>
        <g:hiddenField name="id" value="${technologyInstance?.id}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="technology.id" default="Id"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: technologyInstance, field: "id")}</td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name"><g:message code="technology.name" default="Name"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: technologyInstance, field: "name")}</td>

                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="technology.list"
                                                                                                           default="Technology List"/></g:link></span>
                                    <span class="menuButton"><g:link class="create" action="create"><g:message code="technology.new"
                                                                                                               default="New Technology"/></g:link></span>
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
