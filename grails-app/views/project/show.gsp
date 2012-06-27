<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="app.title.project" args="${[projectInstance]}"/></title>
    <r:require modules="jquery-form"/>
</head>

<body>
<script>
    $(document).ready(function() {
        $('#assignment_endDate_datePicker').attr("readonly", "true");
        $('#assignment_startDate_datePicker').attr("readonly", "true");
    })
    $(document).ready(function() {
        var assignmentId = $("#assignment_id");
        var assignmentVersion = $("#assignment_version");
        var assignmentUserId = $("#assignment_user_id");
        var assignmentRoleId = $("#assignment_role_id");
        var assignmentDescription = $("#assignment_description");
        var assignmentStartDate = $("#assignment_startDate_datePicker");
        var assignmentEndDate = $("#assignment_endDate_datePicker");
        var assignmentActive = $("#assignment_active");
        var assignmentAllFields = $([]).add(assignmentId).add(assignmentVersion).add(assignmentUserId).add(assignmentRoleId).add(assignmentDescription).add(assignmentStartDate).add(assignmentEndDate).add(assignmentActive);
        var assignmentTips = $("#assignment_validateTips");

        function updateAssignmentTips(t) {
            assignmentTips.text(t).effect("highlight", {}, 1500);
        }

        function beforeAssignmentSubmit() {
            assignmentTips.text("");
            assignmentAllFields.removeClass('ui-state-error');
        }

        $('#assignmentForm').ajaxForm({
            beforeSubmit: beforeAssignmentSubmit,
            dataType: 'json',
            success: showAssignmentResponse
        });

        function showAssignmentResponse(response, statusText) {
            if (response.ok) {
                assignmentAllFields.val('');
                $("#assignmentDialog").dialog('close');
                showDialog(response, statusText);
                reloadAssignmentsList();
            } else {
                var message = "";
                for (var key in response.errors) {
                    $("#" + key).addClass("ui-state-error");
                    message += response.errors[key] + "\n\n";
                }
                updateAssignmentTips(message);
                showDialog(response, statusText);
            }
        }

        $('#btnCreateAssignment').click(
                function() {
                    $('#ui-dialog-title-assignmentDialog').text('<g:message code="assignment.create"/>');
                    $('#assignmentDialog').dialog('open');
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

        $("#assignmentDialog").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            modal: true,
            buttons: {
                '<g:message code="ok"/>': function() {
                    $("#assignmentForm").submit();
                },
                '<g:message code="cancel"/>': function() {
                    $(this).dialog('close');
                }
            },
            close: function() {
                assignmentAllFields.val('').removeClass('ui-state-error');
                assignmentTips.text('');
            }
        });

    });

    function editAssignment(id) {
        $('#ui-dialog-title-assignmentDialog').text('<g:message code="assignment.edit"/>');
        $.ajax({
            url: "${createLink(controller: 'assignment', action: 'ajaxEdit')}",
            global: true,
            type: "GET",
            data: ({id: id}),
            dataType: "json",
            success: function(response, statusText) {
                if (response.ok) {
                    $('#assignment_id').val(response.id);
                    $('#assignment_version').val(response.version);
                    $('#assignment_user_id').val(response.userId);
                    $('#assignment_role_id').val(response.roleId);
                    $('#assignment_description').val(response.description);
                    $('#assignment_startDate_datePicker').val(response.startDate);
                    $('#assignment_endDate_datePicker').val(response.endDate);
                    $('#assignment_active').attr('checked', response.active);
                    $("#assignmentDialog").dialog('open');

                } else {
                    showDialog(response, statusText)
                }
            }
        });
    }

    function reloadAssignmentsList() {
        $.ajax({
            url: "${createLink(action: 'ajaxAssignmentsList')}",
            data: {id: ${projectInstance.id}},
            async: false,
            success: function(resp) {
                $("#assignmentsList").html(resp);
            }
        });
    }
</script>

<div id="assignmentDialog">
    <p id="assignment_validateTips"/>
    <g:form controller="assignment" action="ajaxSave" method="post" name="assignmentForm">
        <g:hiddenField name="id" id="assignment_id"/>
        <g:hiddenField name="version" id="assignment_version"/>
        <g:hiddenField name="project.id" value="${projectInstance.id}" id="assignment_project_id"/>
        <fieldset>
            <label for="user.id"><g:message code="assignment.user"/>:</label>
            <g:select name="user.id" id="assignment_user_id" from="${users}" optionKey="id" optionValue="name"
                      value=""/>
            <label for="role.id"><g:message code="assignment.role"/>:</label>
            <g:select name="role.id" id="assignment_role_id" from="${roles}" optionKey="id" optionValue="name"
                      value=""/>
            <label for="description"><g:message code="assignment.description"/>:</label>
            <g:textArea name="description" id="assignment_description" rows="5" cols="40" value=""/>
            <label for="startDate"><g:message code="assignment.startDate"/>:</label>
            <jquery:datePicker name="startDate" id="assignment_startDate" value="" format="MM/dd/yyyy"
                               jsformat="mm/dd/yy"/>
            <label for="endDate"><g:message code="assignment.endDate"/>:</label>
            <jquery:datePicker name="endDate" id="assignment_endDate" value="" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>
            <label for="active"><g:message code="assignment.active" default="Active"/>:</label>
            <g:checkBox name="active" id="assignment_active"/>
        </fieldset>
    </g:form>
</div>

<div class="body">
    <h1><span class="style7"><g:message code="project.show" args="${ [projectInstance.name] }" /></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message">
            <g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}"/>
        </div>
    </g:if>

    <div id="projectTab">
        <table>
            <tbody style="color:black">
            <tr class="prop odd">
                <td valign="top" class="name"><g:message code="project.description"/>:</td>
                <td valign="top" class="value">${fieldValue(bean: projectInstance, field: "description")}</td>
            </tr>
            <tr class="prop even">
                <td valign="top" class="name"><g:message code="project.startDate"/>:</td>
                <td valign="top" class="value"><g:formatDate date="${projectInstance?.startDate}"
                                                             formatName='onlyDate.format'/></td>
            </tr>
            <tr class="prop even">
                <td valign="top" class="name"><g:message code="project.endDate"/>:</td>
                <td valign="top" class="value"><g:formatDate date="${projectInstance?.endDate}"
                                                             formatName='onlyDate.format'/></td>
            </tr>

            <tr class="prop odd">
                <td valign="top" class="name"><g:message code="project.mode" default="Mode"/>:</td>
                <td valign="top" class="value">${fieldValue(bean: projectInstance, field: "mode")}</td>
            </tr>

            <tr class="prop even">
                <td valign="top" class="name"><g:message code="project.technologies" default="Technologies"/>:</td>

                <td valign="top" style="text-align: left;" class="value">
                    <ul>
                        <g:each in="${projectInstance?.technologies}" var="technologiesInstance">
                            <g:if test="${!technologiesInstance.deleted}">
                                <li><g:link style="color: #000000;" controller="technology" action="show"
                                            id="${technologiesInstance.id}">${technologiesInstance.encodeAsHTML()}</g:link></li>
                            </g:if>
                        </g:each>
                    </ul>
                </td>

            </tr>
            </tbody>
        </table>
    </div>
    <br/>
    <h2><span><g:message code="project.property.assignment" default="Assignments"/></span></h2>
        
    <div id="assignmentsTab">
        <div id="assignmentsList">
            <g:render template="assignmentsList" model="[assignments: projectInstance.assignments]"/>
        </div>
        <button id="btnCreateAssignment" class="ui-button ui-state-default ui-corner-all">
            <g:message code="assignment.new"/>
        </button>
    </div>
</div>
</body>
</html>
