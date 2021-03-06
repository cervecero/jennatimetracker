<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Jenna Timetracker - <g:layoutTitle default="${message(code:'app.tagline')}"/></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta name="description" content="Forget about tracking time!"/>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <r:require modules="core"/>
    <r:layoutResources/>

  <style type="text/css">
	body { font-size:62.5%; margin:0; padding:0; }
	#menuLog { font-size:1.4em; margin:20px; }
	.hidden { position:absolute; top:0; left:-9999px; width:1px; height:1px; overflow:hidden; }

	.fg-button { clear:left; margin:0 4px 40px 20px; padding: .4em 1em; text-decoration:none !important; cursor:pointer; position: relative; text-align: center; zoom: 1; }
	.fg-button .ui-icon { position: absolute; top: 50%; margin-top: -8px; left: 50%; margin-left: -8px; }
	a.fg-button { float:left;  }
	button.fg-button { width:auto; overflow:visible; } /* removes extra button width in IE */

	.fg-button-icon-left { padding-left: 2.1em; }
	.fg-button-icon-right { padding-right: 2.1em; }
	.fg-button-icon-left .ui-icon { right: auto; left: .2em; margin-left: 0; }
	.fg-button-icon-right .ui-icon { left: auto; right: .2em; margin-left: 0; }
	.fg-button-icon-solo { display:block; width:8px; text-indent: -9999px; }	 /* solo icon buttons must have block properties for the text-indent to work */
	</style>

  <script type="text/javascript">
  var confirmOK = false;
  var autocompleting = false;

  $(function() {

    $("#loading").dialog({
        autoOpen: false,
        bgiframe: true,
        modal: true,
        resizable: false,
        buttons: {}
    });

    $("#loading").ajaxStart(function() {
        if (!autocompleting) {
            $("#loading").dialog("open");
        }
    });

    $("#loading").ajaxStop(function() {
        if (!autocompleting) {
            $("#loading").dialog("close");
        }
    });

    $("#messageDialog").dialog({
        autoOpen: false,
        bgiframe: true,
        modal: true,
        buttons: {
            '<g:message code="ok"/>': function() {
                $(this).dialog('close');
            }
        }
    });

    $("#errorDialog").dialog({
        autoOpen: false,
        bgiframe: true,
        modal: true,
        buttons: {
            '<g:message code="ok"/>': function() {
                $(this).dialog('close');
            }
        }
    });
  });

  function showDialog(response, statusText) {
    if (response.ok) {
      $("#ui-dialog-title-messageDialog").text(response.title);
      $("#messageText").text(response.message);
      $("#messageDialog").dialog('open');
    } else if (response.message) {
      $("#ui-dialog-title-errorDialog").text(response.title);
      $("#errorText").text(response.message);
      $("#errorDialog").dialog('open');
    }
  }
  </script>

  <script type="text/javascript">
    $(function(){
    	// BUTTONS
    	$('.fg-button').hover(
    		function(){ $(this).removeClass('ui-state-default').addClass('ui-state-focus'); },
    		function(){ $(this).removeClass('ui-state-focus').addClass('ui-state-default'); }
    	);

        // MENUS: fgmenu es un workaround para jquery-ui.autocomplete
        $('#my-info-button').fgmenu({
              content: $('#my-info-button').next().html(), // grab content from this page
              showSpeed: 400
          });
          $('#management-button').fgmenu({
              content: $('#management-button').next().html(), // grab content from this page
              showSpeed: 400
          });
          $('#reports-button').fgmenu({
              content: $('#reports-button').next().html(), // grab content from this page
              showSpeed: 400
          });
          $('#administration-button').fgmenu({
              content: $('#administration-button').next().html(), // grab content from this page
              showSpeed: 400
          });

    });
    </script>
  <g:layoutHead />
</head>
<body>
<div id="main">
    <header id="header">
      <h1><a href="${resource(dir:'/',file:'')}" class="style8">Jenna Timetracker</a></h1>
      <p id="subHeading"><g:message code="app.tagline"/></p>
    </header>



    <g:isLoggedIn>
      <div id="mainDiv">



      <div style="float:left;" id="my_info_div">
       <a tabindex="0" href="#my-info" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="my-info-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.my.info"/></a>
       <div id="my-info" class="hidden">
         <ul>
             <li><a href="${createLink(controller: 'effort', action: 'myList')}"><g:message code="app.menu.my.efforts"/></a></li>
             <li><a href="${createLink(controller: 'home')}">Dashboard</a></li>
             <li><a href="${createLink(controller: 'profile', action: 'show')}"><g:message code="app.menu.my.profile"/></a></li>
          </ul>
        </div>
      </div>

      <g:ifAnyGranted role="ROLE_PROJECT_LEADER">
        <div id="management_div" style="float: left;">
          <a tabindex="0" href="#management" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="management-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.management"/></a>
          <div id="management" class="hidden">
            <ul>
              <li><a href="${createLink(controller: 'assignment')}"><g:message code="app.menu.management.assignments"/></a></li>
              <li><a href="${createLink(controller: 'effort', action: 'list')}"><g:message code="app.menu.management.efforts"/></a></li>
              <li><a href="${createLink(controller: 'project')}"><g:message code="app.menu.management.projects"/></a></li>
              <li><a href="${createLink(controller: 'role')}"><g:message code="app.menu.management.roles"/></a></li>
              <li><a href="${createLink(controller: 'tag')}"><g:message code="app.menu.management.tags"/></a></li>
              <li><a href="${createLink(controller: 'skill')}"><g:message code="app.menu.management.skill"/></a></li>
              <li><a href="${createLink(controller: 'technology')}"><g:message code="app.menu.management.technology"/></a></li>
            </ul>
          </div>
        </div>
      </g:ifAnyGranted>


       <g:ifAnyGranted role="ROLE_USER,ROLE_COMPANY_ADMIN,ROLE_PROJECT_LEADER">
        <div id="reports_div" style="float: left;">
          <a tabindex="0" href="#reports" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="reports-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.administration.reports"/></a>
          <div id="reports" class="hidden">
            <ul>
                <g:ifAnyGranted role="ROLE_SYSTEM_ADMIN,ROLE_PROJECT_LEADER">
                  <li><a href="${createLink(controller: 'reports')}" title="Reports"><g:message code="app.menu.administration.reports.time.spent"/></a></li>
                </g:ifAnyGranted>
                <li><a href="${createLink(controller: 'reports',action:'knowledge')}"><g:message code="app.menu.administration.reports.knowledge"/></a></li>
                <li><a href="${createLink(controller: 'reports',action:'ranking')}"><g:message code="app.menu.administration.reports.knowledge.ranking"/></a></li>
                <li><a href="${createLink(controller: 'reports', action:'mood')}"><g:message code="app.menu.administration.reports.mood"/></a></li>
                <li><a href="${createLink(controller: 'reports', action:'usersGantt')}"><g:message code="app.menu.administration.reports.usersGantt"/></a></li>
                <li><a href="${createLink(controller: 'user', action:'list')}"><g:message code="app.menu.administration.reports.users"/></a></li>
                <li><a href="${createLink(controller: 'dashboard', action:'project')}"><g:message code="app.menu.administration.reports.projects"/></a></li>
            </ul>
          </div>
        </div>
      </g:ifAnyGranted>

      <g:ifAnyGranted role="ROLE_COMPANY_ADMIN,ROLE_PROJECT_LEADER">
       <div id="administration_div" style="float: left;">

        <a tabindex="0" href="#administration" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="administration-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.administration"/></a>
        <div id="administration" class="hidden">
        <ul>
          <g:ifAnyGranted role="ROLE_SYSTEM_ADMIN">
            <li><a href="${createLink(controller: 'company')}"><g:message code="app.menu.administration.companies"/></a></li>
            <li><a href="${createLink(controller: 'quartz')}"><g:message code="app.menu.administration.scheduled.jobs"/></a></li>
          </g:ifAnyGranted>

          <g:ifAnyGranted role="ROLE_COMPANY_ADMIN">
            <li><a href="${createLink(controller: 'pendingUsers')}"><g:message code="app.menu.administration.pending.users"/></a></li>
            <li><a href="${createLink(controller: 'score')}"><g:message code="app.menu.administration.scores"/></a></li>
          </g:ifAnyGranted>

        </ul>
        </div>
        </div>
      </g:ifAnyGranted>



        <div id="logout_div" style="float: right;">
          <div><a href="${createLink(controller: 'logout')}" class="logout"></a></div>
        </div>

      </div>
    </g:isLoggedIn>
    <div class="wrapper">
        <g:isLoggedIn>
          <div id="content">
            <g:layoutBody />
          </div>
        </g:isLoggedIn>
        <g:isNotLoggedIn>
          <div id="otherContent">
            <g:layoutBody />
          </div>
        </g:isNotLoggedIn>

    </div>
</div>
<div id="footer">
    <p class="style1">Jenna Timetracker v<g:message code="${grailsApplication.metadata['app.version']}"/> <g:message code="app.powered"/></p>
</div>
<div id="loading" title="<g:message code="app.loading"/>"></div>
<div id="messageDialog" title="">
    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
    <p id="messageText"></p>
</div>
<div id="errorDialog" title="">
    <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
    <p id="errorText"></p>
</div>
<r:layoutResources/>
</body>
</html>
