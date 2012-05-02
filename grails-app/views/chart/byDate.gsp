<html>
    <head>
        <meta name="layout" content="main" />
        <g:javascript src="swfobject.js"/>
    </head>
    <body>
        <div class="body">
            <h1>Project time details</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
        </div>
          <g:form method="post" action="byDate">
                <div class="dialog">
                    <table id='filterTable'>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date">Date:</label>
                                </td>
                                <td valign="top" class="value">
                                  <jquery:datePicker name="date" value="${filter.date}" format="MM/dd/yyyy" jsformat="mm/dd/yy"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input type='submit' name="create" class="save" value="Filter" /></span>
                </div>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            </g:form>
      <ofchart:chart name="chart" url="${java.net.URLEncoder.encode(createLink(action:'byDateChartData', params: filter.toParamsMap()))}" width="800" height="400"/>
        <div id="chart">
            <h1>Alternative content</h1>
            <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
        </div>
      <ofchart:chart name="totalChart" url="${java.net.URLEncoder.encode(createLink(action:'totalByDateChartData', params: filter.toParamsMap()))}" width="800" height="400"/>
        <div id="totalChart">
            <h1>Alternative content</h1>
            <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
        </div>
    </body>
</html>