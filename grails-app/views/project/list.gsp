<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="app.menu.management.projects"/></title>
    <g:javascript src="jquery-ui/jquery.form.js"/>
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery.jgrowl.css')}" media="screen">
    <g:javascript library="jgrowl"/>
</head>

<body>

<script>

    // TODO: Modify from library
    $(document).ready(function() {
        $("#startDateEdit_datePicker").attr("readonly", "true");
        $("#endDateEdit_datePicker").attr("readonly", "true");
        $("#filterButton").click(function() {
            $("#offset").val('0');
            $("#projectFormList").submit();
        });
    });

    function reloadAfterDelete() {
        $("#ajaxProjectDeleted").submit();
    }

    function deleteIt(id) {
        var answer = confirm("<g:message code='delete.project.confirm'/>");
        if (answer) {
            $.ajax({
                url: "${createLink(action: 'ajaxDelete')}",
                global: true,
                type: "GET",
                data: ({id: id}),
                dataType: "json",
                success: function(response, statusText) {
                    $("#dialogEdit").dialog('close');
                    $.jGrowl(response.message);
                    reloadAfterDelete();
                }
            });
        }
    }

    $(function() {
        var id = $("#id");
        var version = $("#version");
        var name = $("#name");
        var description = $("#description");
        var startDate = $("#startDate");
        var endDate = $("#endDate");
        var allFields = $([]).add(id).add(version).add(name).add(description).add(startDate).add(endDate);
        var tips = $("#validateTipsCreate");

        var idEdit = $("#idEdit");
        var versionEdit = $("#versionEdit");
        var nameEdit = $("#nameEdit");
        var descriptionEdit = $("#descriptionEdit");
        var startDateEdit = $("#startDateEdit");
        var endDateEdit = $("#endDateEdit");
        var allFieldsEdit = $([]).add(idEdit).add(versionEdit).add(nameEdit).add(descriptionEdit).add(startDateEdit).add(endDateEdit);
        var tipsEdit = $("#validateTipsEdit");

        function updateTips(t) {
            tips.text(t).effect("highlight", {}, 1500);
        }

        function updateTipsEdit(t) {
            tipsEdit.text(t).effect("highlight", {}, 1500);
        }

        function beforeSubmitEdit() {
            $("#validateTipsEdit").text("");
            allFieldsEdit.removeClass('ui-state-error');
        }

        function beforeSubmitCreate() {
            $("#validateTipsCreate").text("");
            allFields.removeClass('ui-state-error');
        }

        $('#projectFormEdit').ajaxForm({
            success: showResponseEdit,
            beforeSubmit: beforeSubmitEdit,
            dataType: 'json'
        });

        $('#projectFormCreate').ajaxForm({
            success: showResponseCreate,
            beforeSubmit: beforeSubmitCreate,
            dataType: 'json'
        });

        function showResponseEdit(response, statusText) {
            if (response.ok) {
                allFieldsEdit.val('');
                $("#dialogEdit").dialog('close');
            } else {
                var message = "";
                for (var key in response.errors) {
                    $("#" + key).addClass("ui-state-error");
                    message += response.errors[key] + "\n\n";
                }
                updateTipsEdit(message);
            }
            showDialog(response, statusText)
        }

        function showResponseCreate(response, statusText) {
            if (response.ok) {
                allFields.val('');
                $("#dialogCreate").dialog('close');
                reloadListado();
            } else {
                var message = "";
                for (var key in response.errors) {
                    $("#" + key).addClass("ui-state-error");
                    message += response.errors[key] + "\n\n";
                }
                updateTips(message);
            }
            showDialog(response, statusText)
        }

        $('#btnCreate').click(
                function() {
                    $('#ui-dialog-title-dialog').text('<g:message code="project.create"/>');
                    $('#dialogCreate').dialog('open');
                }).hover(
                function() {
                    $(this).addClass("ui-state-hover");
                },
                function() {
                    $(this).removeClass("ui-state-hover");
                }).mousedown(
                function() {
                    $(this).addClass("ui-state-active");
                }).mouseup(function() {
                    $(this).removeClass("ui-state-active");
                });

        function reloadListado() {
            $("#ajaxProjectCreated").submit();
        }

        $("#dialogEdit").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            modal: true,
            buttons: {
                '<g:message code="ok"/>': function() {
                    $("#projectFormEdit").submit();
                },
                '<g:message code="cancel"/>': function() {
                    $(this).dialog('close');
                },
                '<g:message code="delete"/>': function () {
                    deleteIt($("#idEdit").val());
                }
            },
            close: function() {
                allFieldsEdit.val('').removeClass('ui-state-error');
                tipsEdit.text('');
            }
        });

        $("#dialogCreate").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 550,
            modal: true,
            buttons: {
                '<g:message code="ok"/>': function() {
                    $("#projectFormCreate").submit();
                },
                '<g:message code="cancel"/>': function() {
                    $(this).dialog('close');
                }
            },
            close: function() {
                allFields.val('').removeClass('ui-state-error');
                tips.text('');
            }
        });

        $('#editProjectForm').ajaxForm({
            success: showEditResponse,
            dataType: 'json'
        });

        function showEditResponse(response, statusText) {
            if (response.okEdit) {
                $("#idEdit").val(response.idEdit);
                $("#versionEdit").val(response.versionEdit);
                $("#nameEdit").val(response.nameEdit);
                $("#descriptionEdit").val(response.descriptionEdit);
                $("#startDateEdit_datePicker").val(response.startDateEdit);
                $("#endDateEdit_datePicker").val(response.endDateEdit);

                var activeEdit = response.activeEdit;

                if (activeEdit == true) {
                    $("#activeEdit").attr("checked", "checked");
                } else {
                    $("#activeEdit").removeAttr("checked");
                }

                var billableEdit = response.billableEdit;

                if (billableEdit == true) {
                    $("#billableEdit").attr("checked", "checked");
                } else {
                    $("#billableEdit").removeAttr("checked");
                }

                $("#dialogEdit").dialog('open');
            } else {
            }
        }

        changeLinks();

    });

    function edit(id) {
        $('#ui-dialog-title-dialog').text('<g:message code="project.edit"/>');
        $("#editProjectId").val(id);
        $("#editProjectForm").submit();
    }

    function changeLinks() {
        $("a.step,a.prevLink,a.nextLink").each(function() {
            linkData = $(this).attr("href");
            newStr = "javascript:paginate('" + linkData + "');";
            $(this).attr("href", newStr);
        });
        $(".sortable > a").each(function() {
            linkData = $(this).attr("href");
            newStr = "javascript:paginate('" + linkData + "');";
            $(this).attr("href", newStr);
        });
    }

    function paginate(data) {
        $.query = $.query.load(data);
        $("#max2").val($.query.get('max'));
        $("#offset2").val($.query.get('offset'));
        $("#sort").val($.query.get('sort'));
        $("#order").val($.query.get('order'));
        $("#projectFormList2").submit();
    }

</script>

<g:form action="list" method="post" name="projectFormList2">
    <g:hiddenField name="offset" id="offset2" value="${params.offset ?: '0'}"/>
    <g:hiddenField name="max" id="max2" value="${params.max ?: '10'}"/>
    <g:hiddenField name="sort" value="${params.max ?: '10'}"/>
    <g:hiddenField name="order" value="${params.max ?: '10'}"/>
    <g:hiddenField name="project" value="${params.project}"/>
    <g:hiddenField name="startDate" value="${params.startDate}"/>
    <g:hiddenField name="endDate" value="${params.endDate}"/>
    <g:hiddenField name="ongoing" value="${params.ongoing}"/>
</g:form>

<g:form action="ajaxEdit" method="POST" name="editProjectForm">
    <g:hiddenField name="id" id="editProjectId"/>
</g:form>

<g:form action="ajaxProjectCreated" method="POST" name="ajaxProjectCreated">
</g:form>

<g:form action="ajaxProjectDeleted" method="POST" name="ajaxProjectDeleted">
</g:form>

<div id="dialogEdit" title="Edit Project">
    <p id="validateTipsEdit"/>
    <g:form action="ajaxUpdate" method="post" name="projectFormEdit">

        <g:hiddenField name="idEdit" id="idEdit"/>
        <g:hiddenField name="versionEdit"/>
        <fieldset>
            <label for="nameEdit"><g:message code="project.name" default="Name"/>:</label>
            <g:textField name="nameEdit" maxlength="50"/>

            <label for="descriptionEdit"><g:message code="project.description" default="Description"/>:</label>
            <g:textArea name="descriptionEdit" rows="3" cols="40"/>

            <label for="startDateEdit"><g:message code="project.startDate" default="Start Date"/>:</label>
            <jquery:datePicker name="startDateEdit" onclick="disableCalendarInput()" format="MM/dd/yyyy"
                               jsformat="mm/dd/yy"/>

            <label for="endDateEdit"><g:message code="project.endDate" default="End Date"/>:</label>
            <jquery:datePicker name="endDateEdit" onclick="disableCalendarInput()" format="MM/dd/yyyy"
                               jsformat="mm/dd/yy"/>

            <label for="activeEdit"><g:message code="projectActive" default="Project is active"/>:</label>
            <g:checkBox name="activeEdit"/>

            <label for="billable"><g:message code="projectBillable" default="Project is billable"/>:</label>
            <g:checkBox name="billableEdit"/>

        </fieldset>
    </g:form>
</div>


<div id="dialogCreate" title="Create Project">
    <p id="validateTipsCreate"/>
    <g:form action="ajaxSave" method="post" name="projectFormCreate">

        <g:hiddenField name="id"/>
        <g:hiddenField name="version"/>
        <fieldset>
            <label for="name"><g:message code="project.name" default="Name"/>:</label>
            <g:textField name="name" maxlength="50" value="${fieldValue(bean: projectInstance, field: 'name')}"/>

            <label for="description"><g:message code="project.description" default="Description"/>:</label>
            <g:textArea name="description" rows="3" cols="40" value="${fieldValue(bean: projectInstance, field: 'description')}"/>

            <label for="startDate"><g:message code="project.startDate" default="Start Date"/>:</label>
            <jquery:datePicker name="startDate" value="${projectInstance?.startDate}"/>

            <label for="endDate"><g:message code="project.endDate" default="End Date"/>:</label>
            <jquery:datePicker name="endDate" value="${projectInstance?.endDate}"/>

            <label for="teamLeader.id"><g:message code="project.teamLeader" default="Team Leader"/>:</label>
            <g:select name="teamLeader.id" from="${User.list()}" optionKey="id" value="${projectInstance?.teamLeader?.id}"/>

            <label for="mode.id"><g:message code="project.mode" default="Mode"/>:</label>
            <g:select name="mode.id" from="${Mode.list()}" optionKey="id" value="${projectInstance?.mode?.id}"/>

            <label for="technologies"><g:message code="project.technology" default="Technologies"/>:</label>
            <g:select name="technologies" multiple="yes" size="2" from="${Technology.list()}" optionKey="id" value="${projectInstance?.technologies}"/>

            <label for="active"><g:message code="projectActive" default="Project is active"/>:</label>
            <g:checkBox name="active" value="true"/>

            <label for="billable"><g:message code="projectBillable" default="Project is billable"/>:</label>
            <g:checkBox name="billable" value="true"/>

        </fieldset>
    </g:form>
</div>


<div class="body">
    <h1><span class="style7"><g:message code="project.show" default="Project List"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message">
            <g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}"/>
        </div>
    </g:if>

    <div style="overflow: hidden; display: block; outline-color: -moz-use-text-color; outline-style: none; outline-width: 0px; height: auto; width: auto;"
         class="ui-dialog ui-widget ui-widget-content ui-corner-all" tabindex="-1" role="dialog"
         aria-labelledby="ui-dialog-title-dialog">
        <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" unselectable="on"
             style="-moz-user-select: none;">
            <span class="ui-dialog-title" id="ui-dialog-title-dialog" unselectable="on"
                  style="-moz-user-select: none;">Filter</span>
        </div>

        <div id="dialogFilter" class="ui-dialog-content ui-widget-content">
            <g:form action="list" method="post" name="projectFormList">

                <g:hiddenField name="offset" value="${params.offset ?: '0'}"/>
                <g:hiddenField name="max" value="${params.max ?: '10'}"/>
                <fieldset>
                    <label for="project"><g:message code="project.name" default="Name"/>:</label>
                    <g:textField name="project" value="${project}"/>

                    <label for="startDate"><g:message code="project.startDate" default="Start Date"/>:</label>
                    <jquery:datePicker name="startDate" value="${startDate}" default="none"/>

                    <label for="endDate"><g:message code="project.endDate" default="End Date"/>:</label>
                    <jquery:datePicker name="endDate" value="${endDate}" default="none"/>

                    <label for="ongoing"><g:message code="project.ongoing" default="Ongoing"/>:</label>
                    <g:checkBox name="ongoing" value="${ongoing}"/>
                </fieldset>
            </g:form>
        </div>

        <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
            <button type="button" id="filterButton" class="ui-state-default ui-corner-all">
                <g:message code="filter" default="Filter"/>
            </button>
        </div>
    </div>

    <div id="list">
        <g:render template="list"/>
    </div>
    <button id="btnCreate" class="ui-button ui-state-default ui-corner-all">
        <g:message code="project.new" default="New Project"/>
    </button>
</div>
</body>
</html>
