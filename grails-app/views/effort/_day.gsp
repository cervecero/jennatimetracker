<g:set var="isActualMonth" value="${actualMonth == date.month}"/>
<td class="${ isActualMonth ? 'even' : 'odd' }${ date == today ? ' today' : '' }">
  <g:if test="${isActualMonth && editable}">
    <p class="ui-icon ui-icon-plus" onclick="add(${date.date}, ${date.month + 1}, ${date.year + 1900})" style="float:left;"/>
  </g:if>
  <p class="day-num">${date.date}</p>
  <g:each in="${efforts}" var="effort">
    <div class="effort tooltip">
    <g:if test="${editable}">
      <span class="ui-icon ui-icon-minus" onclick="deleteIt(${effort.id})" style="float:left;"></span>
      <span class="ui-icon ui-icon-zoomin" onclick="edit(${effort.id})"></span>
    </g:if>
    ${effort.timeSpent}hs: ${effort.tags.join(', ')}
    <g:if test="${effort.comment}">
      <span class="tip">${effort.comment}</span>
    </g:if>
    </div>
  </g:each>
</td>
