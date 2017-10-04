/**
 * Created by Роман on 02.10.2016.
 */
var serviceFriendsLocation = "wss://" + document.location.host + "/sockets/friends";
var friendsWebSocket;
function connectToFriendsService() {
    friendsWebSocket = new WebSocket(serviceFriendsLocation);
    friendsWebSocket.onopen = function () {
        friendsWebSocket.send(idUser);
    };
    friendsWebSocket.onmessage = onFriendsMessageReceived;
}

function onFriendsMessageReceived(evt){
    var msg = JSON.parse(evt.data); // native API

}