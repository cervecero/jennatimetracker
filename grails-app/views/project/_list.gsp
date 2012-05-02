<div class="list">
  <table>
    <thead>
    <tr>
      <th/>
      <g:sortableColumn property="name" title="Name" titleKey="project.name"/>
      <g:sortableColumn property="startDate" title="Start Date" titleKey="project.startDate"/>
      <g:sortableColumn property="endDate" title="End Date" titleKey="project.endDate"/>
    </tr>
    </thead>
    <tbody>
    <g:each in="${projectInstanceList}" status="i" var="projectInstance">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td>
          <a href="javascript:edit(${projectInstance.id});"><img src="${resource(dir: 'images', file: 'edit_16x16.png')}" border="0"/></a>
          <a href="${createLink(action: 'show', id: projectInstance.id)}"><img src="${resource(dir: 'images', file: 'lens_16x16.png')}" border="0"/></a>
        </td>
        <td>${fieldValue(bean: projectInstance, field: "name")}</td>
        <td><g:formatDate date="${projectInstance.startDate}" formatName='onlyDate.format'/></td>
        <td><g:formatDate date="${projectInstance.endDate}" formatName='onlyDate.format'/></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>
<div class="paginateButtons">
  <g:paginate total="${projectInstanceTotal}"/>
</div>
