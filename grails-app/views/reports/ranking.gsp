<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.reports.knowledge.ranking"/></title>
    </head>
    <body>

     <g:if test="${flash.message}">
         <div id='messageDiv' class="message">
            ${flash.message}
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
            $("#createReportRankingForm").submit();
        }
      }
    </script>


    <g:form action="ranking" method="POST" name="createReportRankingForm" id="createReportRankingForm">
      <g:hiddenField name="dateStart" id="dateStart" />
      <g:hiddenField name="dateEnd" id="dateEnd" />

      <h2>

      </h2>

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
      </table>


      <div id="button_search" align="center">
        <input type="button" id="Search" name="Search" value="<g:message code='reports.search' default='Search' />" onclick="search();"/>
      </div>

    </g:form>
      <br>
      <g:if test="${ranking?.size() > 0}">
        <h3><g:message code='reports.search.from' default='From' />: ${startDate} - <g:message code='reports.search.to' default='To' />: ${endDate} </h3>
        <%--  Here we show the ranking list content, if any. --%>
        <table>
          <TR>
            <TH> <g:message code="reports.ranking.position"/>
            </TH>
            <TH> <g:message code="reports.ranking.user"/>
            </TH>
            <TH> <g:message code="reports.ranking.score"/>: ${rankingTotal}
            </TH>
          </TR>
           <g:each in="${ranking}" status="i" var="row">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                <td>${i +1}</td>
                <td>${row.getAt('user')}</td>
                <td>${row.getAt('points')}</td>

              </tr>
            </g:each>
        </table>
      </g:if>

    </body>
</html>