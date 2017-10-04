/**
 * Created by Роман on 02.10.2016.
 */
var serviceOnlineLocation = "wss://" + document.location.host + "/sockets/online";
function connectToOnlineService() {
    onlineWebSocket = new WebSocket(serviceOnlineLocation);
    onlineWebSocket.onopen = function () {
        onlineWebSocket.send(idUser);
    };
    onlineWebSocket.onmessage = onOnlineMessageReceived;
}

function onOnlineMessageReceived(evt){
    var msg = JSON.parse(evt.data); // native API
    if ($('#status'+msg.userId).length) {
        if (msg.online == true) {
            $('#status' + msg.userId).css('display', 'inline-block')
        } else {
            $('#status' + msg.userId).hide();
        }
    }
}