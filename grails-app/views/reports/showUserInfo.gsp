<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <script type="text/javascript">
      function goBack(){
        history.back();
      }
    </script>
    <div class="body">
        <div class="dialog">
            <table>
                <tbody>

                    <tr class="prop even">
                        <td valign="top" class="name">
                            <g:message code="user.name" default="Name" />:
                        </td>
                        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                            ${fieldValue(bean: userInstance, field: 'name')}
                        </td>
                    </tr>

                    <tr class="prop even">
                        <td valign="top" class="name">
                          <g:message code="user.birthday" default="Birthday" />:
                        </td>
                        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'birthday', 'errors')}">
                          ${userInstance.getBirthday()}
                        </td>
                    </tr>

                    <tr class="prop odd">
                        <td valign="top" class="name">

                          <a href="http://www.gravatar.com">
                            <img src="${resource(dir:'images',file:'gravatar_icon.jpg')}" width="25" height="25">
                            <g:message code="user.avatar" default="Gravatar" />:
                          </a>
                        </td>
                        <td valign="top">
                          <span alt="lala">
                            <avatar:gravatar email="${userInstance.account}" size="75" />
                          </span>

                        </td>
                    </tr>

                    <tr class="prop even">
                        <td valign="top" class="name" colspan=2>

                          <table>
                            <tr class="prop odd">
                              <td valign="top" class="name">
                                <g:message code="user.points" default="Points" />:
                              </td>
                              <td valign="top" class="name">
                                  <h2 style="color:black;">${userInstance.getPoints()}</h2>
                              </td>
                              <td valign="top" class="name">
                                  <g:message code="user.historic.ranking" default="Historic Ranking" />:
                              </td>
                              <td valign="top" class="name">
                                  <h2 style="color:black;">${userRanking}/${totalUsers}</h2>
                              </td>
                            </tr>
                          </table>
                        </td>
                    </tr>

                    <tr class="prop odd">
                        <td valign="top" class="name">
                          <g:message code="user.assignments" default="Assignments" />:
                        </td>
                        <td valign="top">
                          <g:if test="${assignments?.size() > 0}">
                            <table>
                              <thead>
                                  <tr>
                                     <th><g:message code="assignment.project" default="Project" /></th>
                                     <th><g:message code="assignment.role" default="Role" /></th>
                                     <th><g:message code="assignment.startDate" default="Start Date" /></th>
                                     <th><g:message code="assignment.endDate" default="End Date" /></th>
                                  </tr>
                              </thead>

                              <g:each in="${assignments}" status="i" var="assignmentInstance">
                                  <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                      <td>${fieldValue(bean: assignmentInstance, field: "project")}</td>
                                      <td>${fieldValue(bean: assignmentInstance, field: "role")}</td>
                                      <td><g:formatDate date="${assignmentInstance.startDate}"  format="dd-MM-yyyy" /></td>
                                      <td><g:formatDate date="${assignmentInstance.endDate}"    format="dd-MM-yyyy"/></td>
                                  </tr>
                              </g:each>
                            </table>
                          </g:if>
                          <g:else>
                            <g:message code="reports.knowledge.show.user.info.no.assignments" default="User has no assignments"></g:message>
                          </g:else>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="buttons">
          <a onclick="goBack();"> Go back!</a>
        </div>

        </div>
    </body>
</html>
