<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="app.title.project" args="${[projectInstance]}"/></title>
    <g:javascript src="jquery-ui/ui.core.js"/>
    <g:javascript src="jquery-ui/ui.tabs.js"/>
    <g:javascript src="jquery-ui/jquery.form.js"/>
</head>

<body>
<script>
    $(document).ready(function() {
        $('#assignment_endDate_datePicker').attr("readonly", "true");
        $('#assignment_startDate_datePicker').attr("readonly", "true");
    })
    $(document).ready(function() {
        var milestoneId = $("#milestone_id");
        var milestoneVersion = $("#milestone_version");
        var milestoneName = $("#milestone_name");
        var milestoneDescription = $("#milestone_description");
        var milestoneDueDate = $("#milestone_dueDate_datePicker");
        var milestoneAllFields = $([]).add(milestoneId).add(milestoneVersion).add(milestoneName).add(milestoneDescription).add(milestoneDueDate);
        var milestoneTips = $("#milestone_validateTips");

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

        <%--
        function updateMilestoneTips(t) {
          milestoneTips.text(t).effect("highlight", {}, 1500);
        }
        --%>
        function updateAssignmentTips(t) {
            assignmentTips.text(t).effect("highlight", {}, 1500);
        }

        <%--

        function beforeMilestoneSubmit() {
            milestoneTips.text("");
            milestoneAllFields.removeClass('ui-state-error');
        }
        --%>
        function beforeAssignmentSubmit() {
            assignmentTips.text("");
            assignmentAllFields.removeClass('ui-state-error');
        }

        <%--
        $('#milestoneForm').ajaxForm({
          beforeSubmit: beforeMilestoneSubmit,
          dataType: 'json',
          success: showMilestoneResponse
        });
        --%>
        $('#assignmentForm').ajaxForm({
            beforeSubmit: beforeAssignmentSubmit,
            dataType: 'json',
            success: showAssignmentResponse
        });
        <%--
        function showMilestoneResponse(response, statusText) {
          if (response.ok) {
            milestoneAllFields.val('');
            $("#milestoneDialog").dialog('close');
            showDialog(response, statusText);
            reloadMilestonesList();
          } else {
            var message = "";
            for (var key in response.errors) {
              $("#" + key).addClass("ui-state-error");
              message += response.errors[key] + "\n\n";
            }
            updateMilestoneTips(message);
            showDialog(response, statusText);
          }
        }
        --%>
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

        
        <%--
        $('#btnCreateMilestone').click(function() {
          $('#ui-dialog-title-milestoneDialog').text('<g:message code="milestone.create"/>');
          $('#milestoneDialog').dialog('open');
        }).hover(function() {
          $(this).addClass("ui-state-hover");
        },
                function() {
                  $(this).removeClass("ui-state-hover");
                }).mousedown(function() {
          $(this).addClass("ui-state-active");
        }).mouseup(function() {
          $(this).removeClass("ui-state-active");
        });
        --%>
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
        <%--
        $("#milestoneDialog").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            modal: true,
            buttons: {
                '<g:message code="ok"/>': function() {
                    $("#milestoneForm").submit();
                },
                '<g:message code="cancel"/>': function() {
                    $(this).dialog('close');
                }
            },
            close: function() {
                milestoneAllFields.val('').removeClass('ui-state-error');
                milestoneTips.text('');
            }
        });
        --%>
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
        $("#tabPanel").tabs();

    });
    <%--

    function editMilestone(id) {
      $('#ui-dialog-title-milestoneDialog').text('<g:message code="milestone.edit"/>');
      $.ajax({
        url: "${createLink(controller: 'milestone', action: 'ajaxEdit')}",
        global: true,
        type: "GET",
        data: ({id: id}),
        dataType: "json",
        success: function(response, statusText) {
          if (response.ok) {
              $('#milestone_id').val(response.id);
              $('#milestone_version').val(response.version);
              $('#milestone_name').val(response.name);
              $('#milestone_description').val(response.description);
              $('#milestone_dueDate_datePicker').val(response.dueDate);
              $("#milestoneDialog").dialog('open');
          } else {
            showDialog(response, statusText)
          }
        }
      });
    }
    --%>
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

    <%--
   function reloadMilestonesList() {
     $.ajax({
       url: "${createLink(action: 'ajaxMilestonesList')}",
       data: {id: ${projectInstance.id}},
       async: false,
       success: function(resp) {
         $("#milestonesList").html(resp);
       }
     });
   }
    --%>
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
<%--
<div id="milestoneDialog">
  <p id="milestone_validateTips"/>
  <g:form controller="milestone" action="ajaxSave" method="post" name="milestoneForm">
    <g:hiddenField name="id" id="milestone_id"/>
    <g:hiddenField name="version" id="milestone_version"/>
    <g:hiddenField name="project.id" value="${projectInstance.id}" id="milestone_project_id"/>
    <fieldset>
      <fieldset>
        <label for="name"><g:message code="milestone.name"/>:</label>
        <g:textField name="name" id="milestone_name" maxlength="50" value=""/>
        <label for="description"><g:message code="milestone.description" />:</label>
        <g:textField name="description" id="milestone_description" maxlength="50" value=""/>
        <label for="dueDate"><g:message code="milestone.dueDate" />:</label>
        <jquery:datePicker name="dueDate" id="milestone_dueDate" value=""/>
      </fieldset>
    </fieldset>
  </g:form>
</div>
--%>
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
    <h1><span class="style7"><g:message code="project.show" default="Show Project"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>

    <div id="tabPanel">
        <ul>
            <li><a href="#projectTab"><span><g:message code="project.show.title" default="View Project"/></span></a>
            </li>
            <%--
            <li><a href="#milestonesTabs"><span><g:message code="project.property.milestone" default="Milestones"/></span></a></li>
            --%>
            <li><a href="#assignmentsTab"><span><g:message code="project.property.assignment"
                                                           default="Assignments"/></span></a></li>
        </ul>

        <div id="projectTab">
            <table>
                <tbody style="color:black">
                <tr class="prop even">
                    <td valign="top" class="name"><g:message code="project.name"/>:</td>
                    <td valign="top" class="value">${fieldValue(bean: projectInstance, field: "name")}</td>
                </tr>
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
                    <td valign="top" class="name"><g:message code="project.account" default="Account"/>:</td>
                    <td valign="top" class="value">${fieldValue(bean: projectInstance, field: "account")}</td>
                </tr>

                <tr class="prop even">
                    <td valign="top" class="name"><g:message code="project.client" default="Client"/>:</td>
                    <td valign="top" class="value">${fieldValue(bean: projectInstance, field: "client")}</td>
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

                <!--<tr class="prop even">
                                <td valign="top" class="name"><g:message code="project.tags"/>:</td>
                              <td valign="top" class="value">${projectInstance.tags.join(', ')}</td>
                            </tr>-->
                </tbody>
            </table>
        </div>
        <%--

        <div id="milestonesTabs">
          <div id="milestonesList">
            <g:render template="milestonesList" model="[milestones: projectInstance.milestones]"/>
          </div>
          <button id="btnCreateMilestone" class="ui-button ui-state-default ui-corner-all"><g:message code="milestone.new" /></button>
        </div>
        --%>
        <div id="assignmentsTab">
            <div id="assignmentsList">
                <g:render template="assignmentsList" model="[assignments: projectInstance.assignments]"/>
            </div>
            <button id="btnCreateAssignment" class="ui-button ui-state-default ui-corner-all"><g:message
                    code="assignment.new"/></button>
        </div>
    </div>

</div>
</body>
</html>
