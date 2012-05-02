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

<h1 style="text-align: center; display: inline; margin:0px;"><g:message code="report.effort"
                                                                        default=""/> ${project.name}</h1>

<p>&nbsp;</p>

<p>&nbsp;</p>

<h3 style="float: right ; margin:0px; display:inline;"><g:message code="report.generalMood"
                                                                  default=""/> ${avgMood}</h3>

<p>&nbsp;</p>

<p>&nbsp;</p>

<table>
    <thead>
    <tr>
        <th><g:message code="assignment.user"></g:message></th>
        <th><g:message code="assignment.role"></g:message></th>
        <th><g:message code="effort.timeSpent"></g:message></th>
    </tr>
    </thead>
    <tbody>

    <g:each in="${effortsGrouped}" var="effort">
        <tr class="prop even">
            <td>${effort.userName}</td>
            <td>${effort.roleName}</td>
            <td><div align="right"><g:formatNumber number="${effort.timeSpent}" format="#0.00"/></div></td>
        </tr>
    </g:each>
    </tbody>
</table>
<p>&nbsp;</p>

<p>&nbsp;</p>

<table>
    <thead>
    <tr>
        <th><g:message code="effort.date"></g:message></th>
        <th><g:message code="user.name"></g:message></th>
        <th><g:message code="assignment.role"></g:message></th>
        <th><g:message code="effort.timeSpent"></g:message></th>
        <th><g:message code="report.mood"></g:message></th>
        <th><g:message code="report.generalMood"></g:message></th>
        <th><g:message code="effort.comment"></g:message></th>
    </tr>
    </thead>
    <tbody>

    <g:each in="${efforts}" var="effort">
        <tr class="prop even">
            <td NOWRAP><g:formatDate date="${effort.date}" format="dd-MM-yyyy"/></td>
            <td>${effort.userName}</td>
            <td>${effort.roleName}</td>
            <td><div align="right"><g:formatNumber number="${effort.timeSpent}" format="#0.00"/></div></td>
            <td><div align="right"><g:formatNumber number="${effort.mood}" format="#"/></div></td>
            <td><div align="right"><g:formatNumber number="${effort.avgMood}" format="#0.00"/></div></td>
            <td>${effort.comment}</td>
        </tr>
    </g:each>
    </tbody>
</table>

<export:formats formats="['csv', 'excel', 'ods', 'pdf', 'rtf', 'xml']" params="${params}"/>

</body>
</html>
