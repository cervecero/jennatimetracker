

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="tag.show" default="Create Tag" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${tagInstance}">
            <div class="errors">
                <g:renderErrors bean="${tagInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
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

                            <%--  What does final user care about soundex?
                            --&>
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

                            <%--  There is no point on allowing to select a company if the only one user can choose is
                                  the one it owns.
                             --%>
                            <%--
                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="company"><g:message code="tag.company" default="Company" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'company', 'errors')}">
                                    <g:select name="company.id" from="${Company.list()}" optionKey="id" value="${tagInstance?.company?.id}"  />

                                </td>
                            <tr>
                            --%>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="tag.list" default="Tag List" /></g:link></span>
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
