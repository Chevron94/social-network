var tokenUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/oauth/token';
var tokenRequest = 'Basic dWktY2xpZW50OjEwMjIzN2Q3LTA2NmEtNGQxMC1iMDRkLWYxM2YwM2E3ODBiYg==';

function sendGetRequest(url, params, callback) {
    var access_token = getCookie('access_token');
    $.ajax({
        url: url,
        headers: {
            'Authorization': 'Bearer ' + access_token
        },
        data: params,
        success: function (data) {
            callback(data);
        },
        statusCode: {
            401: function () {
                getToken();
                var new_token = getCookie('access_token');
                $.ajax({
                    url: url,
                    headers: {
                        'Authorization': 'Bearer ' + new_token
                    },
                    data: params,
                    success: function (data) {
                        callback(data);
                    },
                    statusCode: {
                        401: function () {
                            alert("401");
                        },
                        404: function () {
                            alert("404");
                        }
                    }
                })
            }
        }
    });
}

function sendPostRequest(url, body, callback) {

}

function getToken() {
    var access_token = getCookie('access_token');
    var refresh_token = getCookie('refresh_token');
    $.ajax({
        url: tokenUrl,
        headers:{
            'Authorization': tokenRequest,
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'POST',
        data: 'grant_type=refresh_token&access_token='+access_token+'&password='+refresh_token,
        success: function(data){
            setCookie("access_token", data.access_token);
            setCookie("access_token", data.refresh_token);
        },
        async: false
    })
}

function setCookie(cname, cvalue) {
    var d = new Date();
    d.setTime(d.getTime() + (24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}