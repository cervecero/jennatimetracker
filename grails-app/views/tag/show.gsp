

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
            <h1><span class="style7"><g:message code="tag.show" default="Show Tag" /></span></h1>
            <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${tagInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="tag.id" default="Id" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: tagInstance, field: "id")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="tag.name" default="Name" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: tagInstance, field: "name")}</td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="tag.soundex" default="Soundex" />:</td>

                                <td valign="top" class="value">${fieldValue(bean: tagInstance, field: "soundex")}</td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="tag.category" default="Category" />:</td>

                                <td valign="top" class="value"><g:link controller="tagCategory" action="show" id="${tagInstance?.category?.id}">${tagInstance?.category?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name"><g:message code="tag.company" default="Company" />:</td>

                                <td valign="top" class="value"><g:link controller="company" action="show" id="${tagInstance?.company?.id}">${tagInstance?.company?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name"><g:message code="tag.efforts" default="Efforts" />:</td>

                                <td  valign="top" style="text-align: left;" class="value">
                                    <ul>
                                    <g:each in="${tagInstance?.efforts}" var="effortInstance">
                                        <li><g:link controller="effort" action="show" id="${effortInstance.id}">${effortInstance.encodeAsHTML()}</g:link></li>
                                    </g:each>
                                    </ul>
                                </td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                  <span class="menuButton"><g:link class="list" action="list"><g:message code="tag.list" default="Tag List" /></g:link></span>
                  <span class="menuButton"><g:link class="create" action="create"><g:message code="tag.new" default="New Tag" /></g:link></span>
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
