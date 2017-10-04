/**
 * Created by roman on 10/12/15.
 */
var acceptUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/people/confirmRequest';
var sendUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/people/sendRequest';
var deleteUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/people/deleteRequest';
var loadMoreUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/people/more';

function updateFriends(sender)
{
    $('#add_'+sender).hide();
    var html =  document.getElementById(sender);
    $('#'+sender).remove();
    $('#friends').append(html);
    $('#friendsNotFound').html('');
}

function updateRequests(receiver)
{
    $('#add_'+receiver).hide();
    var html = document.getElementById(receiver);
    $('#'+receiver).remove();
    $('#sentRequests').append(html);
    $('#sentRequestsNotFound').html('');

}

function deleteFriend(receiver){
    $('#'+receiver).remove();
}

function acceptRequest(sender,receiver)
{
    var x = sender + receiver;
    $.post(acceptUrl,
        {
            idSender: sender,
            idReceiver: receiver,
            ajax: 'true'
        }, updateFriends(sender)
    )

}
function sendRequest(sender,receiver)
{
    var x=sender+receiver;
    $.post(sendUrl,
        {
            idSender: sender,
            idReceiver: receiver,
            ajax : 'true'},function(data){
            var path = window.location.pathname;
            if(path == '/users' || path=='/friends') {
                if (data === "true") {
                    updateFriends(receiver)
                } else {
                    updateRequests(receiver)
                }
            }else{
                $('#friendButton').html('');
                var html ='<input type="button" class="btn btn-danger btn-block" value="Delete" onclick="deleteRequest('+sender+','+receiver+')">';
                $('#deleteButton').html(html);
            }
        }

    );
}

function deleteRequest(sender,receiver){
    var x=sender+receiver;
    $.post(deleteUrl,
        {
            idSender: sender,
            idReceiver: receiver,
            ajax : 'true'},function(data){
                var path = window.location.pathname;
                if(path == '/users' || path=='/friends') {
                    deleteFriend(receiver);
                }else{
                    $('#deleteButton').html('');
                    var html = '<input type="button" class="btn btn-success btn-block" value="Add to friends" onclick="sendRequest('+sender+','+receiver+')">';
                    $('#friendButton').html(html);
                }
            }

    );
}

Date.prototype.customFormat = function(formatString){
    var YYYY,YY,MMMM,MMM,MM,M,DDDD,DDD,DD,D,hhhh,hhh,hh,h,mm,m,ss,s,ampm,AMPM,dMod,th;
    YY = ((YYYY=this.getFullYear())+"").slice(-2);
    MM = (M=this.getMonth()+1)<10?('0'+M):M;
    MMM = (MMMM=["January","February","March","April","May","June","July","August","September","October","November","December"][M-1]).substring(0,3);
    DD = (D=this.getDate())<10?('0'+D):D;
    DDD = (DDDD=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"][this.getDay()]).substring(0,3);
    th=(D>=10&&D<=20)?'th':((dMod=D%10)==1)?'st':(dMod==2)?'nd':(dMod==3)?'rd':'th';
    formatString = formatString.replace("#YYYY#",YYYY).replace("#YY#",YY).replace("#MMMM#",MMMM).replace("#MMM#",MMM).replace("#MM#",MM).replace("#M#",M).replace("#DDDD#",DDDD).replace("#DDD#",DDD).replace("#DD#",DD).replace("#D#",D).replace("#th#",th);
    h=(hhh=this.getHours());
    if (h==0) h=24;
    if (h>12) h-=12;
    hh = h<10?('0'+h):h;
    hhhh = h<10?('0'+hhh):hhh;
    AMPM=(ampm=hhh<12?'am':'pm').toUpperCase();
    mm=(m=this.getMinutes())<10?('0'+m):m;
    ss=(s=this.getSeconds())<10?('0'+s):s;
    return formatString.replace("#hhhh#",hhhh).replace("#hhh#",hhh).replace("#hh#",hh).replace("#h#",h).replace("#mm#",mm).replace("#m#",m).replace("#ss#",ss).replace("#s#",s).replace("#ampm#",ampm).replace("#AMPM#",AMPM);
};


function loadMore(start,sender,list){
    $('#loadMoreSent').hide();
    var male =( $('#gender').val().indexOf('m')>0);
    var female = ( $('#gender').val().indexOf('f')>0);
    $.getJSON(loadMoreUrl,
        {
            idUser: sender,
            start: start,
            list: list,
            login: $('#login').val(),
            idContinent: $('#continent').val(),
            idCountry: $('#country').val(),
            idCity: $('#city').val(),
            male: male,
            female: female,
            ageFrom: $('#ageFrom').val(),
            ageTo: $('#ageTo').val(),
            idLanguage: [$('#language').val()],
            ajax : 'true'},function(data){
            var html='';
            var len = data.length;
            if (len == 0){
                html+='<h5 align="center">Not Found</h5>';
            }
            for (var i = 0; i< len; i++) {
                var date = new Date(data[i].birthday);
                var status;
                if (data[i].online) {
                    status = "display: inline-block"
                } else {
                    status = "display: none"
                }
                html+='<div id="'+data[i].id+'" class="row scale-text" style="margin: auto; margin-right:1%; border-bottom: 1px solid #ddd;">'+
                        '<div class="col-xs-11">'+
                            '<div class="row" style="margin: auto">'+
                                '<div class="col-xs-12">'+
                                    '<div class="row">' +
                                        '<div class="col-xs-3"><div class="row" style="margin-top: 1%;"> ';
                if(data[i].album.photos.length>0){
                    html += '<div class="col-xs-12 btn btn-link img_wrap_big" style="background-image: url('+data[i].photoURL+')" data-toggle="modal" data-target="#photoViewer" onclick="showPhoto('+data[i].album.photos[0].id+",'"+data[i].album.photos[0].photoUrl+"',"+data[i].id+","+data[i].album.id+",'"+(new Date(data[i].album.photos[0].uploaded)).customFormat("#YYYY#-#MM#-#DD#")+"',"+0+')"></div>';
                }else{
                    html+= '<div class="col-xs-12 img_wrap_big" style="background-image: url('+data[i].photoURL+')"></div>';
                }
                html+=                  '</div>' +
                    '<div class="row" align="center" style="margin-top: 3%; margin-bottom: 3%;">';
                if (list == 'friends' || list == 'sent'){
                    html+= '<button id="add_'+data[i].id+'" style="display: none" type="button" class="Add btn btn-success btn-xs" onclick="acceptRequest('+data[i].id+','+idUser+'); return false">Add'+
                        '</button>\n';
                }else{
                    if (list =='received'){
                        html+='<button id="add_'+data[i].id+'" type="button" class="Add btn btn-success btn-xs" onclick="acceptRequest('+data[i].id+','+idUser+'); return false">Add'+
                            '</button>\n';
                    }else{
                        html+='<button id="add_'+data[i].id+'" type="button" class="Add btn btn-success btn-xs" onclick="sendRequest('+idUser+','+data[i].id+'); return false">Add'+
                            '</button>\n';
                    }
                }

                html+= '<button id="send_'+data[i].id+'" type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#messageWindow" onclick="newMessage('+data[i].id+",'"+data[i].login+"'"+'); return false">Message'+
                    '</button>\n';

                if ((list == 'friends' || list == 'received' || list == 'sent') && idUser==idRequestUser) {
                    html+= '<button id="delete_' + data[i].id + '" type="button" class="Add btn btn-danger btn-xs" onclick="deleteRequest(' + idUser + ', ' + data[i].id + '); return false">Delete' +
                        '</button>';
                }else{
                    html+= '<button id="delete_' + data[i].id + '" style="display: none" type="button" class="Add btn btn-danger btn-xs" onclick="deleteRequest(' + idUser + ', ' + data[i].id + '); return false">Delete' +
                        '</button>';
                }
                html+=
                    '</div>'+
                                        '</div>'+
                                        '<div class="col-xs-9">'+
                                            '<div class="row  table-fixed">'+
                                                '<div class="col-xs-3">'+
                                                    'Login'+
                                                '</div>'+
                                                '<div class="col-xs-9">'+
                                                    '<a href="/user'+data[i].id+'"><b>'+data[i].login+'</b></a>\n'+
                                                    '<span id="status'+data[i].id+'" class="label label-success" style="'+status+'">Online</span>'+
                                                '</div>'+
                                            '</div>'+
                                            '<div class="row  table-fixed">'+
                                                '<div class="col-xs-3">'+
                                                    'Name'+
                                                '</div>'+
                                                '<div class="col-xs-9">'+
                                                    '<b>'+data[i].name+'</b>'+
                                                '</div>'+
                                            '</div>'+
                                            '<div class="row table-fixed">'+
                                                '<div class="col-xs-3">'+
                                                    'Birthday'+
                                                '</div>'+
                                                '<div class="col-xs-9">'+
                                                    '<b>'+date.customFormat("#YYYY#-#MM#-#DD#") +'</b>'+
                                                '</div>'+
                                            '</div>'+
                                            '<div class="row table-fixed">'+
                                                '<div class="col-xs-3">'+
                                                    'From'+
                                                '</div>'+
                                                '<div class="col-xs-9">'+
                                                    '<b>'+
                                                        '<img style="height: 18px" src="'+data[i].countryFlag+'"> '+data[i].city + ' (' + data[i].country +')'+
                                                    '</b>'+
                                                '</div>'+
                                            '</div>'+
                                            '<div class="row table-fixed">'+
                                                '<div class="col-xs-3">'+
                                                    'Gender'+
                                                '</div>'+
                                                '<div class="col-xs-9">'+
                                                    '<b>'+data[i].gender+'</b>'+
                                                '</div>'+
                                            '</div>'+
                                            '<div class="row table-fixed" style="margin-bottom: 1%">'+
                                                '<div class="col-xs-3">'+
                                                    'Languages'+
                                                '</div>'+
                                                '<div class="col-xs-9">';
                                                    html+='<ui style="list-style: none;">';
                                                    for(var j = 0; j<data[i].languages.length; j++){
                                                        var lang = data[i].languages[j];
                                                        var style = '';
                                                        switch (lang.level){
                                                            case 1:
                                                                style = 'background-color: PowderBlue; color: black;';
                                                                break;
                                                            case 2:
                                                                style = 'background-color: LightBlue; color: black;';
                                                                break;
                                                            case 3:
                                                                style = 'background-color: LightSkyBlue; color: black;';
                                                                break;
                                                            case 4:
                                                                style = 'background-color: DeepSkyBlue; color: black;';
                                                                break;
                                                            case 5:
                                                                style = 'background-color: DodgerBlue;';
                                                                break;
                                                            case 6:
                                                                style = 'background-color: Blue;';
                                                                break;
                                                            case 7:
                                                                style = 'background-color: MediumBlue;';
                                                                break;
                                                            case 8:
                                                                style = 'background-color: #0000CD;';
                                                                break;
                                                            default:
                                                                style ='';
                                                        }
                                                    html += '<li><span style="'+style+'" class="label">'+lang.name+" ("+lang.levelName+")"+'</span></li>';
                                                    }
                                                html+='</ui></div>'+
                                            '</div>'+
                                    '</div>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>'+'<br>';
            }
            if (list == 'friends') {
                friendsStart++;
                $('#friends').append(html);
                if (len<20){
                    $('#loadMoreFriends').hide();
                }else{
                    $('#loadMoreFriends').show();
                }
            }else if(list == 'sent'){
                sentRequestsStart++;
                $('#sentRequests').append(html);
                if (len<20){
                    $('#loadMoreSent').hide();
                }else{
                    $('#loadMoreSent').show();
                }
            }else if(list == 'received'){
                receivedRequestsStart++;
                $('#friendRequests').append(html);
                if (len<20){
                    $('#loadMoreReceived').hide();
                }else{
                    $('#loadMoreReceived').show();
                }
            }else{
                otherStart++;
                $('#otherUsers').append(html);
                if (len<20){
                    $('#loadMoreOther').hide();
                }else{
                    $('#loadMoreOther').show();
                }
            }
        }

    );
}
function initPage(){
    $('#searchButton').prop('disabled', true);
    setTimeout(function(){
        $('#searchButton').prop('disabled', false);
    }, 2000);
    var path = window.location.pathname;
    if(path == '/users'){
        $('#otherUsers').html('');
        otherStart = 0;
        loadMore(otherStart,idUser,'other');
    }else{
        $('#friends').html('');
        friendsStart = 0;
        loadMore(friendsStart,idRequestUser,'friends');
        $('#friendRequests').html('');
        $('#sentRequests').html('');
        receivedRequestsStart = 0;
        sentRequestsStart = 0;
        loadMore(receivedRequestsStart,idUser,'received');
        loadMore(sentRequestsStart,idUser,'sent');
    }
}
