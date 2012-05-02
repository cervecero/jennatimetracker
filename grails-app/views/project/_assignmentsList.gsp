<div class="list">
  <table>
    <thead>
    <tr>
      <th><g:message code="assignment.user"/></th>
      <th><g:message code="assignment.role"/></th>
      <th><g:message code="assignment.startDate"/></th>
      <th><g:message code="assignment.endDate"/></th>
    </tr>
    </thead>
    <tbody  style="color:black">
    <g:each in="${assignments}" status="i" var="assignment">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td  style="color:black"><a href="javascript:editAssignment(${assignment.id});" style="color:black">${fieldValue(bean: assignment, field: "user.name")}</a></td>
        <td style="color:black">${fieldValue(bean: assignment, field: "role.name")}</td>
        <td style="color:black"><g:formatDate date="${assignment.startDate}" formatName="onlyDate.format"/></td>
        <td style="color:black"><g:formatDate date="${assignment.endDate}" formatName="onlyDate.format"/></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>
