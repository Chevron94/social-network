<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Objects" %>
<%@ page import="socialnetwork.entities.*" %>
<%--
  Created by IntelliJ IDEA.
  User: roman
  Date: 10/4/15
  Time: 5:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="templates/scripts.jsp" %>
    <link href="/resources/css/text.css" rel="stylesheet">
    <% User user = (User) request.getAttribute("user"); %>
    <% SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");%>
    <% List<User> friends = (List<User>) request.getAttribute("friends"); %>
    <% FriendRequest friendRequest = (FriendRequest) request.getAttribute("friendRequest");%>
    <% String error = (String) request.getAttribute("error"); %>
    <% List<Album> albums = (List<Album>) request.getAttribute("albums");%>
    <% Long numberOfAlbums = (Long) request.getAttribute("numberOfAlbums");%>
    <% Long numberOfFriends = (Long) request.getAttribute("numberOfFriends");%>
    <% SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");%>
    <title><%=Objects.equals(user.getId(),request.getSession().getAttribute("idUser")) ? "My profile" : user.getLogin()+"'s profile"%></title>
    <style>
        .word {
            word-break: break-all;
        }
    </style>
</head>
<body>
<br>
<%@include file="templates/header.jsp" %>
<%
    if(error!= null){
%>
<div style="background-color: red">
    <h4><%=error%></h4>
</div>
<%
    }
%>
<div class="row scale-text" style="margin: auto; border-bottom: 3px solid lightgrey;">
    <div class="col-xs-12" style="margin-bottom: 10px">
        <div class="row" style="margin-left: 20%">
            <div class="col-xs-3">
                <div class="row table-bordered table-fixed">
                    <%
                        if (user.getAlbums().get(0).getPhotos() != null && user.getAlbums().get(0).getPhotos().size() > 0) {
                    %>
                    <input type="image" id="mainPhoto" src="<%=user.getPhotoURL()%>" class="img img-responsive" alt="Responsive image"
                           style="width: 100%" data-toggle="modal" data-target="#photoViewer"
                           onclick="showPhoto(<%=user.getAlbums().get(0).getPhotos().get(0).getId()%>,'<%=user.getPhotoURL()%>',<%=user.getId()%>,<%=user.getAlbums().get(0).getId()%>,'<%=simpleDateFormat.format(user.getAlbums().get(0).getPhotos().get(0).getUploaded())%>',0)">
                    <%
                    } else {
                    %>
                    <img src="<%=user.getPhotoURL()%>" class="img img-responsive" style="width: 100%"
                         alt="Responsive image">
                    <%
                        }%>
                </div>
                <div id="buttons">
                    <%
                        if (Objects.equals(idUser, user.getId())) {
                    %>
                    <div class="row" id="newPhotoButton">
                        <button type="button" class="btn btn-success btn-block" value="Upload new photo"
                               data-toggle="modal" data-target="#updatePhotoWindow"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Upload new photo</button>
                    </div>
                    <div class="row" align="center" style="margin-top: 1%">
                        <button type="button" data-toggle="modal" data-target="#editViewer" class="btn btn-info btn-block" onclick="return false;"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Edit</button>
                    </div>
                    <%
                        }%>
                    <%
                        if (!Objects.equals(user.getId(), idUser)) {
                    %>

                    <%@include file="templates/message.jsp" %>

                    <div class="row" id="friendButton" style="margin-top: 1%;">
                        <%
                            if (friendRequest == null || (Objects.equals(friendRequest.getSender().getId(), user.getId()) && !friendRequest.isConfirmed())) {
                        %>
                        <button type="button" class="btn btn-success btn-block" value="Add to friends"
                               onclick="sendRequest(<%=idUser%>,<%=user.getId()%>)"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add to friends</button>
                        <%
                            }%>
                    </div>
                    <div class="row" id="messageButton" style="margin-top: 1%;">
                        <button type="button" class="btn btn-info btn-block" value="Send message" data-toggle="modal"
                               data-target="#messageWindow"
                               onclick="newMessage(<%=user.getId()%>,'<%=user.getLogin()%>')"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> Send message</button>
                    </div>
                    <div class="row" id="deleteButton" style="margin-top: 1%;">
                        <%
                            if (friendRequest != null && (friendRequest.isConfirmed() || Objects.equals(friendRequest.getSender().getId(), idUser))) {
                        %>
                        <button type="button" class="btn btn-danger btn-block" value="Delete"
                               onclick="deleteRequest(<%=idUser%>,<%=user.getId()%>)"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span> Delete</button>
                        <%
                            }%>
                    </div>
                    <%
                        }%>
                </div>
                <div class="row" style="background: lightgrey; margin-top: 1%">
                    <a href="<%=pageContext.getRequest().getScheme()+"://"
                            + pageContext.getRequest().getServerName()+"/user"+user.getId()+"/friends"%>">
                        <b>Friends <%="(" + numberOfFriends + ")"%>
                        </b>
                    </a>
                </div>
                <%
                    if (friends.size() > 0) {
                        for (int i = 0; i < 2; i++) {
                            if (i * 3 < friends.size()) {
                %>
                <div class="row" style="margin-top: 1%">


                    <%
                        for (int j = 0; j < 3; j++) {
                            if (friends.size() > 3 * i + j) {
                    %>
                    <div class="col-xs-4" style="margin-inside: 5%">
                        <div class="row" align="center">
                            <div class="col-xs-offset-1 col-xs-10 img_wrap_profile btn btn-link" style="background-image: url(<%=friends.get(3*i+j).getPhotoURL()%>)" onclick="window.location.href='/user<%=friends.get(3*i+j).getId()%>'"></div>
                        </div>
                        <div class="row" align="center">
                            <a href="/user<%=friends.get(3*i+j).getId()%>"><p
                                    class="word"><%=friends.get(3 * i + j).getName()%>
                            </p></a>
                        </div>
                    </div>

                    <%
                            }
                        }

                    %>

                </div>
                <%
                            }
                        }
                    }
                %>

                <div class="row" style="background: lightgrey; margin-top: 1%">
                    <a href="<%=pageContext.getRequest().getScheme()+"://"
                            + pageContext.getRequest().getServerName()+"/user"+user.getId()+"/albums"%>">
                        <b>Albums <%="(" + numberOfAlbums + ")"%>
                        </b>
                    </a>
                </div>
                <%if(numberOfAlbums>0){
                %>
                <%
                    for (Album album : albums) {

                %>
                <div class="row" style="margin-top: 1%; margin-bottom: 1%;">
                    <div class="col-xs-12">
                        <div style="border-style: double;" class="row">
                            <%  if (album.getPhotos() != null && album.getPhotos().size() > 0) {%>
                            <div class="col-xs-12 img_wrap_big btn"
                                 style="background-image: url(<%=album.getPhotos().get(0).getPhotoUrl()%>)"
                                 data-toggle="modal" data-target="#photoViewer"
                                 onclick="showPhoto(<%=album.getPhotos().get(0).getId()%>,'<%=album.getPhotos().get(0).getPhotoUrl()%>',<%=user.getId()%>,<%=album.getId()%>,'<%=simpleDateFormat.format(album.getPhotos().get(0).getUploaded())%>',0)">
                            </div>
                            <% } else { %>
                            <div class="col-xs-12 btn"
                                onclick="location.href = '/albums/<%=album.getId()%>'">
                                 No Photos yet
                            </div>
                            <% } %>
                        </div>
                        <div class="row">
                            <a href="/albums/<%=album.getId()%>"><p
                                    class="word"><%=album.getName()%>
                            </p></a>
                        </div>
                    </div>

                </div>
                <%
                    }
                %>

                <%
                }%>

            </div>
            <div class="col-xs-offset-1 col-xs-6" align="left">
                <div class="row table-bordered">
                    <div class="col-xs-12">
                        <div class="row table-fixed">
                            <div class="col-xs-3">
                                Name
                            </div>
                            <div class="col-xs-9">
                               <%=user.getName()%>
                                <span id="status<%=user.getId()%>" class="label label-success" style="display: <%=user.getOnline() ? "inline-block" : "none"%>">Online</span>
                            </div>
                        </div>

                        <div class="row table-fixed" >
                            <div class="col-xs-3">
                                Birthday
                            </div>
                            <div class="col-xs-9">
                                <%=dateFormat.format(user.getBirthday())%>
                            </div>
                        </div>

                        <div class="row table-fixed" >
                            <div class="col-xs-3">
                                From
                            </div>
                            <div class="col-xs-9">
                                <img style="height: 18px" src="<%=user.getCountry().getFlagURL()%>">
                                <%=user.getCity().getName() + " (" + user.getCountry().getName() + ")"%>
                            </div>
                        </div>

                        <div class="row table-fixed" >
                            <div class="col-xs-3">
                                Gender
                            </div>
                            <div class="col-xs-9">
                                <%=user.getGender().getName()%>
                            </div>
                        </div>
                        <div class="row table-fixed" >
                            <div class="col-xs-3">
                                Languages
                            </div>
                            <div class="col-xs-9">
                                <ui style="list-style: none;">
                                    <%for(LanguageUser languageUser:user.getLanguageUsers()){
                                        switch (languageUser.getLanguageLevel().getId().intValue()){
                                            case 1:{
                                    %>
                                    <li><span style="background-color: PowderBlue; color: black;" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 2:{
                                    %>
                                    <li><span style="background-color: LightBlue; color: black;" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 3:{
                                    %>
                                    <li><span style="background-color: LightSkyBlue; color: black;" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 4:{
                                    %>
                                    <li><span style="background-color: DeepSkyBlue; color: black;" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 5:{
                                    %>
                                    <li><span style="background-color: DodgerBlue" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 6:{
                                    %>
                                    <li><span style="background-color: Blue" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 7:{
                                    %>
                                    <li><span style="background-color: MediumBlue" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                            break;
                                        }
                                        case 8:{
                                    %>
                                    <li><span style="background-color: #0000CD" class="label"><%=languageUser.getLanguage().getName()+" ("+languageUser.getLanguageLevel().getName()+")"%></span></li>
                                    <%
                                                    break;
                                                }
                                            }
                                        }%>
                                </ui>
                            </div>
                        </div>

                        <div class="row table-fixed" style="margin-top: 1%">
                            <div class="col-xs-3">
                                About me
                            </div>
                            <div class="col-xs-9">
                                <%=user.getDescription()%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div style="height: 20px;">

</div>
<%@include file="templates/photoView.jsp" %>
<div class="modal fade" id="updatePhotoWindow" tabindex="-1" role="dialog" aria-labelledby="updatePhotoLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <label id="updatePhotoLabel"><h2>Upload new photo</h2></label>
            </div>
            <div class="modal-body">
                <form method="post" action="/profile/updatePhoto" id="albumForm" enctype="multipart/form-data">
                    <div class="form-group form-group-sm">
                        <div class="row">
                            <div class="col-xs-3" align="left">
                                <label for="photoInput" class="control-label">Select new photo</label>
                            </div>
                            <div class="col-xs-9" align="left">
                                <div class="input-group">
                        <span class="input-group-btn">
                            <span class="btn btn-default btn-file btn-sm">
                                 Browse&hellip;<input type="file" id="photoInput" name="photoInput" accept="image/*"/>
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
<%@include file="templates/edit.jsp"%>
</body>
</html>
