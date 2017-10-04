<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="socialnetwork.entities.Album" %><%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 02.04.2016
  Time: 20:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Album album = (Album) request.getAttribute("album");
%>
<head>
    <title><%=album.getName()%>
    </title>
    <%@include file="templates/scripts.jsp" %>
    <script type="text/javascript">
        var curPhoto = 0;
    </script>
</head>
<body onload="loadPhotos(<%=album.getId()%>,curPhoto,24)">
<br>
<%@include file="templates/header.jsp" %>
<div class="row" id="titleName" style="margin-left: 1%; margin-right: 1%">
    <h2><%=album.getName()%></h2>
</div>


<div class="row" style="margin: 1%" id="controlButtons">
    <a class="btn btn-info" href="/user<%=album.getUser().getId()%>/albums"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> <b><%=album.getUser().getLogin()%>'s albums</b></a>
    <%
        if (idUser.equals(album.getUser().getId()) && !album.getName().equals("Main")) {
    %>
        <button class="btn btn-success" id="uploadPhotosButton" value="Add photos" data-toggle="modal" href="#uploadWindow"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add photos</button>
        <button class="btn btn-warning" value="Rename album" data-toggle="modal" href="#renameWindow"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Rename album</button>
        <button class="btn btn-danger" id="deleteAlbumButton" value="Delete album" data-toggle="modal" href="#deleteWindow"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span> Delete album</button>
    <%
        }
    %>
</div>
<%
    if (idUser.equals(album.getUser().getId()) && !album.getName().equals("Main")) {
%>
<div class="row form-group form-group-sm" style="margin: 1%" id="editField">
    <input class="form-control" type="text" id="newName" style="display: none" value="">
    <button class="btn-primary" style="display: none" value="Submit">Submit</button>
</div>
<%
    }
%>
<div class="row" style="margin: 1%; max-height: 60vh; overflow-y: auto">
    <div class="col-xs-12">
        <div class="row table-bordered">
            <div class="col-xs-12" id="photos">

            </div>
        </div>
        <div class="row" align="center">
            <button class="btn btn-info" id="loadMore" value="LoadMore" style="display: none" onclick="loadPhotos(<%=album.getId()%>,curPhoto,12);">LoadMore</button>
        </div>
    </div>
</div>


<%@include file="templates/photoView.jsp"%>

<div class="modal fade" id="deleteWindow" tabindex="-1" role="dialog" aria-labelledby="deleteLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="deleteLabel"><h2>Delete album</h2></label>
            </div>
            <div class="modal-body">
                <p>Do you want to delete this album?</p>
                <p><button class="btn btn-danger btn-sm" value="Delete" onclick="deleteAlbum(<%=album.getId()%>)">Delete</button></p>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="renameWindow" tabindex="-1" role="dialog" aria-labelledby="renameLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="renameLabel"><h2>Rename album</h2></label>
            </div>
            <div class="modal-body">
                <div class="form-group form-group-sm">
                    <div class="row">
                        <div class="col-xs-3">
                            <label class="control-label" for="albumName">Album name<sup>*</sup></label>
                        </div>
                        <div class="col-xs-9">
                            <input type="text" id="albumName" class="form-control" name="albumName" pattern="^\w+([\s-]\w+)*$" required>
                            <label class="control-label" for="albumName" style="font-size: smaller">Can contains only A-Z,a-z,0-9,_,- and space</label>
                        </div>
                    </div>
                    <div class="row" style="margin-top: 1%">
                        <div class="col-xs-offset-3 col-xs-9">
                            <input align="center" type="button" data-dismiss="modal" class="btn btn-primary btn-sm" value="Rename" onclick="renameAlbum(<%=album.getId()%>)">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="uploadWindow" tabindex="-1" role="dialog" aria-labelledby="uploadLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="uploadLabel"><h2>Upload photos</h2></label>
            </div>
            <div class="modal-body">
                <form method="post" id="messageForm" action="/albums/<%=album.getId()%>" enctype="multipart/form-data">
                    <div class="form-group form-group-sm">
                        <div class="row">
                            <div class="col-xs-3" align="left">
                                <label for="files" class="control-label">Select files</label>
                            </div>
                            <div class="col-xs-9" align="left">
                                <div class="input-group">
                        <span class="input-group-btn">
                            <span class="btn btn-default btn-file btn-sm">
                                 Browse&hellip;<input type="file" id="files" name="files" multiple="multiple" accept="image/*"/>
                            </span>
                        </span>
                                    <input type="text" id="filename" class="form-control" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="row" style="margin-top: 1%">
                            <div class="col-xs-offset-3 col-xs-9" align="left">
                                <input align="center" type="submit" class="btn btn-primary" value="Upload">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    $(document).on('change', '.btn-file :file', function() {
        var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [numFiles, label]);
    });

    $(document).ready( function() {
        $('.btn-file :file').on('fileselect', function(event, numFiles, label) {

            var input = $(this).parents('.input-group').find(':text'),
                    log = numFiles > 1 ? numFiles + ' files selected' : label;

            if( input.length ) {
                input.val(log);
            } else {
                if( log ) alert(log);
            }

        });
    });
</script>

</html>
