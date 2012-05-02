

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
    <%--
    <script type="text/javascript">
      $(document).ready(function(){
         // $("#birthday_datePicker").attr("readonly", "true");
      });
    </script>
    --%>
        <div class="body">
          <h1><span class="style7"><g:message code="user.show" default="Edit User" /></span></h1>
          <p>&nbsp;</p>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${userInstance}">
            <div class="errors">
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${userInstance?.id}" />
                <g:hiddenField name="version" value="${userInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="user.name" default="Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="25" value="${fieldValue(bean: userInstance, field: 'name')}" />

                                </td>
                            </tr>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="chatTime"><g:message code="user.chatTime" default="Chat Time" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'chatTime', 'errors')}">
                                  <g:select name="chatTime"
                                      from="${availablesChatTime}"
                                          value="${userInstance.chatTime}"
                                      />
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
                                    <label for="locale"><g:message code="user.locale" default="Locale" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'locale', 'errors')}">
                                    <g:select name="locale"  from="${locales}"
                                            value="${userInstance?.locale}"  />
                                </td>
                            </tr>
                            <%--
                            <tr class="prop even">
                                <td valign="top" class="name">
                                    <label for="locale"><g:message code="user.locale" default="Locale" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'locale', 'errors')}">
                                    <g:localeSelect name="locale" from="${locales}"
                                            value="${userInstance?.locale}"  />

                                </td>
                            </tr>
                            --%>

                            <tr class="prop odd">
                                <td valign="top" class="name">
                                    <label for="timeZone"><g:message code="user.timeZone" default="Time Zone" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'timeZone', 'errors')}">
                                    <g:timeZoneSelect name="timeZone" value="${userInstance?.timeZone}"  />

                                </td>
                            </tr>
                            <tr class="prop even">
                                <td valign="top" class="name">
                                  <label for="birthday"><g:message code="user.birthday" default="Birthday" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'birthday', 'errors')}">
                                  <jquery:datePicker name="birthday" format="MM/dd/yyyy" value="${userInstance?.birthday}"/> (MM/dd/yyyy)
                                </td>
                            </tr>

                            <g:ifAnyGranted role="ROLE_PROJECT_LEADER,ROLE_COMPANY_ADMIN">
                              <tr class="prop odd">
                                  <td valign="top" class="name">
                                    <label for="dailyWorkingHours"><g:message code="user.daily.working.hours" default="Daily Working Hours" />:</label>
                                  </td>
                                  <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'dailyWorkingHours', 'errors')}">
                                    <g:textField name="dailyWorkingHours" maxlength="2" value="${fieldValue(bean: userInstance, field: 'dailyWorkingHours')}" />
                                  </td>
                              </tr>

                              <tr class="prop even">
                                  <td valign="top" class="name">
                                    <label for="skills"><g:message code="user.skills" default="Skills" />:</label>
                                  </td>
                                  <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'skills', 'errors')}">
                                    <g:select name="skills" from="${skills}" optionKey="id" value="${userInstance.skills*.id}" multiple="true"/>
                                  </td>
                              </tr>
                            </g:ifAnyGranted>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                      <span class="menuButton"><g:link class="list" action="list"><g:message code="user.list" default="User List" /></g:link></span>
                      <span class="menuButton"><g:link class="create" action="create"><g:message code="user.new" default="New User" /></g:link></span>
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'update', 'default': 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'delete', 'default': 'Delete')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
