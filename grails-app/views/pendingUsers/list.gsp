 <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <title><g:message code="app.menu.administration.pending.users"/></title>
    <g:javascript src="jquery-ui/jquery.form.js" />
</head>

<body>
<script type="text/javascript">
	function sendInvitation(id){
	    $("#inviteId").val(id);
	    $("#sendInvitations").submit();
	};
	
	function dismiss(id){
	    $("#dismissId").val(id);
	    alert($("#dismissId").val());
	    $("#dismissRequest").submit();
	};
</script>

<div class="body">
    <h1>Requested Invitations</h1>
    <br>

    <g:form name="dismissRequest" action="dismiss" method="post">
        <g:hiddenField  name="dismissId" id="dismissId"/>
    </g:form>

    <g:form name="sendInvitations" action="invite" method="post">
        <g:hiddenField  name="inviteId" id="inviteId"/>
    </g:form>

    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <br>
    <g:if test="${requestedInvitations.size()> 0}">
        <div class="dialog">
            <table>
                <thead>
                    <tr>
                        <th><g:message code="invitation.requested.name"         default="Name" /></th>
                        <th><g:message code="invitation.requested.email"        default="Email" /></th>
                        <th><g:message code="invitation.requested.requested.on" default="Requested on" /></th>
                        <th><g:message code="invitation.requested.invited.on"   default="Invited on" /></th>
                        <th><g:message code="invitation.requested.invite"       default="Invite" /></th>
                        <th><g:message code="invitation.requested.dismiss"      default="Dismiss Request" /></th>
                    </tr>
                </thead>
                <tbody>
	                <g:each in="${requestedInvitations}" status="i" var="inviteMeInstance">
	                    <g:hiddenField name="invitationRequestId_${i}" id="invitationRequestId_${i}" value="${inviteMeInstance.id}"/>
	                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                        <td>${fieldValue(bean: inviteMeInstance, field: "name")}</td>
	                        <td>${fieldValue(bean: inviteMeInstance, field: "email")}</td>
	                        <td><g:formatDate date="${inviteMeInstance.requested}" /></td>
	                        <td><g:formatDate date="${inviteMeInstance.invited}" /></td>
	                        <td>
	                            <span class="menuButton">
	                                <img class="alignleft" title="Send" src="${resource(dir:'images',file:'sendInvitation.png')}" alt="" onclick="javascript:sendInvitation(${inviteMeInstance.id})" width="16" height="16">
	                            </span>
	                        </td>
	                        <td>
	                            <span class="menuButton">
	                                <img class="alignleft" title="Dismiss" src="${resource(dir:'images',file:'dismissInvitation.png')}"  onclick="javascript:dismiss(${inviteMeInstance.id})" alt="" width="16" height="16" >
	                            </span>
	                        </td>
	                    </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </g:if>
    <g:else>
        <h2><g:message code="invitation.requested.no.invitations" default="There are no requested invitations." /></h2>
    </g:else>
</div>
</body>
