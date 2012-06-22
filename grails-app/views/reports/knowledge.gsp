<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="app.menu.administration.reports.knowledge"/></title>
        <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery.jgrowl.css')}" media="screen">
        <g:javascript library="jgrowl" />
    </head>
    <body>
    <script type="text/javascript">
      function vote(id){
        $.ajax({
          type: "POST",
          url: "${createLink(action: 'ajaxVote')}",
          data: ({learningId: id}),
          dataType: "json",
          success: function(response, statusText) {
            $("#vote_"+id).html(response.points);
            $.jGrowl(response.message);
          },
          error: function(){
            alert("There was an error trying to process your vote.");
          }
        });
      }

    </script>

    <g:if test="${flash.message}">
      <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
    </g:if>

    <br>

    <fieldset title='<g:message code="reports.knowledge.learn" default="Learn" />'>
        <a class="ui-button ui-state-default ui-corner-all" href="${createLink(controller: 'reports',action:'newKnowledge')}" title="newLearning">
            <g:message code="reports.knowledge.new.learning"/>
        </a>
    </fieldset>
    <fieldset title='<g:message code="reports.knowledge.search" default="Search" />'>
        <g:form action="knowledge" method="GET">
            <g:textField name="search" id="search" value="${search}"/>
            <input type="submit" id="btnSearch" class="ui-button ui-state-default ui-corner-all" value="${message(code:"reports.knowledge.search")}" />
        </g:form>
    </fieldset>


      <table >

        <thead>
            <tr>
	            <g:sortableColumn property="user" title="User" titleKey="learning.user" params="${[search:search]}" />
	            <th><g:message code="learning.learning" default="Learning" style="width:80%"/></th>
	            <g:sortableColumn property="date" title="Date" titleKey="learning.date" params="${[search:search]}"/>
	            <g:sortableColumn property="points" title="Votes" titleKey="learning.points" params="${[search:search]}" style="width:5%"/>
            </tr>
        </thead>

       <g:each in="${learnings}" status="i" var="row">

            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
              <td>
                  <a href="mailto:${row.user?.account}">
                    <img class="alignleft" title="Send an email" src="${resource(dir:'images',file:'gmail.png')}" alt="" width="16" height="16">
                  </a> &nbsp;&nbsp;
                  <a href="${createLink(action: 'showUserInfo', id: row.user?.id)}" title="Show user's profile">
                    ${row.user?.name}
                  </a>
              </td>
              <td>
                <div id="learning_${row.id}">
                    <g:if test="${highlights[i]}">
                        <markdown:renderHtml text="${highlights[i].encodeAsSafeHTML()}"/>
                    </g:if>
                    <g:else>
                        <markdown:renderHtml text="${row.description?.encodeAsSafeHTML()}"/>
                    </g:else>
                    
                </div>
              </td>
              <td>
                <g:formatDate date="${row.date}"  format="dd-MM-yyyy" />
              </td>
              <td style="text-align:center">
                  <img style="cursor:pointer" class="alignleft" title="Vote" src="${resource(dir:'images',file:'ilikeit.png')}" alt="vote" onclick="javascript:vote(${row.id});" width="32" height="32">
                  <span id="vote_${row.id}">
                      ${row?.points}
                  </span>
              </td>
            </tr>
            
        </g:each>
      </table>

      <div class="paginateButtons">
          <g:paginate total="${totalLearnings}" params="[search: search]"/>
      </div>
    </body>
</html>