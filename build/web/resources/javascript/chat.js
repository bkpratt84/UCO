
var chatRecipientUserID = "";
var ourUserID = "";
var ongoingChatText = {};

// Establish a WebScoket connection to the server
if ( "WebSocket" in window ) {
   var webSocketURI = 
       "ws://" + document.location.host + "/sdd-project/chat-server";

    var webSocket = new WebSocket(webSocketURI);

    webSocket.onopen = function(message) {
    };

    webSocket.onmessage = function(message) {
        var messageJson = JSON.parse(message.data);
        messageHandler(messageJson);
    };

    webSocket.onerror = function(message) {
    };

    webSocket.close = function(message) {

    };
}
else {
    alert("WebSockets are not supported in this browser!");
}

// Determines the correct action to take based off the WebSocket message
function messageHandler(messageJson) {

    var type = messageJson.Type;
    var data = messageJson.Data;
    var user = messageJson.User;
    var userID = messageJson.SessionID;
    
    switch ( type ) {

        // Chat message received
        case "ChatMessage" :
        {
            if ( userID && !ongoingChatText[userID] ) {
                ongoingChatText[userID] = "";
            }

            if ( chatRecipientUserID && !ongoingChatText[chatRecipientUserID] ) {
                ongoingChatText[chatRecipientUserID] = "";
            }

            if ( chatRecipientUserID && (ourUserID === userID) ) {
                ongoingChatText[chatRecipientUserID] += styleMessage(user, data);
            }
            else {
                ongoingChatText[userID] += styleMessage(user, data);

                if ( userIsOnline(userID) ) {
                    chatMessageReceivedHilight(userID);
                }
            }

            if ( (chatRecipientUserID === userID) || (ourUserID === userID) ) {
                appendToChat(user, data);
            }
            break;
        }

        case "MessageReceivedAck" :
        {
            clearChatUserHilight(userID);

            if ( chatRecipientUserID === userID ) {
                chatUserHilight(userID);
            }
            break;
        }

        case "Unread" :
        {
            chatMessageReceivedHilight(userID);
            break;
        }

        // Add/remove user name from list as they go on/off line
        case "UserStateChange" :
        {
            // Determine the type of state change
            switch ( data ) {

                case "PopulateUserList" :
                {
                    if ( !( $( "#" + userID ).length ) ) {
                        addUserToList(user, userID);
                    }
                    showUserOnlineIcon(userID);
                    break;
                }

                case "UserOnline" :
                {
                    // Add user if they are not alread listed
                    if ( !( $( "#" + userID ).length ) ) {
                        addUserToList(user, userID);
                    }
                    showUserOnlineIcon(userID);

                    if ( chatRecipientUserID === userID ) {
                        enableMessageControls(true);
                    }

                    break;
                }

                case "UserOffline" :
                {
                    if ( !ongoingChatText[userID] ) {
                        removeUserFromList(userID);
                    }
                    else {
                        hideUserOnlineIcon(userID);
                    }

                    if ( chatRecipientUserID === userID ) {
                        enableMessageControls(false);

                        $( "#chattingWith" ).html("Nobody");
                    }

                    break;
                }

                case "HistoryWithUser" :
                {
                    if ( !( $( "#" + userID ).length ) ) {
                        addUserToList(user, userID);
                    }
                    break;
                }

            }
            break;
        }

        case "UserID" :
        {
            ourUserID = userID;
            break;
        }

        default :
        {
            appendToChat("Invalid Received Data");
            break;
        }

    }

}

function removeUserFromList(userID) {
    $( "#" + userID ).remove();
}

function addUserToList(userName, userID) {

    var row = 
        '<tr ' + 'id = "' + userID + '" >' +
            '<td>' +
                 '<div class="btn-group btn-group-justified">' +
                    '<div class="btn-group">' +
                            '<button class="btn btn-default userNameButton">' + 
                            '<span class="glyphicon glyphicon-user user-online-icon" aria-hidden="true" ' + 
                                'style="display: none">&nbsp;</span>' +
                                userName.substring(0, 10) + 
                            '</button>' +
                    '</div>' +
                    '<div class="btn-group">' +
                        '<button class="btn btn-default deleteHistoryButton">Delete History</button>' +
                    '</div>' +
                 '</div>' +
            '</td>' +
        '</tr>';

   $( "#userListTable tr:last" ).after(row);

}

function scrollDownChat() {
    if ( $( "#chatText" ).length )
    {
        $( "#chatText" ).scrollTop($( "#chatText" )[0].scrollHeight - $( "#chatText" ).height());
    }
}

function styleMessage(name, message) {
    var msgWrapper = 
        '<button class="btn btn-primary" type="button"> ' +
        name + ':</button>' +

        '<button class="btn btn-default" type="button"> ' +
        message + '</button>' +
        '</br></br>';

    return msgWrapper;
}

// Append a message to the chat box
function appendToChat(name, message) {
    $( "#chatText" ).append( styleMessage(name, message) );
    scrollDownChat();
}

// Initiates sending data to the WebSocket
function sendText() {
    if ( ($( "#chatInput" ).val().trim().length > 0) ) {
        
        var message = $( "#chatInput" ).val();
        $( "#chatInput" ).val("");

        var jsonString = JSON.stringify( {"ChatMessage" : message} );
        webSocket.send(jsonString);
    }
}

function messageReceived(id) {
    var jsonString = JSON.stringify( {"MessageReceived" : id} );
    webSocket.send(jsonString);
}

// Sends a message when the send button is pressed
// on the message entry field.
$( "#chatSendButton" ).click( function() {
    sendText();
    clearChatUserHilight(chatRecipientUserID);
    chatUserHilight(chatRecipientUserID);
    messageReceived(chatRecipientUserID);
});

// Sends a message when the enter key is pressed
// on the message entry field.
$( "#chatInput" ).keypress( function(event) {
    if ( event.which === 13 ) {
        event.preventDefault();
        sendText();

        clearChatUserHilight(chatRecipientUserID);
        chatUserHilight(chatRecipientUserID);
        messageReceived(chatRecipientUserID);
    }
});

// Username clicked
$( document.body ).on( "click", ".userNameButton", function() {
    var id = $( this ).closest( "tr" ).attr( "id" );

    clearChatUserHilight(chatRecipientUserID);
    chatRecipientUserID = id;
    chatUserHilight(chatRecipientUserID);

    var jsonString = JSON.stringify( {"RecipientUserID" : id} );

    webSocket.send(jsonString);

    $( "#chatText" ).empty();
    $( "#chatText" ).append(ongoingChatText[id]);
    scrollDownChat();

    messageReceived(chatRecipientUserID);

    $( "#chattingWith" ).html( $(this).html() );

    if ( userIsOnline(id) ) {
        enableMessageControls(true);
    }
});

$( document.body ).on( "click", ".deleteHistoryButton", function() {
    var id = $( this ).closest( "tr" ).attr( "id" );


    var yesNo = 
        '<div class="btn-group">' +
        '<button class="btn btn-danger yes-history-delete">Delete</button>' +
        '</div>' +

        '<div class="btn-group">' +
        '<button class="btn btn-success no-history-delete">Keep</button>' +
        '</div>';

    $( yesNo ).insertAfter( $( "#" + id + " .deleteHistoryButton" ) );

    $( "#" + id + " .deleteHistoryButton" ).unwrap();
    $( "#" + id + " .deleteHistoryButton" ).remove();
});

function restoreDeleteHistory(id) {
    var deleteHistory = 
        '<div class="btn-group">' +
        '<button class="btn btn-default deleteHistoryButton">Delete History</button>' +
        '</div>';

    $( deleteHistory ).insertAfter( $( "#" + id + " .no-history-delete" ) );

    $( "#" + id + " .yes-history-delete" ).unwrap();
    $( "#" + id + " .yes-history-delete" ).remove();

    $( "#" + id + " .no-history-delete" ).unwrap();
    $( "#" + id + " .no-history-delete" ).remove();
}

// Abort delete
$( document).on( "click", ".no-history-delete", function() {
    var id = $( this ).closest( "tr" ).attr( "id" );
    restoreDeleteHistory(id);
});

// Confirmation click delete
$( document).on( "click", ".yes-history-delete", function() {
    var id = $( this ).closest( "tr" ).attr( "id" );
    deleteHistory(id);
    restoreDeleteHistory(id);
});

// Delete history button clicked
function deleteHistory(id) {

    var jsonString = JSON.stringify( {"DeleteHistory" : id} );
    webSocket.send(jsonString);

    if ( chatRecipientUserID === id ) {
        $( "#chatText" ).empty();
    }

    if ( userIsOnline(id) === false ) {
        removeUserFromList(id);
    }

    ongoingChatText[id] = "";
}

function chatMessageReceivedHilight(id) {

    if ( $( "#" + id + " .userNameButton" ).hasClass("btn-success") === false ) {
       $( "#" + id + " .userNameButton" ).removeClass("btn-primary");
       $( "#" + id + " .userNameButton" ).removeClass("btn-default");

       $( "#" + id + " .userNameButton" ).addClass("btn-success");
   }

}

function chatUserHilight(id) {

       if ( $( "#" + id + " .userNameButton" ).hasClass("btn-primary") === false ) {
           $( "#" + id + " .userNameButton" ).removeClass("btn-success");
           $( "#" + id + " .userNameButton" ).removeClass("btn-default");

           $( "#" + id + " .userNameButton" ).addClass("btn-primary");
       }

}

function clearChatUserHilight(id) {

    if ( id ) {

       if ( $( "#" + id + " .userNameButton" ).hasClass("btn-default") === false ) {
           $( "#" + id + " .userNameButton" ).removeClass("btn-success");
           $( "#" + id + " .userNameButton" ).removeClass("btn-primary");

           $( "#" + id + " .userNameButton" ).addClass("btn-default");
       }
   }

}

function enableMessageControls(enable) {
    $( "#chatInput" ).prop("disabled", !enable);
    $( "#chatSendButton" ).prop("disabled", !enable);
}

function showUserOnlineIcon(id) {
    $( "#" + id + " .user-online-icon").show("slow");
}

function hideUserOnlineIcon(id) {
    $( "#" + id + " .user-online-icon").hide("slow");
}

function userIsOnline(id) {
    return $( "#" + id + " .user-online-icon").is(":visible");
}
