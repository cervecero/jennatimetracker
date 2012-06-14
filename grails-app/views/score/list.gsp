<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.scores"/></title>
    </head>
    <body>
    <div class="body">
         <g:form>
          <h1>
            <span class="style7">
              <g:message code="role.show" default="Role List" />
            </span>
          </h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                          <th><g:message code="score.category" default="Category" /></th>

                          <th><g:message code="score.subcategory" default="Sub Category" /></th>

                          <th><g:message code="score.points" default="Points" /></th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${scoreCategories}" status="i" var="score">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td>${fieldValue(bean: score, field: "category")}</td>

                            <td>${fieldValue(bean: score, field: "subCategory")}</td>

                            <td><g:textField name="scores_${score.id}" value="${score.points}"/></td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>

           <div class="buttons">
              <span class="button"><g:actionSubmit class="update" action="update" value="${message(code: 'update', 'default': 'Update')}"  /></span>
          </div>
         </g:form>
        </div>
    </body>
</html>
