<tr style="background-color: #000000;" class="prop even">
    <td valign="top" class="name">
        <label for="client"><g:message code="project.client"
                                       default="Client"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'client', 'errors')}">
        <g:select name="client.id" from="${clients}" optionKey="id" value="${projectInstance?.client?.id}"/>
    </td>
</tr>