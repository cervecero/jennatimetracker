

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.management.assignments"/></title>
    </head>
    <body>
    <script type="text/javascript">
      function changeLinks() {
        $("a.step,a.prevLink,a.nextLink").each(function() {
          linkData = $(this).attr("href");
          newStr = "javascript:paginate('" + linkData + "');";
          $(this).attr("href", newStr);
        });
        $(".sortable > a").each(function() {
          linkData = $(this).attr("href");
          newStr = "javascript:paginate('" + linkData + "');";
          $(this).attr("href", newStr);
        });
      }

      function paginate(data) {
        $.query = $.query.load(data);
        $("#max2").val($.query.get('max'));
        $("#offset2").val($.query.get('offset'));
        $("#sort").val($.query.get('sort'));
        $("#order").val($.query.get('order'));
        $("#assignmentFormList2").submit();
      }

      $(document).ready(function() {
        $("#filterButton").click(function() {
          $("#offset").val('0');
          $("#assignmentFormList").submit();
        });
        changeLinks();
      });
    </script>
<g:form action="list" method="post" name="assignmentFormList2">
  <g:hiddenField name="offset" id="offset2" value="${params.offset ?: '0'}"/>
  <g:hiddenField name="max" id="max2" value="${params.max ?: '10'}"/>
  <g:hiddenField name="sort" value="${params.max ?: '10'}"/>
  <g:hiddenField name="order" value="${params.max ?: '10'}"/>
  <g:hiddenField name="project" value="${params.project}"/>
  <g:hiddenField name="user" value="${params.user}"/>
  <g:hiddenField name="startDate" value="${params.startDate}"/>
  <g:hiddenField name="endDate" value="${params.endDate}"/>
</g:form>
  <div class="body">
          <h1><span class="style7"><g:message code="assignment.show" default="Assignment List" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

      <div style="overflow: hidden; display: block; outline-color: -moz-use-text-color; outline-style: none; outline-width: 0px; height: auto; width: auto;"
              class="ui-dialog ui-widget ui-widget-content ui-corner-all" tabindex="-1" role="dialog"
              aria-labelledby="ui-dialog-title-dialog">
        <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" unselectable="on"
           style="-moz-user-select: none;">
        <span class="ui-dialog-title" id="ui-dialog-title-dialog" unselectable="on"
              style="-moz-user-select: none;">Filter</span>
      </div>

      <div id="dialogFilter" title="Filter assignment" class="ui-dialog-content ui-widget-content" style="height: auto; width: auto;">
        <g:form action="list" method="post" name="assignmentFormList">

          <g:hiddenField name="offset" value="${params.offset ?: '0'}"/>
          <g:hiddenField name="max" value="${params.max ?: '10'}"/>
          <fieldset>
              <label for="project"><g:message code="assignment.project" default="Project"/>:</label>
              <g:select name="project" from="${projectList}" optionKey="id" noSelection="['': '']" value="${project}"/>

              <label for="user"><g:message code="assignment.user" default="User"/>:</label>
              <g:select name="user" from="${userList}" optionKey="id" noSelection="['': '']" value="${user}"/>

            <label for="startDate"><g:message code="project.startDate" default="Start Date"/>:</label>
            <jquery:datePicker name="startDate" value="${startDate}" default="none"/>

            <label for="endDate"><g:message code="project.endDate" default="End Date"/>:</label>
            <jquery:datePicker name="endDate" value="${endDate}" default="none"/>

          </fieldset>
        </g:form>
      </div>

        <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
          <button type="button" id="filterButton" class="ui-state-default ui-corner-all"><g:message code="filter" default="Filter"/></button>
        </div>

      </div>

      <div class="list">
                <table>
                    <thead>
                        <tr>

                   	    <g:sortableColumn property="id" title="Id" titleKey="assignment.id" />

                   	    <th><g:message code="assignment.project" default="Project" /></th>

                   	    <th><g:message code="assignment.user" default="User" /></th>

                   	    <th><g:message code="assignment.role" default="Role" /></th>

                   	    <g:sortableColumn property="startDate" title="Start Date" titleKey="assignment.startDate" />

                   	    <g:sortableColumn property="endDate" title="End Date" titleKey="assignment.endDate" />

                   	    <g:sortableColumn property="active" title="Active" titleKey="assignment.active" />

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${assignmentInstanceList}" status="i" var="assignmentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="show" id="${assignmentInstance.id}">${fieldValue(bean: assignmentInstance, field: "id")}</g:link></td>

                            <td>${fieldValue(bean: assignmentInstance, field: "project")}</td>

                            <td>${fieldValue(bean: assignmentInstance, field: "user")}</td>

                            <td>${fieldValue(bean: assignmentInstance, field: "role")}</td>

                            <td><g:formatDate date="${assignmentInstance.startDate}"  format="dd-MM-yyyy" /></td>

                            <td><g:formatDate date="${assignmentInstance.endDate}"    format="dd-MM-yyyy"/></td>

                          <td>${fieldValue(bean: assignmentInstance, field: "active")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="assignment.new" default="New Assignment" /></g:link></span>
                <g:paginate total="${assignmentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
