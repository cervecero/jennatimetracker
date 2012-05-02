<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>

    <script type="text/javascript">
     function createReport(excelReport){

        var startDate;
        var endDate;
        var project;
        var role;
        var user;
        var excel;

        // alert("Excel Report? --> "+excelReport);
        if (excelReport){
          excel = true;
        } else {
          excel = false;
        }

        startDate = $("#startDate_datePicker").val();
        endDate = $("#endDate_datePicker").val();
        project = $("#projectsCombo").val();
        role = $("#rolesCombo").val();
        user = $("#usersCombo").val();

        var startDateCB = $("#startDateCheckbox").attr("checked");
        var endDateCB = $("#endDateCheckbox").attr("checked");
        var projectsCB = $("#projectsCheckbox").attr("checked");
        var rolesCB = $("#rolesCheckbox").attr("checked");
        var usersCB = $("#usersCheckbox").attr("checked");

        var url = "createReport"
        var parameters= "";
        parameters+=addToParameter(parameters, "excel", excel);

        if (startDateCB && startDate)
          parameters+=addToParameter(parameters, "startDate", startDate)

        if (endDateCB && endDate)
          parameters+=addToParameter(parameters, "endDate", endDate)

        if (projectsCB && project)
          parameters+=addToParameter(parameters, "projectId", project)

        if (rolesCB && role)
          parameters+=addToParameter(parameters, "roleId", role)

        if (usersCB && user)
          parameters+=addToParameter(parameters, "userId", user)

        if ("" != parameters){
          url+=parameters
        }
      //  alert(url);

       window.open(url);

      }
       
     function addToParameter(parameter, parameterName, parameterValue){
       // alert("Parameter: "+parameter+"\nName: "+parameterName+"\nValue: "+parameterValue);
       if ("" == parameter)
        return "?"+parameterName+"="+parameterValue;
       else
        return "&"+parameterName+"="+parameterValue;

     }

     // TODO: Modificar desde la librería.
      $(document).ready(function(){
          $("#startDate_datePicker").attr("readonly", "true");
          $("#endDate_datePicker").attr("readonly", "true");

          $("#startDate_datePicker").val("");
          $("#endDate_datePicker").val("");
          $("#projectsCombo").val("");
          $("#rolesCombo").val("");
          $("#usersCombo").val("");

      });

      function projectUpdated(){

        var projectId = $("#projectsCombo").val();

        // alert("Project Id: "+projectId);

        updateUsersAndRoles(projectId);

      }

      function roleUpdated(){

        var projectId = $("#projectsCombo").val();
        var roleId = $("#rolesCombo").val();

        // alert("Project Id: "+projectId+" - Role Id: "+roleId);

        updateUsers();

      }

      function updateUsersAndRoles(projectId){
       $.ajax({
          type: "POST",
          url: "ajaxUpdateUsersAndRoles?projectId="+projectId,
          cache: false,
          dataType: "json",
          success: function(response, statusText)
          {
            reloadUsersCombo(response.usersData);
            reloadRolesCombo(response.rolesData);

          },
          error: function()
          {
              alert("There was an error gathering information.");
          }
        });
      }

      function updateUsers(){

        var project;
        var role;
        var url = "ajaxUpdateUsers";

        project = $("#projectsCombo").val();
        role = $("#rolesCombo").val();

        var projectsCB = $("#projectsCheckbox").attr("checked");
        var rolesCB = $("#rolesCheckbox").attr("checked");

        var parameters = ""

        if (projectsCB && project)
          parameters+=addToParameter(parameters, "projectId", project)

        if (rolesCB && role)
          parameters+=addToParameter(parameters, "roleId", role)

        if ("" != parameters){
          url+=parameters
        }

       //  alert("A punto de invocar: "+url);
        $.ajax({
          type: "POST",
          url: url,
          // data:formData,
          cache: false,
          dataType: "json",
          success: function(response, statusText)
          {
            reloadUsersCombo(response.usersData);
          },
          error: function()
          {
              alert("There was an error gathering information.");
          }
        });
      }

      function reloadUsersCombo(data)     {
        var options = '';

        $.each(data, function(i, user){
          options += '<option value="' + data[i].id+ '">' +  data[i].name+'</option>';
       });
       //  alert(options);

        $("#usersCombo").html(options);
      }

      function reloadRolesCombo(data)     {
        var options = '';

        $.each(data, function(i, user){
          options += '<option value="' + data[i].id+ '">' +  data[i].name+'</option>';
       });
        // alert(options);

        $("#rolesCombo").html(options);
      }

      function startDateCBUpdated(){
        var show = $("#startDateCheckbox").attr("checked");

        if (show){
          $("#startDateDiv").show();
        }
        else {
          $("#startDateDiv").hide();
          $("#startDate_datePicker").val("");
        }
      }

     function endDateCBUpdated(){
        var show = $("#endDateCheckbox").attr("checked");
        // alert("projectsCBUpdated: "+show);
        if (show){
          $("#endDateDiv").show();
        }
        else {
          $("#endDateDiv").hide();
          $("#endDate_datePicker").val("");
        }
      }

     function projectsCBUpdated(){
        var show = $("#projectsCheckbox").attr("checked");
        // alert("projectsCBUpdated: "+show);
        if (show){
          $("#projectsDiv").show();
        }
        else {
          $("#projectsDiv").hide();
          $("#projectsCombo").val("");
        }
        updateUsersAndRoles();
      }

      function rolesCBUpdated(){
        var show = $("#rolesCheckbox").attr("checked");

      //   alert("rolesCBUpdated: "+show);
        if (show)
          $("#rolesDiv").show();
        else
          $("#rolesDiv").hide();
          $("#rolesCombo").val("");
          roleUpdated();
      }



      function usersCBUpdated(){
        var show = $("#usersCheckbox").attr("checked");
   //      alert("usersCBUpdated: "+show);
        if (show)
          $("#usersDiv").show();
        else
          $("#usersDiv").hide();
          $("#usersCombo").val("");
      }
    </script>


    <g:form action="createReport" method="POST" name="createReportForm" id="createReportForm">

      <table>

      <th colspan="2">
          <g:message code="reports.filterBy" default="Filter by" />
      </th>

      <tr class="odd" style="width:400px;">
          <td valign="top">
              <g:checkBox name="startDateCheckbox" onclick="startDateCBUpdated()" />
              <g:message code="reports.startDate" default="Start Date" />
          </td>
          <td valign="top">
            <div id="startDateDiv" style="display:none;">
              <jquery:datePicker name="startDate" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>
            </div>
          </td>
      </tr>

      <tr class="even" style="width:400px;">
          <td valign="top" class="name">
              <g:checkBox name="endDateCheckbox" onclick="endDateCBUpdated()"/>
              <g:message code="reports.endDate" default="End Date" />
          </td>
          <td valign="top">
            <div id="endDateDiv" style="display:none;">
              <jquery:datePicker name="endDate" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>
            </div>
          </td>
      </tr>

      <tr class="odd" style="width:400px;">
          <td valign="top">
              <g:checkBox name="projectsCheckbox" onclick="projectsCBUpdated()"/> <g:message code="reports.filterByProject" default="Filter by project" />
          </td>
          <td valign="top">

            <div id="projectsDiv" style="display:none;">
              <g:select id="projectsCombo" name="projectId" from="${projects}" optionKey="id" value="${name}" onchange="projectUpdated()" />
            </div>
          </td>
      </tr>


      <tr class="even" style="width:400px;">
          <td valign="top">
              <g:checkBox name="rolesCheckbox" onclick="rolesCBUpdated()"/> <g:message code="reports.filterByRole" default="Filter by role" />
          </td>
          <td valign="top">

            <div id="rolesDiv"  style="display:none;">
               <g:select id="rolesCombo" name="roleId" from="${roles}" optionKey="id" value="${name}" onchange="roleUpdated()" />
             </div>
          </td>
      </tr>

      <tr class="odd" style="width:400px;">
          <td valign="top">
              <g:checkBox name="usersCheckbox" onclick="usersCBUpdated()"/> <g:message code="reports.filterByUser" default="Filter by user" />

          </td>
          <td valign="top">

            <div id="usersDiv" style="display:none;">
              <g:select id="usersCombo" name="userId" from="${users}" optionKey="id" value="${name}" />
            </div>
          </td>
      </tr>

      </table>
      <br>
      <g:message code='reports.generate'/>
      <div class="buttons">

          <span onclick="createReport(true);" style="cursor: pointer">
            <img class="alignleft" title="<g:message code='reports.generate.excel'/>" src="${resource(dir:'images',file:'icon_xls.png')}"  alt="" width="20" height="20" >
          </span>
          &nbsp;
          <span onclick="createReport(false);" style="cursor: pointer">
            <img class="alignleft" title="<g:message code='reports.generate.pdf'/>" src="${resource(dir:'images',file:'icon_pdf.png')}"  alt="" width="20" height="20" >
          </span>
      </div>

    </g:form>

    </body>
</html>