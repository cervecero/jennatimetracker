<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="body">
    <h1><span class="style7"><g:message code="user.show" default="Show User"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:form>
        <g:hiddenField name="id" value="${userInstance?.id}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop odd">
                    <td valign="top" class="name"><g:message code="user.name" default="Name"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: userInstance, field: "name")}</td>

                </tr>

                <tr class="prop even">
                    <td valign="top" class="name"><g:message code="user.account" default="gMail"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: userInstance, field: "account")}</td>

                </tr>

                <tr class="prop odd">
                    <td valign="top" class="name"><g:message code="user.chatTime" default="Chat Time"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: userInstance, field: "chatTime")}</td>

                </tr>

                <tr class="prop even">
                    <td valign="top" class="name"><g:message code="user.humour" default="Humour"/>:</td>

                    <td valign="top" class="value">${fieldValue(bean: userInstance, field: "humour")}</td>

                </tr>

                <tr class="prop odd">
                    <td valign="top" class="name"><g:message code="user.enabled" default="Enabled"/>:</td>

                    <td valign="top" class="value"><g:formatBoolean boolean="${userInstance?.enabled}"/></td>

                </tr>

                <tr class="prop even">
                    <td valign="top" class="name"><g:message code="user.locale" default="Language"/>:</td>

                    <td valign="top"
                        class="value">${userInstance?.locale?.getDisplayLanguage(userInstance?.locale)}</td>

                </tr>

                <tr class="prop odd">
                    <td valign="top" class="name"><g:message code="user.timeZone" default="Time Zone"/>:</td>

                    <td valign="top" class="value">${userInstance?.timeZone?.displayName}</td>

                </tr>
                <tr class="prop even">
                    <td valign="top" class="name"><g:message code="user.birthday" default="Birthday"/>:</td>

                    <td valign="top" class="value">
                        <g:formatDate date="${userInstance?.birthday}" format="MM-dd-yyyy"/>
                    </td>

                </tr>
                <tr class="prop odd">
                    <td valign="top" class="name"><g:message code="user.daily.working.hours"
                                                             default="Daily Working Hours"/>:</td>

                    <td valign="top" class="value">${userInstance?.dailyWorkingHours}</td>

                </tr>
                <tr class="prop odd">
                    <td valign="top" class="name"><g:message code="profile.skills" default="Skills"/>:</td>

                    <td valign="top" style="text-align: left;" class="value">
                        <ul>
                            <g:each in="${userInstance?.skills}" var="userSkill">
                                <g:if test="${!userSkill.deleted}">
                                <li><g:link controller="skill" action="show"
                                            id="${userSkill.id}">${userSkill.encodeAsHTML()}</g:link></li>
                                </g:if >
                            </g:each>
                        </ul>
                    </td>

                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="user.list"
                                                                                   default="User List"/></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="user.new"
                                                                                       default="New User"/></g:link></span>
            <span class="button"><g:actionSubmit class="edit" action="edit"
                                                 value="${message(code: 'edit', 'default': 'Edit')}"/></span>
            <%-- <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span> --%>
        </div>
    </g:form>
</div>
</body>
</html>
