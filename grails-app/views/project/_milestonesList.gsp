<div class="list">
  <table>
    <thead>
    <tr>
      <th><g:message code="milestone.name"/></th>
      <th><g:message code="milestone.description"/></th>
      <th><g:message code="milestone.dueDate"/></th>
    </tr>
    </thead>
    <tbody  style="color:black">
    <g:each in="${milestones}" status="i" var="milestone">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td><a href="javascript:editMilestone(${milestone.id});">${fieldValue(bean: milestone, field: "name")}</a></td>
        <td>${fieldValue(bean: milestone, field: "description")}</td>
        <td><g:formatDate date="${milestone.dueDate}"/></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>
