<%@ page contentType="text/html;charset=UTF-8" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>

    <style>

    th {
        background-repeat: no-repeat;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        background-size: cover;
        vertical-align: middle;
    }
    </style>

</head>

<body>
<div class="body">

    <h1 style="text-align: center; display: inline; margin:0px;"><g:message code="report.type"
                                                                            default="Week Report"/></h1>

    <h3 style="float: right ; margin:0px; display:inline;">${user?.account}</h3>

    <p>&nbsp;</p>

    <p>&nbsp;</p>


    <h1><span class="style7"><g:message code="report.workHours" default="Working Hours"/></span></h1>

    <p>&nbsp;</p>

    <g:form>
        <g:hiddenField name="id" value="${user?.id}"/>
        <table>
            <thead>
            <tr>

                <th><g:message code="assignment.project"></g:message></th>
                <th><g:message code="learning.date"></g:message></th>
                <th><g:message code="effort.comment"></g:message></th>
                <th><g:message code="effort.timeSpent"></g:message></th>
            </tr>
            </thead>
            <tbody>
                <g:each in="${workReport}">
                <tr class="prop even">
                    <td valign="top">${it.project}</td>
                    <td valign="top">${it.date.toString().split(" ")[0]}</td>
                    <td valign="top">${it.comment}</td>
                    <td valign="top">${it.timeSpent}</td>
                </tr>
            </g:each>

            </tbody>
        </table>

    </g:form>

    <p>&nbsp;</p>

    <p>&nbsp;</p>

    <h1><span class="style7"><g:message code="report.mood" default="Mood"/></span></h1>

    <p>&nbsp;</p>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:form>
        <g:hiddenField name="id" value="${user?.id}"/>
        <table>
            <thead>

            <th><g:message code="effort.date"></g:message></th>
            <th><g:message code="mood.value"></g:message></th>

            </thead>
            <tbody>

            <g:each in="${moodReport}">
                <tr class="prop even">
                    <td valign="top">${it.date.toString().split(" ")[0]}</td>
                    <td valign="top">${it.moodValue}</td>
                </tr>
            </g:each>
            </tbody>
        </table>

    </g:form>

    <p>&nbsp;</p>

    <p>&nbsp;</p>

    <h1><span class="style7"><g:message code="report.knowledgeAcquired" default="Acquired knowledge"/></span></h1>

    <p>&nbsp;</p>

    <g:form>
        <g:hiddenField name="id" value="${user?.id}"/>
        <table>
            <thead>
            <th><g:message code="learning.date"></g:message></th>
            <th><g:message code="learning.learning"></g:message></th>

            </thead>
            <tbody>
            <g:each in="${knowledgeReport}">
                <tr class="prop even">
                    <td valign="top">${it.date.toString().split(" ")[0]}</td>
                    <td valign="top">${it.knowledge.encodeAsSafeHTML()}</td>
                </tr>
            </g:each>
            </tbody>
        </table>

    </g:form>

    <p>&nbsp;</p>

    <p>&nbsp;</p>

    <h1><span class="style7"><g:message code="report.knowledgeVoted" default="Voted knowledges"/></span></h1>

    <p>&nbsp;</p>

    <g:form>
        <g:hiddenField name="id" value="${user?.id}"/>
        <table>
            <thead>

            <th><g:message code="learning.date"></g:message></th>
            <th><g:message code="learning.learning"></g:message></th>
            <th><g:message code="report.votedUser"></g:message></th>
            </thead>
            <tbody>
            <g:each in="${votedKnowledge}">
                <tr class="prop even">
                    <td valign="top">${it.date.toString().split(" ")[0]}</td>
                    <td valign="top">${it.knowledge}</td>
                    <td valign="top">${it.user}</td>
                </tr>
            </g:each>
            </tbody>
        </table>

    </g:form>

</div>
</body>
</html>
