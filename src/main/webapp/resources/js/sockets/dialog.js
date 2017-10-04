var dialogWebSocket;
var serviceMessagesLocation = "wss://" + document.location.host + "/sockets/dialog";
var $nickName;
var $message;
var $chatWindow;
var $dialog;
var path = window.location.pathname;
var audio = new Audio('/resources/audio/message.mp3');
audio.volume = 0.9;
var loadMoreDialogsUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/dialogs/loadMore';
var loadOneDialogUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/dialogs/';
var unreadMessagesUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/unreadMessages';
var newDialogUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + window.location.pathname;
var unreadMessages;
var openDialog;
var startMessage = 0;

function loadMoreMessages(idDialog) {
    var url = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/dialog/getMessages';
    $.getJSON(url,
        {
            idDialog: idDialog,
            startMessage: startMessage,
            ajax: true
        },
        function (data) {
            var len = data.length;
            var messageLine = '';
            for (var i = 0; i < len; i++) {
                var text = data[i].messageText.replace(/\n\r/g, "<br>");
                if (data[i].read) {
                    messageLine += '<li class="left clearfix read">';
                } else {
                    messageLine += '<li class="left clearfix unread">';
                }
                messageLine +=
                    '<span class="chat-img pull-left">' +
                    '<div class="img_wrap btn btn-link" style="width: 55px; background-image: url(' + data[i].avatar + ')" onclick="window.location.href=' + "'/user" + data[i].senderId + "';" + '"></div>' +
                    '</span>' +
                    '<div class="chat-body clearfix">' +
                    '<div class="header">' +
                    '<a href="/user' + data[i].senderId + '" ><strong class="primary-font"> ' + data[i].sender + '</strong></a> <small class="pull-right text-muted">' +
                    '<span class="glyphicon glyphicon-time"></span>' + data[i].received + '</small>' +
                    '</div>' +
                    '<p>' + text + '</p>' +
                    '</div>' +
                    '</li>';
                if (len < 20) {
                    $('#loadMoreMessages').hide()
                } else  $('#loadMoreMessages').show();
            }
            $('#dialogName').html($('#' + idDialog).html());
            if (startMessage === 0) {
                $('#response').html("");
            }
            $('#response').prepend(messageLine);
            if ($('#dialogId').val() != '0') {
                $('#dialog' + $('#dialogId').val()).removeClass("active");
            }
            if (document.getElementById('dialog' + idDialog)) {
                $('#dialog' + idDialog).addClass("active");
            }
            $('#dialogId').val(idDialog);
            openDialog = idDialog;
            window.sessionStorage.setItem("openDialog", JSON.stringify(openDialog));
            if (startMessage === 0) {
                var elem = document.getElementById('tableDiv');
                elem.scrollTop = elem.scrollHeight;
                sendMessage('', idUser, idDialog);
            }
            startMessage += len;
        })
}

function chatClick(idDialog) {

    if (document.getElementById('dialog' + idDialog)) {
        document.getElementById('dialog' + idDialog + "Unread").innerHTML = '';
        $('#dialog' + idDialog).removeClass("btn-danger");
        $('#dialog' + idDialog).addClass("btn-default");
    }
    if (idDialog.toString() in unreadMessages) {
        delete unreadMessages[idDialog.toString()];
        window.sessionStorage.setItem('unreadMessages', JSON.stringify(unreadMessages));
    }
    if (Object.keys(unreadMessages).length === 0) {
        $('#dialogsLink').html('Dialogs');
    } else {
        $('#dialogsLink').html('Dialogs <b>(+' + Object.keys(unreadMessages).length + ')</b>');
    }
    if (idDialog.toString() !== $('#dialogId').val()) {
        startMessage = 0;
        $('#loadMoreMessages').hide();
        document.getElementById('loadMoreMessages').onclick = function () {
            loadMoreMessages(idDialog);
        };
        loadMoreMessages(idDialog);
    }
}

function loadMoreDialogs() {
    $.getJSON(
        loadMoreDialogsUrl,
        {
            start: dialogStart,
            ajax: true
        }, function (data) {
            var html = '';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                if (data[i].id.toString() in unreadMessages) {
                    html += '<div id="dialog' + data[i].id + '" class="row btn btn-danger btn-block" style="margin: 1%" onclick="return chatClick(' + data[i].id + ')">\n';
                } else {
                    html += '<div id="dialog' + data[i].id + '" class="row btn btn-default btn-block" style="margin: 1%" onclick="return chatClick(' + data[i].id + ')">\n';
                }
                var picUrl = '';

                if (data[i].private) {
                    picUrl = data[i].users[0].photoURL;
                    //html += '<img src="' + data[i].userDialogs[0].user.photoURL + '" alt="User Avatar" class="img img-responsive" style="position: absolute" />\n';
                } else {
                    picUrl = '/resources/images/system/group.jpg';
                    //html += '<img src="/resources/images/system/group.jpg" alt="User Avatar" class="img img-responsive" style="position: absolute" />\n';
                }
                html += '<div class="col-xs-3">\n' +
                    '<div class="row img_wrap" style="background-image:url(' + picUrl + ')">\n';
                html +=
                    '</div>\n';
                if (data[i].private) {
                    var status;
                    if (data[i].users[0].online) {
                        status = "inline-block"
                    } else {
                        status = "none";
                    }
                    html += '<div class="row">\n' +
                        '<span id="status'+data[i].users[0].id+'" class="label label-success" style="display: '+status+'">Online</span>\n' +
                        '</div>\n';
                }

                html += '</div>\n' +
                    '<div class="col-xs-offset-2 col-xs-7">\n' +
                    '<div class="row" align="left">\n' +
                    '<label class="control-label">' + data[i].name + '</label>\n' +
                    '</div>\n' +
                    '<div class="row" align="left">\n';
                if (data[i].private) {
                    html += '<label class="control-label"><img style="height: 20px" src="' + data[i].users[0].countryFlag + '">' + data[i].users[0].city+ '</label>\n';
                }
                html += '</div>\n' +
                    '<div class="row">\n';
                if (data[i].id.toString() in unreadMessages) {
                    html += '<span id="dialog' + data[i].id + 'Unread" class="badge">+' + unreadMessages[data[i].id.toString()] + '</span>\n';
                } else {
                    html += '<span id="dialog' + data[i].id + 'Unread" class="badge"></span>\n';
                }
                html += '</div>\n' +
                    '</div>\n' +
                    '</div>';
            }
            $('#dialogs').append(html);
            if (len < 10) {
                $('#loadMoreDialogsButton').hide()
            } else {
                $('#loadMoreDialogsButton').show()
            }
            if (dialogStart === 0 && len > 0) {
                if (openDialog !== null && openDialog > 0) {
                    chatClick(openDialog, true);
                } else {
                    $('#dialog' + data[0].id).trigger('click');
                }
            }
            dialogStart += data.length;
        }
    )
}

function onMessageReceived(evt) {
    var msg = JSON.parse(evt.data); // native API
    if (!(msg.senderId === idUser) && !(msg.messageText === '')) {
        sendNotification(msg.sender,{ body: msg.messageText, dir: 'auto', icon: msg.avatar })
    }
    if (path === '/dialogs') {
        var isNew = false;
        var isReadEvent = false;
        if ($dialog.val() === msg.receiver.toString()) {
            if (msg.messageText === "") {
                isReadEvent = true;
                var messages = document.getElementById("response").childNodes;
                for (var i = 0; i < messages.length; i++) {
                    var userId = messages[i].childNodes[1].firstChild.firstChild.getAttribute("href");
                    userId = userId.substr(5);
                    if (messages[i].classList.contains("unread") && userId !== msg.senderId) {
                        messages[i].classList.remove("unread");
                        messages[i].classList.add("read");
                    }

                }
            } else {
                var text = msg.messageText.replace(/\n\r/g, "<br>");
                var messageLine = '';
                if (msg.read)
                    messageLine += '<li class="left clearfix read">';
                else messageLine += '<li class="left clearfix unread">';
                messageLine +=
                    '<span class="chat-img pull-left">' +
                    '<div class="img_wrap btn btn-link" style="width: 55px; background-image: url(' + msg.avatar + ')" onclick="window.location.href=' + "'/user" + msg.senderId + "';" + '"></div>' +
                    '</span>' +
                    '<div class="chat-body clearfix">' +
                    '<div class="header">' +
                    '<a href="/user' + msg.senderId + '" ><strong class="primary-font">' + msg.sender + '</strong></a>' +
                    '<small class="pull-right text-muted">' +
                    '<span class="glyphicon glyphicon-time"></span>' + msg.received + '</small>' +
                    '</div>' +
                    '<p>' + text + '</p>' +
                    '</div>' +
                    '</li>';
                startMessage += 1;
                $chatWindow.append(messageLine);
                var elem = document.getElementById('tableDiv');
                elem.scrollTop = elem.scrollHeight;
                if (!(msg.senderId === idUser)) {
                    audio.play();
                    sendMessage("", idUser, Number(msg.receiver));
                }
            }
        } else {
            if (msg.messageText !== "") {
                if (!document.getElementById('dialog' + msg.receiver)) {
                    isNew = true;
                    $.getJSON(loadOneDialogUrl + msg.receiver,
                        {
                            ajax: true
                        }, function (data) {
                            var html = '<div id="dialog' + data.id + '" class="row btn btn-danger btn-block" style="margin: 1%" onclick="return chatClick(' + data.id + ')">\n' +
                                '<div class="col-xs-3">\n' +
                                '<div class="row" style="height: 60px; overflow: hidden; position: relative;">\n';
                            if (data.private) {
                                html += '<img src="' + data.userDialogs[0].user.photoURL + '" alt="User Avatar" class="img img-responsive" style="position: absolute" />\n';
                            } else {
                                html += '<img src="/resources/images/system/group.jpg" alt="User Avatar" class="img img-responsive" style="position: absolute" />\n';
                            }
                            html +=
                                '</div>\n';
                            if (data.private){
                                html +=    '<div class="row">\n' +
                                    '<span id="status'+data.userDialogs[0].user.id+'" class="label label-success" style="display: inline-block">Online</span>\n' +
                                    '</div>\n';
                            }
                            html+=    '</div>\n' +
                                '<div class="col-xs-offset-2 col-xs-7">\n' +
                                '<div class="row" align="left">\n' +
                                '<label class="control-label">' + data.name + '</label>\n' +
                                '</div>\n' +
                                '<div class="row" align="left">\n';
                            if (data.private) {
                                html += '<label class="control-label"><img style="height: 20px" src="' + data.userDialogs[0].user.country.flagURL + '">' + data.userDialogs[0].user.city.name + '</label>\n';
                            }
                            html += '</div>\n' +
                                '<div class="row">\n' +
                                '<span id="dialog' + data.id + 'Unread" class="badge">+1</span>\n' +
                                '</div>\n' +
                                '</div>\n' +
                                '</div>';
                            $('#dialogs').prepend(html);
                            unreadMessages[data.id.toString()] = 1;
                            window.sessionStorage.setItem('unreadMessages', JSON.stringify(unreadMessages));
                            dialogStart++;
                        });
                }
                $('#dialog' + msg.receiver).removeClass("btn-default");
                $('#dialog' + msg.receiver).addClass("btn-danger");
                if (!(msg.senderId == idUser.toString())) {
                    if (msg.receiver in unreadMessages) {
                        unreadMessages[msg.receiver] = unreadMessages[msg.receiver] + 1;
                    } else unreadMessages[msg.receiver] = 1;
                    window.sessionStorage.setItem('unreadMessages', JSON.stringify(unreadMessages));
                    document.getElementById('dialog' + msg.receiver + "Unread").innerHTML = '+' + unreadMessages[msg.receiver];
                    $('#dialogsLink').html('Dialogs <b>(+' + Object.keys(unreadMessages).length + ')</b>');
                    audio.play();
                }
            } else {
                isReadEvent = true;
            }
        }
        if (!isNew && !isReadEvent) {
            var html = $('#dialog' + msg.receiver);
            $('#dialog' + msg.receiver).remove();
            $('#dialogs').prepend(html);
        }
    } else {
        if (!(msg.senderId === idUser)) {
            if (msg.receiver in unreadMessages) {
                unreadMessages[msg.receiver] = unreadMessages[msg.receiver] + 1;
            } else unreadMessages[msg.receiver] = 1;
            window.sessionStorage.setItem('unreadMessages', JSON.stringify(unreadMessages));
            $('#dialogsLink').html('Dialogs <b>(+' + Object.keys(unreadMessages).length + ')</b>');
            audio.play();
        }
    }

}
function sendMessage(messageText, sender, dialog) {
    var msg;
    if (sender === null && messageText === null && dialog === null) {
        if ($message.val().trim() !== "" && $dialog.val() !== "0") {
            msg = '{"messageText":"' + $message.val().trim().replace(new RegExp('"','g'),'\\"') + '", "sender":"'
                + $nickName.val() + '", "senderId":"' + idUser + '", "received":"","dialog":"' + $dialog.val() + '"}';
            dialogWebSocket.send(msg);
        }
        $message.val('').focus();
    } else {
        msg = '{"messageText":"' + messageText.trim().replace(new RegExp('"','g'),'\\"') + '", "sender":"'
            + sender + '", "senderId":"' + idUser + '","received":"","dialog":"' + dialog + '"}';
        dialogWebSocket.send(msg);
    }
}
function connectToChatServer() {
    dialogWebSocket = new WebSocket(serviceMessagesLocation);
    dialogWebSocket.onopen = function () {
        dialogWebSocket.send(
            "{" +
                '"senderId" : "' + idUser + '"' +
            "}");
    };
    dialogWebSocket.onmessage = onMessageReceived;
    if (window.sessionStorage.getItem("unreadMessages") === null) {
        $.getJSON(
            unreadMessagesUrl,
            {
                ajax: true
            }, function (data) {
                var len = data.length;
                unreadMessages = {};
                for (var i = 0; i < len; i++) {
                    unreadMessages[Object.getOwnPropertyNames(data[i])[0]] = data[i][Object.getOwnPropertyNames(data[i])[0]];
                }
                window.sessionStorage.setItem("unreadMessages", JSON.stringify(unreadMessages));
                if (Object.keys(unreadMessages).length === 0) {
                    $('#dialogsLink').html("Dialogs");
                } else {
                    $('#dialogsLink').html("Dialogs <b>(+" + Object.keys(unreadMessages).length + ")");
                }
                if (path === '/dialogs') {
                    loadMoreDialogs();
                }
            }
        );
    } else {
        unreadMessages = JSON.parse(window.sessionStorage.getItem("unreadMessages"));
        if (Object.keys(unreadMessages).length === 0) {
            $('#dialogsLink').html("Dialogs");
        } else {
            $('#dialogsLink').html("Dialogs <b>(+" + Object.keys(unreadMessages).length + ")");
        }
        if (path === '/dialogs') {
            loadMoreDialogs();
        }
    }

}

function buttonAction() {
    sendMessage(null, null, null);
}

function pageLoad() {
    if (path === '/dialogs') {
        $nickName = $('#nickname');
        $message = $('#message');
        $chatWindow = $('#response');
        $dialog = $('#dialogId');
        if (window.sessionStorage.getItem("openDialog") === null) {
            openDialog = 0;
            window.sessionStorage.setItem("openDialog", JSON.stringify(openDialog));
        } else {
            openDialog = JSON.parse(window.sessionStorage.getItem("openDialog"));
        }

        $('.chat-wrapper h4').text('Chat # ' + $nickName.val());
        $message.focus();
    }
}
function createDialog() {
    var name = $('#dialogName').val().trim();
    if (name !== "") {
        $('#dialogName').val("");
        var users = "";
        users += idUser;
        var i = 0;
        var friends = document.getElementsByName("users");
        for (var j = 0; j < friends.length; j++) {
            if (friends[j].checked) {
                users += "," + friends[j].value;
                friends[j].checked = false;
                i++;
            }
        }
        if (i>0){
            $.post(newDialogUrl,
                {
                    name: name,
                    users: [users],
                    ajax: true
                }, function (data) {
                    data = JSON.parse(data);
                    var id = data.id;
                    var status;
                    if (data.users[0].online){
                        status ="inline-block"
                    } else {
                        status= "none";
                    }
                    if (!document.getElementById('dialog' + id)) {
                        var html = '<div id="dialog' + data.id + '" class="row btn btn-default btn-block" style="margin: 1%" onclick="return chatClick(' + data.id + ')">\n' +
                            '<div class="col-xs-3">\n' +
                            '<div class="row" style="height: 60px; overflow: hidden; position: relative;">\n';
                        if (data.private) {
                            html += '<img src="' + data.users[0].photoURL + '" alt="User Avatar" class="img img-responsive" style="position: absolute" />\n';
                        } else {
                            html += '<img src="/resources/images/system/group.jpg" alt="User Avatar" class="img img-responsive" style="position: absolute" />\n';
                        }
                        html +=
                            '</div>\n';
                        if (data.private) {
                            html += '<div class="row">\n' +
                                '<span id="status'+data.users[0].id+'" class="label label-success" style="display: '+status+'">Online</span>\n' +
                                '</div>\n';
                        }
                        html += '</div>\n' +
                            '<div class="col-xs-offset-2 col-xs-7">\n' +
                            '<div class="row" align="left">\n' +
                            '<label class="control-label">' + data.name + '</label>\n' +
                            '</div>\n' +
                            '<div class="row" align="left">\n';
                        if (data.private) {
                            html += '<label class="control-label"><img style="height: 20px" src="' + data.users[0].countryFlag + '">' + data.users[0].city + '</label>\n';
                        }
                        html += '</div>\n' +
                            '<div class="row">\n' +
                            '<span id="dialog' + data.id + 'Unread" class="badge"></span>\n' +
                            '</div>\n' +
                            '</div>\n' +
                            '</div>';
                        $('#dialogs').prepend(html);
                        dialogStart++;
                    }
                    chatClick(data.id, true);
                }
            );
        }else alert("Nobody selected")

    } else alert("Name can't be empty");
}