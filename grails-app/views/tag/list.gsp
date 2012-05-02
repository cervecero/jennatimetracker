

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="tag.show" default="Tag List" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>

                   	    <g:sortableColumn property="id" title="Id" titleKey="tag.id" />

                   	    <g:sortableColumn property="name" title="Name" titleKey="tag.name" />

                   	    <g:sortableColumn property="soundex" title="Soundex" titleKey="tag.soundex" />

                   	    <th><g:message code="tag.category" default="Category" /></th>

                   	    <th><g:message code="tag.company" default="Company" /></th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${tagInstanceList}" status="i" var="tagInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="show" id="${tagInstance.id}">${fieldValue(bean: tagInstance, field: "id")}</g:link></td>

                            <td>${fieldValue(bean: tagInstance, field: "name")}</td>

                            <td>${fieldValue(bean: tagInstance, field: "soundex")}</td>

                            <td>${fieldValue(bean: tagInstance, field: "category")}</td>

                            <td>${fieldValue(bean: tagInstance, field: "company")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="tag.new" default="New Tag" /></g:link></span>
                <g:paginate total="${tagInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
