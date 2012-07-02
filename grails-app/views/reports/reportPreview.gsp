<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>

    <script type="text/javascript">
    </script>
    
      <div class="list">
      <table>
        <thead>
        <tr>
          <th>Proyecto
          </th>
          <th>Rol
          </th>
          <th>Usuario
          </th>
          <th>Fecha
          </th>
          <th>Horas
          </th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${report}" status="i" var="reportRow">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${fieldValue(bean: reportRow, field: "project")}</td>
            <td>${fieldValue(bean: reportRow, field: "role")}</td>
            <td>${fieldValue(bean: reportRow, field: "user")}</td>
            <td><g:formatDate date="${reportRow.date}" type="date" style="short"/></td>
            <td>${fieldValue(bean: reportRow, field: "timeSpent")}</td>
            <td>${fieldValue(bean: reportRow, field: "comment")}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${reportTotal}"/>
    </div>
  </body>
</html>