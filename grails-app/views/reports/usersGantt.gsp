<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="app.menu.administration.reports.usersGantt"/></title>
    <r:require modules="jquery-ganttview"/>
    <script type="text/javascript">
        $(function () {
            drawGantt();
        });

        function drawGantt() {
            $("#ganttChart").ganttView({
                dataUrl:'${createLink(controller: 'reports', action: 'usersGanttData')}?' + $('#usersGanttDataForm').serialize(),
                slideWidth:560,
                behavior:{
                    clickable:true,
                    draggable:false,
                    resizable:false,
                    onClick:function (data) {
                        var msg = "You clicked on an event: { start: " + data.start.toString("M/d/yyyy") + ", end: " + data.end.toString("M/d/yyyy") + " }";
                        //$("#eventMessage").text(msg);
                    },
                    onResize:function (data) {
                        var msg = "You resized an event: { id: " + data.id + ", name: " + data.name + ", start: " + data.start.toString("M/d/yyyy") + ", end: " + data.end.toString("M/d/yyyy") + " }";
                        //$("#eventMessage").text(msg);
                    },
                    onDrag:function (data) {
                        var msg = "You dragged an event: { start: " + data.start.toString("M/d/yyyy") + ", end: " + data.end.toString("M/d/yyyy") + " }";
                        //$("#eventMessage").text(msg);
                    }
                }
            });
        }

        function previousGantt() {
            updateGantt(-1);
        }

        function nextGantt() {
            updateGantt(1);
        }

        function updateGantt(dir) {
            var max = parseInt($('#max').val());
            var delta = max * dir;
            var start = parseInt($('#start').val());
            start += delta;
            $('#start').val(start);
            $("#ganttChart").html('');
            drawGantt();
        }

        function submitForm() {
            $('#start').val(0);
            $("#ganttChart").html('');
            drawGantt();
        }
    </script>
</head>

<body>

<div class="body">

    <div style="overflow: hidden; display: block; outline-color: -moz-use-text-color; outline-style: none; outline-width: 0px; height: auto; width: auto; position: relative;"
     class="ui-dialog ui-widget ui-widget-content ui-corner-all" tabindex="-1" role="dialog"
     aria-labelledby="ui-dialog-title-dialog">
    <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" unselectable="on"
         style="-moz-user-select: none;">
        <span class="ui-dialog-title" id="ui-dialog-title-dialog" unselectable="on"
              style="-moz-user-select: none;">Filter</span>
    </div>

    <div id="dialogFilter" title="Filter assignment" class="ui-dialog-content ui-widget-content"
         style="height: auto; width: auto;">
        <g:form url="[action: 'usersGanttData']" id="usersGanttDataForm" name="usersGanttDataForm" onsubmit="submitForm()">

            <g:hiddenField id="start" name="start" value="0"/>
            <g:hiddenField name="max" value="${max}"/>
            <fieldset>
                <label for="billing"><g:message code="billing" default="Billing"/>:</label>
                <g:select name="billing" from="${billings}" value="${billing}" optionKey="${{it.key}}" optionValue="${{it.value}}"/>

                <label for="mode"><g:message code="mode" default="Project Mode"/>:</label>
                <g:select name="mode" from="${modes}" value="${mode}" optionKey="id" multiple="true"/>

                <label for="skill"><g:message code="skill" default="Skills"/>:</label>
                <g:select name="skill" from="${skills}" value="${skill}" optionKey="id" multiple="true"/>

                <label for="startDate"><g:message code="project.startDate" default="Start Date"/>:</label>
                <jquery:datePicker name="startDate" value="${startDate}" default="none"/>

                <label for="endDate"><g:message code="project.endDate" default="End Date"/>:</label>
                <jquery:datePicker name="endDate" value="${endDate}" default="none"/>

            </fieldset>
        </g:form>
    </div>

    <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
        <button type="button" id="filterButton" onclick="submitForm()" class="ui-state-default ui-corner-all"><g:message code="filter"
                                                                                                  default="Filter"/></button>
    </div>

</div>

<div id="ganttChart"></div>
<br/><br/>
<div id="eventMessage"></div>
    <button type="button" id="previousButton" class="ui-state-default ui-corner-all" onclick="previousGantt()">
        <g:message code="previous" default="Previous"/>
    </button>
    <button type="button" id="nextButton" class="ui-state-default ui-corner-all" onclick="nextGantt()">
        <g:message code="next" default="Next"/>
    </button>

    </div>
</body>
</html>
