<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.reports.mood"/></title>
        <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery.jgrowl.css')}" media="screen">
        <g:javascript library="jgrowl" />
    </head>
    <body>

    <script type="text/javascript">
      function reloadGraphic(){
        $("#updateMoodReport").submit();  
      }
    </script>

    <FORM action="mood" method="POST" id="updateMoodReport">
      <table>
        <tr>
          <td><g:message code="reports.mood.select.month"/></td>
          <td>
            <g:select name="selectedMonth" from="${monthList}"  style="width:100px" />
          </td>
        </tr>

        <tr>
          <td><g:message code="reports.mood.select.year"/></td>  
          <td>
            <g:select name="selectedYear" from="${yearList}" style="width:100px" />
          </td>
        </tr>

        <g:ifAnyGranted role="ROLE_COMPANY_ADMIN,ROLE_PROJECT_LEADER">
          <tr>
            <td><g:message code="reports.mood.select.user"/></td>
            <td>
              <g:select name="selectedUser" from="${usersList}" optionKey="id" value="name" style="width:200px" /> 
            </td>
          </tr>
        </g:ifAnyGranted>



      </table>
    </FORM>

    <div class="buttons">
        <span class="button" onclick="reloadGraphic();" style="cursor: pointer">
          <g:message code="reports.reload.graphic" default="Reload Graphic" />
        </span>
    </div>

    <span>
      <img class="alignleft" title="Vote" src="/projectguide/${imagePath}" alt="" >
      <%--
      <img src="/${imagePath}" height="300" width="500" >                           --%>


    </span>


    </body>
</html>