<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>

    <g:if test="${flash.message}">
      <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
    </g:if>
    <br>
    <g:form method="post">

      <h2>
        <g:message code="reports.knowledge.new.learning"/>
      </h2>
      <br>

      <fieldset title='<g:message code="reports.knowledge.learning" default="Learning" />'>
        <table width="400">
          <tr>
            <td align="left">
                  <g:textArea cols="25" rows="10" name="learning" id="learning"/>
            </td>
          </tr>
        </table>
      </fieldset>

      <div class="buttons">
            <span class="button"><g:actionSubmit class="save" action="saveNewLearning" value="${message(code: 'reports.knowledge.save.new.learning', 'default': 'Save')}" /></span>
      </div>
    </g:form>
    </body>
</html>