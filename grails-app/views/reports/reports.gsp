<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>

      <table>
        <TR>
          <TH> Effort Id
          </TH>
          <TH> Project
          </TH>
          <TH> User
          </TH>
          <TH> Date
          </TH>
          <TH> Time Spent
          </TH>
          <TH> Role
          </TH>
        </TR>



       <g:each in="${reportRows}" status="i" var="row">
            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td>${row.getAt('effortId')}</td>
                <td>${row.getAt('project')}</td>
                <td>${row.getAt('user')}</td>
                <td><g:formatDate date="${row.getAt('date')}"  format="dd-MM-yyyy" /></td>
                <td>${row.getAt('timeSpent')}</td>
                <td>${row.getAt('role')}</td>


            </tr>
        </g:each>
      </table>

   
    </body>
</html>