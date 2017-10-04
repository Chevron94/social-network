<%@ page import="socialnetwork.entities.User" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: roman
  Date: 10/4/15
  Time: 5:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User user = (User)request.getAttribute("user"); %>
<% List<User> friends = (List<User>)request.getAttribute("friends");%>
<html>
<head>
  <title id="title">Dialogs</title>
  <script type="text/javascript">
    var dialogStart = 0;
  </script>

  <%@include file="templates/scripts.jsp"%>

</head>
<body onload="pageLoad()">
<br>
<%@include file="templates/header.jsp"%>
<!-- /container -->
<input type="hidden" class="input-block-level" id="nickname" value="<%=user.getLogin()%>">
<input type="hidden" id="dialogId" value="0">
<div class="row table-fixed table-bordered" style="margin: 1%">
  <div class="col-xs-3" style="height: 525px; overflow-y: scroll">
    <div style="margin-top: 1%;">
      <button class="row btn btn-block btn-success" data-toggle="modal" href="#newDialogWindow" style="margin: 1%"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create dialog</button>
    </div>

    <div id="dialogs" style="margin-top: 1%;">

    </div>
    <button id="loadMoreDialogsButton" class="btn btn-info btn-block" style="display: none" onclick="loadMoreDialogs()">Load more dialogs</button>
    <input type="hidden" name="current" id="current" value="">
  </div>
  <div class="col-xs-9" style="margin-top: 1%">
    <div class="panel panel-primary">
      <div class="panel-body" id="tableDiv">
        <button class="btn btn-info btn-block" style="display: none" id="loadMoreMessages">Load previous messages</button>
        <ul class="chat" id="response">

        </ul>
      </div>
    </div>
      <div class="row" style="height: 70px">
        <div class="col-xs-10">
          <textarea class="form-control" style="resize: none" id="message" rows="2"></textarea>
        </div>
        <div class="col-xs-2">
          <input type="button" id="sendMessageButton" onclick="return buttonAction();" class="btn btn-large btn-block btn-block-full btn-primary"
                 value="Send message" />
        </div>
      </div>
  </div>
</div>
<script type="text/javascript">
$('#message').keydown(function (e) {
if (e.ctrlKey && e.keyCode === 13) {
  if($('#message').val().trim() !== "")
    sendMessage(null,null,null);
}
});
if($('#dialogId').val() !== '0')
  $('#dialog' + $('#dialogId').val()).addClass("active");
</script>




<div class="modal fade" id="newDialogWindow" tabindex="-1" role="dialog" aria-labelledby="newDialogLabel">
  <div class="modal-dialog modal-md">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <label id="newDialogLabel"><h2>New dialog</h2></label>
      </div>
      <div class="modal-body">
        <div class="form-group form-group-sm">
          <div class="row">
            <label class="control-label col-xs-3" style="margin-top: 7px" for="dialogName">Dialog name</label>
            <div class="col-xs-9">
              <input type="text" class="form-control input-sm" id="dialogName" name="dialogName" value="">
            </div>
          </div>
        </div>
        <p><b>Select friends for a new dialog</b></p>
        <div id="friendsList" align="center" style="max-height: 330px; overflow-y: auto">
          <%for(User friend:friends){%>
          <div class="row btn btn-default btn-block" align="center" onclick="selectUser(<%=friend.getId()%>)">
            <div class="col-xs-2" align="center"><input class="form-control" name="users" id="user<%=friend.getId()%>" value="<%=friend.getId()%>" type="checkbox" onclick="selectByCheckBox(<%=friend.getId()%>)"></div>
            <div class="col-xs-2">
              <div class="row img_wrap" style="background-image:url(<%=friend.getPhotoURL()%>)"></div>
            </div>
            <div class="col-xs-8"><%=friend.getLogin()%> (<%=friend.getName()%>) <br> <%=friend.getCity().getName()%> (<%=friend.getCountry().getName()%>) <img style="height: 18px" src="<%=friend.getCountry().getFlagURL()%>">
            </div>
          </div>
          <%}%>
        </div>
        <div class="row" style="margin-top: 1%">
          <div align="center">
            <button type="button" class="btn btn-success" <%if (friends.size()==0) {%>disabled="disabled"<%}%> data-dismiss="modal" value="Create" onclick="createDialog()">Create</button>
          </div>
        </div>
        <script type="text/javascript">
          let clicked = new Set();
          function selectUser(id){
            if (!clicked.has(id)){
              document.getElementById("user"+id).checked = !document.getElementById("user"+id).checked;
            }else {
              clicked.delete(id);
            }

          }
          function selectByCheckBox(id){
            clicked.add(id);
          }
        </script>
      </div>
      <div class="modal-footer">

      </div>
    </div>
  </div>
</div>
</body>
</html>
