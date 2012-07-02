<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="app.title.profile" args="${[userInstance]}"/></title>
</head>

<body>
<div class="body">
<h1><span class="style7"><g:message code="user.show" default="Edit User"/></span></h1>

<p>&nbsp;</p>
<g:if test="${flash.message}">
    <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                    default="${flash.defaultMessage}"/></div>
</g:if>
<g:hasErrors bean="${userInstance}">
    <div class="errors">
        <g:renderErrors bean="${userInstance}" as="list"/>
    </div>
</g:hasErrors>
<g:form method="post">
    <g:hiddenField name="id" value="${userInstance?.id}"/>
    <g:hiddenField name="version" value="${userInstance?.version}"/>
    <div class="dialog">
        <table>
            <tbody>

            <tr class="prop even">
                <td valign="top" class="name">
                    <label for="name"><g:message code="user.name" default="Name"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                    <input type="text" name="name" value="${fieldValue(bean: userInstance, field: 'name')}"/>

                </td>
            </tr>


            <tr class="prop odd">
                <td valign="top" class="name">
                    <label for="name"><g:message code="user.profile.new.password" default="New Password"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                    <g:passwordField name="newpassword" maxlength="10"/><g:message
                            code="user.profile.new.password.warning"/>
                </td>
            </tr>

            <tr class="prop even">
                <td valign="top" class="name">
                    <label for="name"><g:message code="user.profile.new.repassword"
                                                 default="New Re-enter Password"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                    <g:passwordField name="newrepassword" maxlength="10"/>
                </td>
            </tr>

            <tr class="prop odd">
                <td valign="top" class="name">
                    <label for="chatTime"><g:message code="user.chatTime" default="Chat Time"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'chatTime', 'errors')}">
                    <g:select name="chatTime"
                              from="${availablesChatTime}"
                              value="${userInstance.chatTime}"/>
                </td>
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

            <%--
                <tr class="prop even">

                    <td valign="top" class="name">
                        <label for="humour"><g:message code="user.humour" default="Humour" />:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'humour', 'errors')}">
                        <g:textField name="humour" value="${fieldValue(bean: userInstance, field: 'humour')}" />

                    </td>
                </tr>

                <tr class="prop odd">

                    <td valign="top" class="name">
                        <label for="enabled"><g:message code="user.enabled" default="Enabled" />:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'enabled', 'errors')}">
                        <g:checkBox name="enabled" value="${userInstance?.enabled}" />

                    </td>
                </tr>
            --%>
            <tr class="prop even">
                <td valign="top" class="name">
                    <label for="locale"><g:message code="user.locale" default="Locale"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'locale', 'errors')}">
                    <g:select name="locale" from="${locales}"
                              value="${userInstance?.locale}"/>
                </td>
            </tr>

            <tr class="prop odd">
                <td valign="top" class="name">
                    <label for="timeZone"><g:message code="user.timeZone" default="Time Zone"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'timeZone', 'errors')}">
                    <g:timeZoneSelect name="timeZone" from="${timeZones}"
                                      value="${userInstance?.timeZone}"/>
                </td>
            </tr>

            <tr class="prop even">
                <td valign="top" class="name">
                    <label for="birthday"><g:message code="user.birthday" default="Birthday"/>:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'birthday', 'errors')}">
                    <jquery:datePicker name="birthday" format="MM/dd/yyyy"
                                       value="${userInstance?.birthday}"/> (MM/dd/yyyy)
                </td>
            </tr>

            <tr class="prop odd">
                <td valign="top" class="name">
                    <a href="http://www.gravatar.com">
                        <img src="${resource(dir: 'images', file: 'gravatar_icon.jpg')}" width="25" height="25">
                        <g:message code="user.avatar" default="Gravatar"/>:
                    </a>
                </td>
                <td valign="top">
                    <span alt="lala">
                        <avatar:gravatar email="${userInstance.account}" size="75"/>
                    </span>
                </td>
            </tr>

            <tr class="prop even">
                <td valign="top" class="name" colspan=2>

                    <table>
                        <tr class="prop odd">
                            <td valign="top" class="name">
                                <g:message code="user.historic.ranking" default="Historic Ranking"/>:
                            </td>
                            <td valign="top" class="name">
                                <h2 style="color:black;">${userRanking}/${totalUsers}</h2>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>


            <tr class="prop odd">
                <td valign="top" class="name">
                    <g:message code="user.assignments" default="Assignments"/>:
                </td>
                <td valign="top">
                    <g:if test="${assignments?.size() > 0}">
                        <table>
                            <thead>
                            <tr>
                                <th><g:message code="assignment.project" default="Project"/></th>
                                <th><g:message code="assignment.role" default="Role"/></th>
                                <th><g:message code="assignment.startDate" default="Start Date"/></th>
                                <th><g:message code="assignment.endDate" default="End Date"/></th>
                            </tr>
                            </thead>

                            <g:each in="${assignments}" status="i" var="assignmentInstance">
                                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                    <td>${fieldValue(bean: assignmentInstance, field: "project")}</td>
                                    <td>${fieldValue(bean: assignmentInstance, field: "role")}</td>
                                    <td><g:formatDate date="${assignmentInstance.startDate}" format="dd-MM-yyyy"/></td>
                                    <td><g:formatDate date="${assignmentInstance.endDate}" format="dd-MM-yyyy"/></td>
                                </tr>
                            </g:each>
                        </table>
                    </g:if>
                    <g:else>
                        <g:message code="user.no.assignments" default="No assignments"></g:message>
                    </g:else>
                </td>
            </tr>

            </tbody>
        </table>
    </div>

    <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update"
                                             value="${message(code: 'user.update', 'default': 'Update')}"/></span>
    </div>
</g:form>
</div>
</body>
</html>
