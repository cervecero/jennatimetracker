<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title><g:message code="app.menu.management.efforts"/></title>
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'fullcalendar.css')}" media="screen">
  <g:javascript src="jquery-ui/jquery.form.js"/>
  <g:javascript library="calendar" />
  <g:javascript library="qtip" />
  <script>
    var calStart;
    var calEnd;

    function reload() {
      moveCalendar(calStart, calEnd);
    }

    function moveCalendar(start, end) {
      $('#calendar').fullCalendar('refresh');
    }

    $(function() {
      $('#calendar').fullCalendar({
        buttons: {
          today: 'today',
          prevYear: true,
          prevMonth: true,
          nextMonth: true,
          nextYear: true
        },
        showTime: false,
        dayClick: function(dayDate) {
          alert(dayDate);
        },
        eventRender: function(calEvent, element) {
          var tipContent = "<strong>" + calEvent.assignmentList + "</strong><br/>" + calEvent.currentDate + " - " + calEvent.timeSpent + "<br/>" + "<blockquote>" + calEvent.comment + "</blockquote>";
          $(element).qtip({
            content: tipContent,
            position: {
              corner: {
                target: 'leftMiddle',
                tooltip: 'rightMiddle'
              }
            },
            border: {
              radius: 4,
              width: 3
            },
            style: {
              name: 'dark',
              tip: 'rightMiddle'
            }
          });
        },
        events: function(start, end, callback) {
          calStart = start;
          calEnd = end;
          $.ajax({
            url: "${createLink(action: 'calendar')}",
            global: true,
            type: "GET",
            data: ({start: start.getTime(), end: end.getTime(), userId: $('#userId').val()}),
            dataType: "json",
            success: function(response, statusText) {
              callback(response);
            }
          });
        }
      });
    });
  </script>
</head>
<body>
<div class="body">
<g:select name="userId" from="${users}" optionKey="id" value="${userId}" onchange="reload()" />
  <div id="calendar"></div>
</div>
</body>
</html>
