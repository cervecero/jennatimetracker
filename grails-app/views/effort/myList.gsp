<%@ page import="java.text.SimpleDateFormat" %>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'fullcalendar.css')}" media="screen">
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery.jgrowl.css')}" media="screen">

  <g:javascript src="jquery-ui/jquery.form.js"/>
  <g:javascript library="calendar" />
  <g:javascript library="qtip" />
  <g:javascript library="jgrowl" />

  <script type="text/javascript">
  var calStart;
  var calEnd;

  function reload() {
    moveCalendar(calStart, calEnd);
  }

  function moveCalendar(start, end) {
    $('#calendar').fullCalendar('refresh');
  }

  $(function() {
      var id = $("#id");
      var version = $("#version");
      var day = $("#date_day");
      var month = $("#date_month");
      var year = $("#date_year");
      var timeSpent = $("#timeSpent");
      var tags = $("#tags");
      var comment = $("#comment");
      var allFields = $([]).add(id).add(version).add(day).add(month).add(year).add(timeSpent).add(tags).add(comment);
      var tips = $("#validateTips");

      function updateTips(t) {
        tips.text(t).effect("highlight", {}, 1500);
      }

      function beforeSubmit() {
        $("#validateTips").text("");
        allFields.removeClass('ui-state-error');
      }

      $('#effortForm').ajaxForm({
        beforeSubmit: beforeSubmit,
        dataType: 'json',
        success: showResponse
      });

      function showResponse(response, statusText) {
        if (response.ok) {
          allFields.val('');
          $("#dialog").dialog('close');
          reload();
        } else {
          var message = "";
          for (var key in response.errors) {
            $("#" + key).addClass("ui-state-error");
            message += response.errors[key] + "\n\n";
          }
          updateTips(message);
        }
        showDialog(response, statusText);
      }

      $("#dialog").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 400,
        modal: true,
        buttons: {
          '<g:message code="ok"/>': function() {
            $("#effortForm").submit();
          },
          '<g:message code="cancel"/>': function() {
            $(this).dialog('close');
          },
           '<g:message code="delete"/>': function () {
            deleteIt($("#id").val());
          }
        },
        close: function() {
          allFields.val('').removeClass('ui-state-error');
          tips.text('');
        }
      });

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
        add(dayDate.getDate(), dayDate.getMonth() + 1, dayDate.getFullYear());
      },
      eventRender: function(calEvent, element) {
        var tipContent = "<strong>" + calEvent.currentDate + "</strong><br/>" + calEvent.timeSpent + "<br/>" + calEvent.assignmentList;
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
        $(element).bind('click', function(event) {
          edit(calEvent.id);
        });
      },
      events: function(start, end, callback) {
        calStart = start;
        calEnd = end;
        $.ajax({
          url: "${createLink(action: 'myCalendar')}",
          global: true,
          type: "GET",
          data: ({start: start.getTime(), end: end.getTime()}),
          dataType: "json",
          success: function(response, statusText) {
            callback(response);
          }
        });
      }
    });
    });

    function add(day, month, year) {
      $.ajax({
        url: "${createLink(controller: 'effort', action: 'ajaxGetAssignments')}",
        global: true,
        type: "GET",
        data: ({day: day, month: month, year: year}),
        dataType: "json",
        success: function(response, statusText) {
          if (response.ok) {
            $("#assignmentId").html(response.assignmentList);

            $('#ui-dialog-title-dialog').text('<g:message code="effort.create"/>');
            $('#date_day').val(day);
            $('#date_month').val(month);
            $('#date_year').val(year);
            $('#dialog').dialog('open');

          } else {
            alert("There are no assignments for the selected date.");
          }
        }
      });


    }

    function edit(id) {
      $('#ui-dialog-title-dialog').text('<g:message code="effort.edit"/>');
      $.ajax({
        url: "${createLink(action: 'ajaxEdit')}",
        global: true,
        type: "GET",
        data: ({id: id}),
        dataType: "json",
        success: function(response, statusText) {
          if (response.ok) {
            $("#id").val(response.id);
            $("#version").val(response.version);
            $("#date_day").val(response.day);
            $("#date_month").val(response.month);
            $("#date_year").val(response.year);
            $("#timeSpent").val(response.timeSpent);

            $("#assignmentId").html(response.assignmentList);

            $("#comment").val(response.comment);
            $("#dialog").dialog('open');
          } else {
            showDialog(response, statusText);
          }},
        error: function(){
          alert("ERROR");
        }
      });
    }


    function deleteIt(id) {
      if (id == null || "" == id){
        alert("<g:message code='delete.effort.not.deletable'/>");
        return;
      }
      var answer = confirm("<g:message code='delete.effort.confirm'/>");
      if (answer){
        $.ajax({
            url: "${createLink(action: 'ajaxDelete')}",
            global: true,
            type: "GET",
            data: ({id: id}),
            dataType: "json",
            success: function(response, statusText) {
              $("#dialog").dialog('close');
              reload();
              $.jGrowl(response.message);

            }
          });
      }
    }
  </script>
</head>
<body>
<div id="dialog">
  <p id="validateTips"/>
  <g:form action="ajaxSave" method="post" name="effortForm">
    <g:hiddenField name="id"/>
    <g:hiddenField name="version"/>
    <g:hiddenField name="date" value="struct"/>
    <g:hiddenField name="date_day"/>
    <g:hiddenField name="date_month"/>
    <g:hiddenField name="date_year"/>
    <fieldset>
      <label for="timeSpent"><g:message code="effort.timeSpent" default="Time spent"/>:</label>
      <g:textField name="timeSpent" maxlength="50" value=""/>

      <label for="assignmentId"><g:message code="effort.assignment" default="Assignment"/>:</label>
      <g:select from="${[]}" name="assignmentId" id="assignmentId"  />

      <label for="comment"><g:message code="effort.comment" default="Comment"/>:</label>
      <g:textArea name="comment" rows="3" cols="50" value=""/>
    </fieldset>
  </g:form>
</div>
<div class="body">
  <div id="calendar"></div>
</div>
</body>
</html>
