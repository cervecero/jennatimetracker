<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
<ul>
    <li>Proyectos activos: ${activeProjects}</li>
    <li>Horas trabajadas: ${timeSpent}</li>
    <li>Nuevos usuarios
        <ul>
            <g:each in="${newUsers}" var="user">
                <li>${user.name} ${user.joined}</li>
            </g:each>
        </ul>
    </li>
    <li>Nuevos proyectos
        <ul>
            <g:each in="${newProjects}" var="project">
                <li>${project.name} ${project.startDate}</li>
            </g:each>
        </ul>
    </li>
    <li>Humor en la última semana: ${weekMood}</li>
    <li>Humor general: ${mood}</li>
    <li>Horas trabajadas por proyecto
        <ul>
            <g:each in="${timeSpentByProject}" var="ts">
                <li>${ts.project.name}: ${ts.timeSpent}</li>
            </g:each>
        </ul>
    </li>
    <li>Nuevos conocimientos
        <ul>
            <g:each in="${newLearnings}" var="learning">
                <li>${learning.user} ${learning.date} ${learning.points} ${learning.description}</li>
            </g:each>
        </ul>
    </li>
    <li>Nuevas asignaciones
        <ul>
            <g:each in="${newAssignments}" var="assignment">
                <li>${assignment.user.name} ${assignment.project.name} ${assignment.role.name} ${assignment.startDate}</li>
            </g:each>
        </ul>
    </li>
    <li>Cumpleaños
        <ul>
            <g:each in="${birthdays}" var="user">
                <li>${user.name} ${user.birthday}</li>
            </g:each>
        </ul>
    </li>
    <li>Aniversarios
        <ul>
            <g:each in="${anniversaries}" var="user">
                <li>${user.name} ${user.joined}</li>
            </g:each>
        </ul>
    </li>
</ul>
</body>
</html>