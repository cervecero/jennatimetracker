<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" lang="en-US">
<head profile="http://gmpg.org/xfn/11">
    <title><g:layoutTitle default="Project Guide" /></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="description" content="Forget about tracking time!">
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'style.css')}" media="screen">
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'jquery-ui-1.7.3.custom.css')}" media="screen">
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'fg.menu.css')}" media="screen">

  <g:javascript library="application" />
  <g:javascript library="jquery" />
  <g:javascript library="jquery_ui" />
  <g:javascript library="fgmenu" />
  <g:javascript library="query"/>

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

	.fg-button.ui-state-loading .ui-icon { background: url(spinner_bar.gif) no-repeat 0 0; }
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

    $("#confirmation").dialog({
        autoOpen: false,
        bgiframe: true,
        resizable: false,
        height:140,
        modal: true,
        buttons: {
            '<g:message code="ok"/>': function() {
                confirmOK = true;
                $(this).dialog('close');
            },
            '<g:message code="cancel"/>': function() {
                confirmOK = false;
                $(this).dialog('close');
            }
        },
        open: function() {
          confirmOK = false;
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

      function showConfirm(message) {
          $("#confirmText").text(message);
          $("#confirmation").dialog('open');
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
    <div id="header">
      <h1><a href="${resource(dir:'/',file:'')}" class="style8">Project Guide</a></h1>
      <p id="subHeading">Forget about tracking time!</p>
    </div>



    <g:isLoggedIn>
      <div id="mainDiv">



      <div style="float:left;" id="my_info_div">
       <a tabindex="0" href="#my-info" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="my-info-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.my.info"/></a>
       <div id="my-info" class="hidden">
         <ul>
             <li><a href="${createLink(controller: 'effort', action: 'myList')}" title="My Efforts"><g:message code="app.menu.my.efforts"/></a></li>
              <li><a href="${createLink(controller: 'profile', action: 'show')}" title="My Profile"><g:message code="app.menu.my.profile"/></a></li>
          </ul>
        </div>
      </div>

      <g:ifAnyGranted role="ROLE_PROJECT_LEADER">
        <div id="management_div" style="float: left;">
          <a tabindex="0" href="#management" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="management-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.management"/></a>
          <div id="management" class="hidden">
            <ul>
              <li><a href="${createLink(controller: 'assignment')}" title="Assignments"><g:message code="app.menu.management.assignments"/></a></li>
              <li><a href="${createLink(controller: 'effort', action: 'list')}" title="Efforts"><g:message code="app.menu.management.efforts"/></a></li>
              <li><a href="${createLink(controller: 'project')}" title="Projects"><g:message code="app.menu.management.projects"/></a></li>
              <li><a href="${createLink(controller: 'role')}" title="Roles"><g:message code="app.menu.management.roles"/></a></li>
              <li><a href="${createLink(controller: 'tag')}" title="Tags"><g:message code="app.menu.management.tags"/></a></li>
              <li><a href="${createLink(controller: 'skill')}" title="Skills"><g:message code="app.menu.management.skill"/></a></li>
              <li><a href="${createLink(controller: 'technology')}" title="Technology"><g:message code="app.menu.management.technology"/></a></li>
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
                <li><a href="${createLink(controller: 'reports',action:'knowledge')}" title="Knowledge"><g:message code="app.menu.administration.reports.knowledge"/></a></li>
                <li><a href="${createLink(controller: 'reports',action:'ranking')}" title="Knowledge Ranking"><g:message code="app.menu.administration.reports.knowledge.ranking"/></a></li>
                <li><a href="${createLink(controller: 'reports', action:'mood')}" title="Mood"><g:message code="app.menu.administration.reports.mood"/></a></li>
                <li><a href="${createLink(controller: 'reports', action:'usersGantt')}" title="Users' assignations "><g:message code="app.menu.administration.reports.usersGantt"/></a></li>
                <li><a href="${createLink(controller: 'user', action:'list')}" title="Users' Week Reports "><g:message code="app.menu.administration.reports.users"/></a></li>
                <li><a href="${createLink(controller: 'dashboard', action:'project')}" title="Users' Week Reports "><g:message code="app.menu.administration.reports.projects"/></a></li>
            </ul>
          </div>
        </div>
      </g:ifAnyGranted>

      <%-- <g:ifAnyGranted role="${Permission.ROLE_COMPANY_ADMIN}">--%>
      <g:ifAnyGranted role="ROLE_COMPANY_ADMIN,ROLE_PROJECT_LEADER">
       <div id="administration_div" style="float: left;">

        <a tabindex="0" href="#administration" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="administration-button"><span class="ui-icon ui-icon-triangle-1-s"></span><g:message code="app.menu.administration"/></a>
        <div id="administration" class="hidden">
        <ul>
          <g:ifAnyGranted role="ROLE_SYSTEM_ADMIN">
            <li><a href="${createLink(controller: 'company')}" title="Companies"><g:message code="app.menu.administration.companies"/></a></li>
          </g:ifAnyGranted>

          <g:ifAnyGranted role="ROLE_COMPANY_ADMIN">
            <li><a href="${createLink(controller: 'pendingUsers')}" title="Pending Users"><g:message code="app.menu.administration.pending.users"/></a></li>
            <li><a href="${createLink(controller: 'score')}" title="Users"><g:message code="app.menu.administration.scores"/></a></li>
            <li><a href="${createLink(controller: 'account')}" title="Account"><g:message code="app.menu.administration.accounts"/></a></li>
            <li><a href="${createLink(controller: 'client')}" title="Client"><g:message code="app.menu.administration.client"/></a></li>
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
    <p class="style1">Project Guide v<g:message code="${grailsApplication.metadata['app.version']}"/> is proudly powered by <a href="http://fdvsolutions.com/">FDV Solutions</a></p>
</div>
<div id="loading" title="<g:message code="app.loading"/>"/>
<div id="messageDialog" title="">
  <p>
    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
    <p id="messageText"></p>
  </p>
</div>
<div id="errorDialog" title="">
  <p>
    <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
    <p id="errorText"></p>
  </p>
</div>
<div id="confirmation" title="Confirmacion">
	<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
    <p id="confirmText">Esta seguro?</p>
</div>
</body>
</html>
