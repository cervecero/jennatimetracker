<%@ page import="org.springframework.security.context.SecurityContextHolder; java.text.SimpleDateFormat" %>
<div id="calendar">
  <h1><span class="style7"><g:message code="myEfforts.title" args="${[fromDate]}"/></span></h1>
  <p>&nbsp;</p>
  <%
    User user = User.findByAccount(SecurityContextHolder.context.authentication.principal.username)
    Calendar fromCal = Calendar.getInstance(user.locale)
    fromCal.setTime(fromDate)
    int fdow = fromCal.firstDayOfWeek
    while (fromCal.get(Calendar.DAY_OF_WEEK) != fdow) {
      fromCal.add(Calendar.DAY_OF_MONTH, -1)
    }
    int ldow = fdow == Calendar.SUNDAY ? Calendar.SATURDAY : fdow - 1
    Calendar toCal = Calendar.getInstance(user.locale)
    toCal.setTime(toDate)
    while (toCal.get(Calendar.DAY_OF_WEEK) != ldow) {
      toCal.add(Calendar.DAY_OF_MONTH, 1)
    }
    Calendar cal = Calendar.getInstance(user.locale)
    cal.time = fromDate
    cal.add(Calendar.MONTH, -1)
  %>
  <div class="list">
    <a href="#" onclick="moveCalendar(${cal.get(Calendar.MONTH)}, ${cal.get(Calendar.YEAR)})"><g:formatDate date="${cal.time}" format="MMMM"/></a>
    &nbsp;|&nbsp;
    <%
      cal.add(Calendar.MONTH, 2)
    %>
    <a href="#" onclick="moveCalendar(${cal.get(Calendar.MONTH)}, ${cal.get(Calendar.YEAR)})"><g:formatDate date="${cal.time}" format="MMMM"/></a>
    <table class="cal">
      <thead>
      <tr>
        <td/>
        <g:each in="${(fromCal.time)..(fromCal.time + 6)}" var="date">
          <th width="14%"><g:formatDate date="${date}" format="EEEE"/></th>
        </g:each>
      </tr>
      </thead>
      <tbody>
      <g:set var="today" value="${new Date().clearTime()}" />
      <g:each in="${(fromCal.time)..(toCal.time)}" var="date" status="idx">
        <g:if test="${idx % 7 == 0}">
          <%
            cal.time = date
          %>
          <tr><td>${cal.get(Calendar.WEEK_OF_YEAR)}</td>
        </g:if>
        <g:render template="day" model="[actualMonth: fromDate.month, date: date, efforts: effortsByDate[(date)], editable: editable]"/>
        <g:if test="${idx % 7 == 6}">
          </tr>
        </g:if>
      </g:each>
      </tbody>
    </table>
  </div>
</div>
