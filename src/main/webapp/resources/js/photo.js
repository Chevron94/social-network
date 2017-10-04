/**
 * Created by Роман on 03.04.2016.
 */
var photoUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/photos';
var photoCntUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/albums/';
var updatePhotoUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/profile/updateMainPhoto';
var idAlbum;
var idPhoto;
function showPhoto(id, url, author_id, album, date, start){
    current = start;
    idAlbum = album;
    idPhoto = id;
    getCntPhotosInAlbum(album);
    document.getElementById("photo").src = url;
    document.getElementById('photoNum').innerHTML = current+1 +' of ' + max;
    document.getElementById('added').innerHTML = date;
    document.getElementById('setAsMainPhoto').setAttribute('onclick','updateMainPhoto('+id+');');
    if (author_id !== idUser){
        $('#setAsMainPhoto').hide();
        $('#deletePhoto').hide();
    }else{
        $('#setAsMainPhoto').show();
        $('#deletePhoto').show();
        document.getElementById('deletePhotoButton').setAttribute('onclick','deletePhoto('+id+')');
    }
}

function getCntPhotosInAlbum(idAlbum){
    $.getJSON(photoCntUrl+idAlbum+'/photosCnt',
        {
            ajax:true
        },
        function(data){
            max = data;
            document.getElementById('photoNum').innerHTML = current+1 +' of ' + max;
        })
}

function loadPhotos(idAlbum, page, count){
    $.getJSON(photoUrl,
        {
            idAlbum:idAlbum,
            page:page,
            count:count,
            ajax:true
        },
        function(data){
            if (count === 1){
                document.getElementById("photo").src = data[0].photoUrl;
                var date = new Date(data[0].uploaded);
                document.getElementById('added').innerHTML = date.customFormat("#YYYY#-#MM#-#DD#");
                document.getElementById('setAsMainPhoto').setAttribute('onclick','updateMainPhoto('+data[0].id+');');
                if (data[0].userId !== idUser){
                    $('#setAsMainPhoto').hide();
                    $('#deletePhoto').hide();
                }else{
                    $('#setAsMainPhoto').show();
                    $('#deletePhoto').show();
                    idPhoto = data[0].id;
                    document.getElementById('deletePhotoButton').setAttribute('onclick','deletePhoto('+data[0].id+')');
                }
            }else if(data.length>0){
                var length = data.length;
                var html = '';
                var rows = length/4;
                if (length %4>0){
                    rows++;
                }
                for(var i=0; i<rows; i++){
                    html+='<div class="row" style="margin-bottom: 1%; margin-top: 1%; margin-right: 1%">\n';
                    for(var j = 0; j<6 && i*6+j<length;j++){
                        var cur = 6*i+j;
                        html+='<div class="col-xs-2"  style="height: 135px; overflow: hidden; position: relative;">\n'+
                                '<input type="image" id="photo'+data[cur].id+'" src="'+data[cur].photoUrl+'" class="img img-responsive" alt="Responsive image" style="position: absolute"  data-toggle="modal" data-target="#photoViewer" onclick="showPhoto('+data[cur].id+",'"+data[cur].photoUrl+"',"+data[cur].userId+","+data[cur].albumId+",'"+(new Date(data[cur].uploaded)).customFormat("#YYYY#-#MM#-#DD#")+"',"+cur+')">'+
                                '</div>';
                    }
                    html+='</div>'
                }
                $('#photos').append(html);
                var elem = document.getElementById('photos');
                elem.scrollTop = elem.scrollHeight;
                curPhoto++;
                if(length<count){
                    $('#loadMore').hide();
                }else{
                    $('#loadMore').show();
                }
            }else if(curPhoto===0){
                var html='<div class="row" style="margin: 1%">\n';
                html+="<h4>No photos</h4>"
                $('#photos').append(html);
                $('#loadMore').hide();
            }
        })
}

function getNext(){
    if (current < max-1){
        current++;
        document.getElementById('photoNum').innerHTML = current+1 +' of ' + max;
        loadPhotos(idAlbum, current,1);
    }else{
        current=0;
        document.getElementById('photoNum').innerHTML = current+1 +' of ' + max;
        loadPhotos(idAlbum, current,1);
    }
}

function getPrevious(){
    if (current>0){
        current--;
        document.getElementById('photoNum').innerHTML = current+1 +' of ' + max;
        loadPhotos(idAlbum, current,1);
    }else{
        current=max-1;
        document.getElementById('photoNum').innerHTML = current+1 +' of ' + max;
        loadPhotos(idAlbum, current,1);
    }
}

function updateMainPhoto(idPhoto){
    $.post(updatePhotoUrl,
        {
            idPhoto: idPhoto,
            ajax : 'true'},function(data){
            if (data === "true"){
                alert("Success");
                if(path === '/profile' || path==='/user'+idUser){
                    window.location = '/profile';
                }
            }else if(data === "false"){
                alert("You already have this photo as main")
            }else alert("Error");
        }

    );
}

function deletePhoto(id){
    $.ajax({
        url: photoUrl+'/'+id,
        type: 'DELETE',
        success: function() {
            if (max === 1){
                window.location = photoCntUrl+id;
                max = 0;
                current = 0;
            }else{
                max -=1;
                $('#photo'+id).remove();
                if(current>0){
                    current -=1;
                }else current = max-1;
                $('#photoViewer').modal('hide');
                curPhoto--;
            }

        }
    });
}