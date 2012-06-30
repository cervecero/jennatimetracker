<head>
    <meta name="layout" content="main"/>
</head>

<body>

<div class="body">
    <h1 style="color:white">User Registration</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${person}">
        <div class="errors">
            <g:renderErrors bean="${person}" as="list"/>
        </div>
    </g:hasErrors>

    <g:form action="save">
        <div class="dialog">
            <table>
                <tbody>

                <tr class='prop'>
                    <td valign='top' class='name' style="color:white"><label for='name'>Full Name:</label></td>
                    <td valign='top' class='value ${hasErrors(bean: person, field: 'name', 'errors')}'>
                        <input type="text" name='name' value="${person?.name?.encodeAsHTML()}"/>
                    </td>
                </tr>

                <tr class='prop'>
                    <td valign='top' class='name' style="color:white"><label for='password'>Password:</label></td>
                    <td valign='top' class='value ${hasErrors(bean: person, field: 'password', 'errors')}'>
                        <input type="password" name='password' value=""/>
                    </td>
                </tr>

                <tr class='prop'>
                    <td valign='top' class='name' style="color:white"><label for='repassword'>Confirm Password:</label>
                    </td>
                    <td valign='top' class='value ${hasErrors(bean: person, field: 'repassword', 'errors')}'>
                        <input type="password" name='repassword' value=""/>
                    </td>
                </tr>

                <tr class='prop'>
                    <td valign='top' class='name' style="color:white"><label for='account'>gMail:</label></td>
                    <td valign='top' class='value ${hasErrors(bean: person, field: 'account', 'errors')}'>
                        <input type="text" name='account' value="${person?.account?.encodeAsHTML()}"/>
                    </td>
                </tr>

                <tr class='prop'>
                    <td valign='top' class='name' style="color:white"><label for='companyName'>Company:</label></td>
                    <td valign='top' class='value ${hasErrors(bean: person, field: 'companyName', 'errors')}'>
                        <input type="text" name='companyName' value="${person?.companyName?.encodeAsHTML()}"/>
                    </td>
                </tr>

                <tr>
                    <td valign="top" class="name" style="color:white">
                        <label for="chatTime">Chat time:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: person, field: 'chatTime', 'errors')}">
                        <g:select name="chatTime"
                                  from="${availablesChatTime}"
                                  value="${person.chatTime ?: '17:00'}"/>
                    </td>
                </tr>

                <tr>
                    <td valign="top" class="name" style="color:white">
                        <label for="timeZone">Time Zone:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: person, field: 'timeZone', 'errors')}">
	                    <%--
	                    Would be nice to use:
	                        <g:timeZoneSelect name="timeZone" value="${person.timeZone?.getID()}" />
	                    However, http://jira.grails.org/browse/GRAILS-6590 prevents us from using it :(                    
	                    --%>
                        <g:select name="timeZone"
                                  from="${timeZones}"
                                  value="${person.timeZone?.getID()}"/>
                    </td>
                </tr>

                <tr>
                    <td valign="top" class="name" style="color:white">
                        <label for="timeZone">Locale:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: person, field: 'locale', 'errors')}">
                        <g:select name="locale"
                                  from="${locales}"
                                  value="${person.locale}"
                                  optionValue="displayName"/>
                    </td>
                </tr>

                <tr class='prop'>
                    <td valign='bottom' class='name' style="color:white"><label for='captcha'>Enter Code:</label></td>
                    <td valign='top' class='name'>
                        <input type="text" name="captcha" id="captcha" size="8"/>
                        <img src="${createLink(controller: 'captcha', action: 'index')}" align="absmiddle"/>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="formButton">
                <input class='save' type="submit" value="Create"></input>
            </span>
        </div>

    </g:form>
</div>
</body>
