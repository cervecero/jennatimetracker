<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.reports.projects"/></title>
    </head>
    <body>

     <g:if test="${flash.message}">
         <div id='messageDiv' class="message">
      <g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" />
         </div>
      </g:if>
     <g:else >
         <div id='messageDiv' class="message" style="display:none;">
         </div>
      </g:else>

    <script type="text/javascript">
      function search(){
        $("#dateStart").val($("#startDate_datePicker").val());
        $("#dateEnd").val($("#endDate_datePicker").val());


        if ($("#dateStart").val() == "" || $("#dateEnd").val() == "") {
            $('#messageDiv').html('<g:message code="reports.incompleteDate" />');
            $('#messageDiv').show();
        }else{
            $("#dashboardForm").submit();
        }
      }
    </script>

    <g:form controller="dashboard" action="projectFollowUp" method="POST" name="dashboardForm">
      <g:hiddenField name="dateStart" id="dateStart" />
      <g:hiddenField name="dateEnd" id="dateEnd" />

      <table>
        <th colspan="2">
            <g:message code="reports.filterBy" default="Filter by" />
        </th>
        <tr class="odd" style="width:400px;">
           <td valign="top" class="name">
              <g:message code="reports.startDate" default="End Date" />
           </td>
           <td valign="top">
              <div id="startDateDiv" >
                <jquery:datePicker name="startDate" format="MM/dd/yyyy" jsformat="mm/dd/yy"  value="${startDateValue}" />
              </div>
            </td>
        </tr>
        <tr class="even" style="width:400px;">
          <td valign="top" class="name">
            <g:message code="reports.endDate" default="End Date"  />
          </td>
          <td valign="top">
            <div id="endDateDiv" >
              <jquery:datePicker name="endDate" format="MM/dd/yyyy" jsformat="mm/dd/yy"  value="${endDateValue}" />
            </div>
          </td>
        </tr>
        <tr class="odd" style="width:400px;">
          <td valign="top" class="name">
            <g:message code="reports.project" default="Project"  />
          </td>
          <td valign="top">
              <g:select name="projectId" optionKey="id" from="${projects}"/>
          </td>
        </tr>
      </table>

      <div id="button_search" align="center">
        <input type="button" id="Search" name="Search" value="<g:message code='reports.search' default='Search' />" onclick="search();"/>
      </div>

    </g:form>
    </body>
</html>