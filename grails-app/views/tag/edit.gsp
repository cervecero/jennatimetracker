

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
        <div class="body">
          <h1><span class="style7"><g:message code="tag.show" default="Edit Tag" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${tagInstance}">
            <div class="errors">
                <g:renderErrors bean="${tagInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${tagInstance?.id}" />
                <g:hiddenField name="version" value="${tagInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="tag.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${fieldValue(bean: tagInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <%--
                                  ¿Porqué habría un usuario de editar un soundex?
                            --%>
                            <%--

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="soundex"><g:message code="tag.soundex" default="Soundex" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'soundex', 'errors')}">
                                    <g:textField name="soundex" value="${fieldValue(bean: tagInstance, field: 'soundex')}" />

                                </td>
                            </tr>
                            --%>


                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="category"><g:message code="tag.category" default="Category" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'category', 'errors')}">
                                    <g:select name="category.id" from="${TagCategory.list()}" optionKey="id" value="${tagInstance?.category?.id}"  />

                                </td>
                            </tr>

                            <%--
                                  User shouldn't be able to change a TAG company as it couldn't have selected the wrong company.
                            --%>
                            <%--
                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="company"><g:message code="tag.company" default="Company" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'company', 'errors')}">
                                    <g:select name="company.id" from="${Company.list()}" optionKey="id" value="${tagInstance?.company?.id}"  />

                                </td>
                            </tr>
                            --%>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="efforts"><g:message code="tag.efforts" default="Efforts" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'efforts', 'errors')}">


                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="tag.list" default="Tag List" /></g:link></span>
                      <span class="menuButton"><g:link class="create" action="create"><g:message code="tag.new" default="New Tag" /></g:link></span>
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'update', 'default': 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
