<%@ page import="socialnetwork.entities.User" %>
<%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 31.03.2016
  Time: 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Albums</title>
    <%@include file="templates/scripts.jsp" %>
    <%
        User requestUser = (User) request.getAttribute("requestUser");
    %>
    <script>
        var albumStart = 0;
    </script>
</head>
<body onload="loadAlbums(<%=requestUser.getId()%>,albumStart,6)">
<br>
<%@include file="templates/header.jsp" %>
<div class="row" style="margin: 1%">
    <a class="btn btn-info" href="/user<%=requestUser.getId()%>"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> <b><%=requestUser.getLogin()%>'s profile
    </b></a>
    <%if (idUser.equals(requestUser.getId())) {%>
    <button class="btn btn-success" value="Create Album" data-toggle="modal" data-target="#albumWindow"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create Album
    </button>
    <%}%>
</div>
<div id="albums" class="row" style="max-height: 68vh; margin: 1%; overflow-y: auto">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-xs-12" id="albumsField">
            </div>
        </div>
        <div class="row" align="center">
            <button class="btn btn-info" id="loadMore" value="LoadMore" style="display: none"
                    onclick="loadAlbums(<%=requestUser.getId()%>,albumStart,6);">LoadMore
            </button>
        </div>
    </div>

</div>

<%@include file="templates/photoView.jsp" %>

<div class="modal fade" id="albumWindow" tabindex="-1" role="dialog" aria-labelledby="albumLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <label id="albumLabel"><h2>New album</h2></label>
            </div>
            <div class="modal-body">
                <div class="form-group form-group-sm">
                    <div class="row">
                        <div class="col-xs-3" align="left">
                            <label for="albumName" class="control-label">Album name</label>
                        </div>
                        <div class="col-xs-9" align="right">
                            <input type="text" id="albumName" name="albumName" class="form-control" value="">
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="col-xs-offset-3 col-xs-9">
                            <input type="button" data-dismiss="modal" class="btn btn-primary btn-sm" value="Create"
                                   onclick="createAlbum()">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
</body>
</html>
