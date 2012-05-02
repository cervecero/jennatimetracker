<head>
<meta name='layout' content='main' />
</head>
<body>
<div class='body'>

<div style="align:center; overflow: hidden; display: block; z-index: 1002; outline-color: -moz-use-text-color; outline-style: none; outline-width: 0px; height: auto; width: 600px; " class="ui-dialog ui-widget ui-widget-content ui-corner-all" tabindex="-1" role="dialog" aria-labelledby="ui-dialog-title-dialog">


  <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" unselectable="on" style="-moz-user-select: none;">
    <span class="ui-dialog-title" id="ui-dialog-title-dialog" unselectable="on" style="-moz-user-select: none;">Congratulations!</span>
  </div>
  <div id="dialog" class="ui-dialog-content ui-widget-content" style="height: auto; min-height: 64px; width: auto;align: center;">
    Your registration has been successfully completed!<br><br>

    An email with an activation link has been sent to your account. <br><br>

    Please activate your account in order to use Jenna. <br><br>

    <input type="button" class="ui-state-default ui-corner-all" value="I have already activated my account and want to login!" onclick="login();">

  </div>
  <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
    <a title="Login" href="/jenna/"/>
  </div>
</form>
</div>
</div>

  <script type='text/javascript'>
    function login(){
      window.location = "../../projectguide";
    }
  </script>

</body>
