

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <div class="body">
          <h1><span class="style7"><g:message code="user.show" default="Create User" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${userInstance}">
            <div class="errors">
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="user.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" size="35" value="${fieldValue(bean: userInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="account"><g:message code="user.account" default="Account" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'account', 'errors')}">
                                    <g:textField name="account" value="${fieldValue(bean: userInstance, field: 'account')}" />

                                </td>
                            </tr>
                        <%--
                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="chatTime"><g:message code="user.chatTime" default="Chat Time" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'chatTime', 'errors')}">
                                    <g:textField name="chatTime" value="${fieldValue(bean: userInstance, field: 'chatTime')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="localChatTime"><g:message code="user.localChatTime" default="Local Chat Time" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'localChatTime', 'errors')}">
                                    <g:textField name="localChatTime" value="${fieldValue(bean: userInstance, field: 'localChatTime')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="humour"><g:message code="user.humour" default="Humour" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'humour', 'errors')}">
                                    <g:textField name="humour" value="${fieldValue(bean: userInstance, field: 'humour')}" />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="enabled"><g:message code="user.enabled" default="Enabled" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'enabled', 'errors')}">
                                    <g:checkBox name="enabled" value="${userInstance?.enabled}" />

                                </td>
                            </tr>


                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="locale"><g:message code="user.locale" default="Locale" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'locale', 'errors')}">
                                    <g:localeSelect name="locale" value="${userInstance?.locale}"  />

                                </td>
                            </tr>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="timeZone"><g:message code="user.timeZone" default="Time Zone" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'timeZone', 'errors')}">
                                    <g:timeZoneSelect name="timeZone" value="${userInstance?.timeZone}"  />

                                </td>
                            </tr>
                        --%>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="user.list" default="User List" /></g:link></span>
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
