<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<html xmlns="http://www.w3.org/1999/html">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery.jgrowl.css')}" media="screen">
        <g:javascript library="jgrowl" />
        <g:javascript library="qtip"/>
    </head>
    <body>
    <script type="text/javascript">
      function search(){
        $("#searchForm").submit();
      }

      function vote(id){
        //alert("Voto positivo para Learning Id: "+id);

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
        <a class="ui-button ui-state-default ui-corner-all" href="${createLink(controller: 'reports',action:'newKnowledge')}" title="newLearning"><g:message code="reports.knowledge.new.learning"/></a>

    </fieldset>
    <fieldset title='<g:message code="reports.knowledge.search" default="Search" />'>
      <table width="400">
        <tr>
          <td align="right">
              <g:form action="knowledge" method="POST" name="searchForm" id="searchForm">
                <g:textField name="search" id="search" value="${search}"/>
              </g:form>
          </td>
          <td align="left">
              <button id="btnSearch" class="ui-button ui-state-default ui-corner-all" onclick="search();">
                <g:message code="reports.knowledge.search" default="Search" />
              </button>
          </td>
        </tr>
      </table>
    </fieldset>


      <table >

        <thead>
            <tr>
	            <g:sortableColumn property="user" title="User" titleKey="learning.user" />
	            <th><g:message code="learning.learning" default="Learning" style="width:80%"/></th>
	            <g:sortableColumn property="date" title="Date" titleKey="learning.date" />
	            <g:sortableColumn property="points" title="Votes" titleKey="learning.points"  style="width:5%"/>
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
                <div style='overflow:hidden' id="learning_${row.id}">
                    <markdown:renderHtml text="${row?.descripcionConLinks?.toString()}"/>
                    <g:if test="${highlights[i]}">
                    <script type="text/javascript">
                        $('#learning_${row.id}').qtip({
                            content:"${highlights[i]}",
                            position:{
                                corner:{
                                    target:'topMiddle',
                                    tooltip:'bottomMiddle'
                                }
                            },
                            border:{
                                radius:4,
                                width:3
                            },
                            style:{
                                name:'dark',
                                tip:'bottomMiddle'
                            }
                        });
                    </script>
                    </g:if>
                </div>
              </td>
              <td>
                <g:formatDate date="${row.date}"  format="dd-MM-yyyy" />
              </td>
              <td >
                 <span class="menuButton">
                    <img class="alignleft" title="Vote" src="${resource(dir:'images',file:'ilikeit.png')}" alt="" onclick="javascript:vote(${row.id});" width="32" height="32">
                  </span>&nbsp;&nbsp;
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