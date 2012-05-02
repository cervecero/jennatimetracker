<html>
    <head>
        <meta name="layout" content="main" />
        <g:javascript src="swfobject.js"/>
        <g:javascript>
          function cloneTagRow() {
            var clonedRow = $( "#hiddenFilterTable tr:last" ).clone( true );
            $( "#filterTable" ).append(clonedRow);
            return false;
          }
        </g:javascript>
    </head>
    <body>
        <div class="body">
            <h1>Project time details</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
        </div>
          <g:form method="post" action="byUser">
                <div class="dialog">
                    <table id='filterTable'>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fromDate">From date:</label>
                                </td>
                                <td valign="top" class="value">
                                  <jquery:datePicker name="fromDate" value="${filter.fromDate}" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="toDate">To date:</label>
                                </td>
                                <td valign="top" class="value">
                                  <jquery:datePicker name="toDate" value="${filter.toDate}" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="user">User:</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="user" value="${filter.user ?: ''}"/>
                                </td>
                            </tr>
                        <g:each in="${filter.tags}" var="tag">
                          <tr class="prop">
                              <td valign="top" class="name">
                                  <label for="tags">Tag:</label>
                              </td>
                              <td valign="top" class="value">
                                  <g:textField name="tags" value="${ tag ?: ''}"/>
                              </td>
                          </tr>
                        </g:each>
                        </tbody>
                    </table>
                  <table id='hiddenFilterTable' style="visibility: hidden;">
                      <tbody>
                          <tr class="prop">
                              <td valign="top" class="name">
                                  <label for="tags">Tag:</label>
                              </td>
                              <td valign="top" class="value">
                                  <g:textField name="tags" value=""/>
                              </td>
                          </tr>
                    </tbody>
                    </table>
                  <button onclick="return cloneTagRow();">add tag</button>
                </div>
                <div class="buttons">
                    <span class="button"><input type='submit' name="create" class="save" value="Filter" /></span>
                </div>
            </g:form>
      <ofchart:chart name="chart" url="${java.net.URLEncoder.encode(createLink(action:'byUserChartData', params: filter.toParamsMap()))}" width="800" height="400"/>
        <div id="chart">
            <h1>Alternative content</h1>
            <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
        </div>
      <ofchart:chart name="totalChart" url="${java.net.URLEncoder.encode(createLink(action:'totalByUserChartData', params: filter.toParamsMap()))}" width="800" height="400"/>
        <div id="totalChart">
            <h1>Alternative content</h1>
            <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
        </div>
    </body>
</html>