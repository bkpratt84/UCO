package chat;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.sql.DataSource;
import javax.websocket.Session;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 * Chat Specifications:
 * 
 * The chat should display a list of available professors who have
 * marked their status as online.
 * 
 * The chat should allow students to connect with professors.
 * 
 * The chat should allow students to be visible to professors and vice versa.
 * 
 * The chat can notify users when other users go on and offline.
 * 
 * Chat history must be saved until a session is explicitly closed by
 * each party. Both parties have a separate chat log so one user can't delete
 * the other's chat log by closing the session.
 * 
 * Chat usernames should be tied in some way to realms system user names.
 * 
 * In Progress:
 * The user clicks on a remote user's name and this registers the peer as
 * now talking to this person. ALl message history should be saved in the
 * area for this peer in a separate place from other chat history.
 * 
 * When a user clicks on a remote user's name the chat history should be
 * shown for this user. All chatting taking place should be updating in the
 * background for multiple chats. When the user clicks the button, only the
 * view is changed. Only when the page is re-loaded should all chat contents
 * for all users be re-loaded from the server.
 */

@ApplicationScoped
@ServerEndpoint(value="/chat-server")
public class ChatWebSocketServer {

    /**
     * Constructor is called when the server starts.
     */
    public ChatWebSocketServer() {
        System.out.println("Chat Server Started");
    }

    /**
     * Gets the remote realms user name from an authenticated user.
     * 
     * @param peer
     * @return The user name string
     */
    private String getUserName(Session peer) {
        Principal p = peer.getUserPrincipal();
        
        if ( p != null  ) {
            String userName = p.toString();
            System.out.println("Chat Remote User Name: " + userName);

            return userName;
        }

        System.out.println("Chat Remote User is Unauthenticated!");
        return "";
    }

    /**
     * Gets some defined username alias.
     * 
     * @param peer
     * @return 
     */
    private String getUserNameAlias(Session peer) {
        return (String)peer.getUserProperties().get("username");
    }

    /**
     * Sets a username alias.
     * 
     * @param peer
     * @param alias 
     */
    private void setUserNameAlias(Session peer, String alias) {
        peer.getUserProperties().put("username", alias);
    }

    /**
     * Returns the user ID for a specific peer. This is tied to the username.
     * 
     * @param peer
     * @return 
     */
    private String getPeerId(Session peer) {
        return Integer.toString(peer.getUserPrincipal().hashCode());
    }

    /**
     * Determine if a given peer belongs to a specified user group.
     * 
     * @param peer
     * @param group
     * @return
     * @throws SQLException 
     */
    private HashMap<String, Boolean> belongsToGroup(Session peer) throws SQLException {

        Connection c = ds.getConnection();
        ResultSet rs = null;

        String userName = peer.getUserPrincipal().getName();

        HashMap<String, Boolean> membership = new HashMap();

        try {
            PreparedStatement query = 
                c.prepareStatement(
                    "select username, groupname " +
                    "from user_groups where username = ?"
                );

            query.setString(1, userName);
            rs = query.executeQuery();
        }
        finally {
            c.close();
        }

        if ( rs != null ) {
            while ( rs.next() ) {
                String groupName = rs.getString(2).toLowerCase();
                membership.put(groupName, Boolean.TRUE);
            }
        }

        return membership;
    }

    /**
     * Saves the message history to the database.
     * 
     * @param localID
     * @param remoteID
     * @param msg 
     */
    private void saveMessageHistory(String localID, 
            String localUsername, String remoteUsername,
            String remoteID, String msg) throws SQLException {

        Connection c = ds.getConnection();
        try {
            PreparedStatement query = 
                c.prepareStatement(
                    "insert into chat_history " +
                    "(local_id, local_username, remote_username, remote_id, message)" +
                    " values (?, ?, ?, ?, ?)"
                );

            query.setString(1, localID);
            query.setString(2, localUsername);
            query.setString(3, remoteUsername);
            query.setString(4, remoteID);
            query.setString(5, msg);

            query.executeUpdate();

            query.setString(1, remoteID);
            query.setString(2, localUsername);
            query.setString(3, localUsername);
            query.setString(4, localID);

            query.executeUpdate();
        }
        finally {
            c.close();
        }
    }

    /**
     * Loads the message from the database and stores it in an array list.
     * 
     * @param localID
     * @return 
     */
    private ArrayList<Message> loadMessageHistory(String localID) throws SQLException {
        ArrayList <Message> history = new ArrayList();
        ResultSet rs = null;

        Connection c = ds.getConnection();

        try {
            PreparedStatement query = 
                c.prepareStatement(
                    "select msg_id, local_id, local_username, remote_username, remote_id, message " +
                    "from chat_history where local_id = ? " +
                    "order by msg_id"
                );

            query.setString(1, localID);

            rs = query.executeQuery();
        }
        finally {
            c.close();
        }

        if ( rs != null ) {

            while (  rs.next() ) {
                String local_username  = rs.getString(3);
                String remote_username = rs.getString(4);
                String remote_id       = rs.getString(5);
                String message         = rs.getString(6);

                Message m = new Message();

                m.setUserName(local_username);
                m.setRemoteUserName(remote_username);
                m.setUserID(remote_id);
                m.setMessage(message);

                history.add(m);
                
            }

        }

        return history;
    }

    /**
     * Deletes all message history between two users in the database.
     * 
     * @param localID
     * @param remoteID 
     */
    private void deleteHistory(String localID, String remoteID) throws SQLException {
        Connection c = ds.getConnection();
        
        try {
            PreparedStatement query =
                c.prepareStatement(
                    "delete from chat_history " +
                    "where local_id = ? and remote_id = ?"
                );
            
            query.setString(1, localID);
            query.setString(2, remoteID);
            
            query.executeUpdate();
        }
        finally {
            c.close();
        }
        
    }

    /**
     * Automatically called when a WebSockets message arrives.
     * 
     * @param message Message from a client
     * @param peer
     * @throws IOException 
     * @throws java.sql.SQLException 
     */
    @OnMessage
    public void onMessage(String message, Session peer) throws IOException, SQLException {

        JsonObject jo = Json.createReader(
            new StringReader(message)
        ).readObject();

        // JSON fields sent back from the browser
        String chatMsg = null;
        if ( jo.getJsonString("ChatMessage") != null ) {
            chatMsg = jo.getString("ChatMessage"); 
        }

        String recipientUserID = null;
        if ( jo.getJsonString("RecipientUserID") != null )  {
            recipientUserID = jo.getString("RecipientUserID");
        }

        String messageReceived = null;
        if ( jo.getJsonString("MessageReceived") != null )  {
            messageReceived = jo.getString("MessageReceived");
        }

        String deleteHistory = null;
        if ( jo.getJsonString("DeleteHistory") != null )  {
            deleteHistory = jo.getString("DeleteHistory");
        }

        if ( deleteHistory != null )  {
            String rUserID = deleteHistory;
            deleteHistory(getPeerId(peer), rUserID);
        }

        if ( (chatMsg != null) && (chatMsg.trim().length() > 0) &&
                peer.getUserProperties().containsKey("RecipientUserID") ) {

            String rUserID = (String)peer.getUserProperties().get("RecipientUserID");
            String peerID  = getPeerId(peer);

            String sender    = getUserNameAlias(peer);
            String recipient = "Unknown";

            for (Session p : peers) {
                if ( getPeerId(p).equals(rUserID) ) {
                    recipient = getUserNameAlias(p);
                }
            }

            // Add the message to the log
            saveMessageHistory(peerID, sender, recipient, rUserID, chatMsg);

            configUnread(rUserID, peerID, true);

            // Broadcast message to all peers
            for ( Session p: peers ) {
                if ( getPeerId(p).equals(rUserID) || (getPeerId(p).equals(peerID)) ) {
                    //peer.getBasicRemote().sendText(message + "</br>");
                    p.getBasicRemote().sendText( 
                        buildJSON("ChatMessage", chatMsg, peer) 
                    );
                }
            }
        }

        // Configure the recipient of messages based on a user's selection
        // in the browser when the click the username
        if ( (recipientUserID != null) && (recipientUserID.trim().length() > 0) ) {

            peer.getUserProperties().put("RecipientUserID", recipientUserID);

            System.out.println("Recipient User ID: " + recipientUserID);
        }

        // Tell all browesers this user has been viewd and the new
        // message status should be cleared
        if ( (messageReceived != null) && (messageReceived.trim().length() > 0) ) {

            String pid = getPeerId(peer);
            configUnread(pid, messageReceived, false);

            for (Session p : peers) {
                if ( getPeerId(p).equals(pid) ) {
                    p.getBasicRemote().sendText( 
                        buildJSON("MessageReceivedAck", "", "", messageReceived) 
                    );
                }
            }
        }

    }

    /**
     * Automatically is called when a WebSocket session is opened by the
     * remote client.
     * 
     * @param peer
     * @throws IOException 
     * @throws java.sql.SQLException 
     */
    @OnOpen
    public void onOpen(Session peer) throws IOException, SQLException {
        peers.add(peer);

        String newUserName = getUserName(peer);
        setUserNameAlias(peer, newUserName);

        String peerID = getPeerId(peer);

        HashMap<String, Boolean> group = belongsToGroup(peer);

        ArrayList<Message> message = loadMessageHistory(getPeerId(peer));
        HashMap<String, Boolean> hist = new HashMap();

        for ( Message m : message ) {
            System.out.println( m.getMessage() );

            peer.getBasicRemote().sendText(
                buildJSON("ChatMessage", m.getMessage(), m.getUserName(), m.getUserID())
            );

            if ( (m.getUserID().equals(getPeerId(peer)) == false) ) {

                if ( hist.containsKey(m.getUserID()) == false ) {

                    // Add users if there is history with them
                    peer.getBasicRemote().sendText(
                        buildJSON("UserStateChange", "HistoryWithUser", m.getRemoteUserName(), m.getUserID())
                    );
                }

                hist.put(m.getUserID(), Boolean.TRUE);
            }

        }

        for (Session p : peers) {
            String id = getPeerId(p);

            if ( id.equals(peerID) == false ) {

                HashMap<String, Boolean> otherGroup = belongsToGroup(p);

                // Only let students see instructors
                if ( group.containsKey("student") ) {
                    if ( otherGroup.containsKey("instructor") == false ) {
                        continue;
                    }
                }
                // Only let instructors see students
                else if ( group.containsKey("instructor") ) {
                    if ( otherGroup.containsKey("student") == false ) {
                        continue;
                    }
                }
                else {
                    continue;
                }

                // Announce to all peers that a new user has joined
                p.getBasicRemote().sendText(
                    buildJSON("UserStateChange", "UserOnline", newUserName, peerID)
                );

                // Add all connected peers to new user's list
                peer.getBasicRemote().sendText(
                    buildJSON("UserStateChange", "PopulateUserList", getUserNameAlias(p), id)
                );
            }
        }

        peer.getBasicRemote().sendText(
            buildJSON("UserID", "", peer)
        );

                
        // Show all unread messages indicators to new connected peers
        if ( newMessage.containsKey(getPeerId(peer)) ) {
            for ( Object s : newMessage.get(getPeerId(peer)).keySet() ) {
                
                String otherPeer = (String)s;
                
                if ( getUnread( getPeerId(peer), otherPeer ) ) {
                    peer.getBasicRemote().sendText(
                            buildJSON("Unread", "", "", otherPeer)
                    );
                }
            }
        }

    }

    /**
     * Automatically called when a WebSocket session is closed by the
     * remote client.
     * 
     * @param peer 
     * @throws java.io.IOException 
     */
    @OnClose
    public void onClose(Session peer) throws IOException {
        peers.remove(peer);

        String userName = getUserNameAlias(peer);
        String id = getPeerId(peer);

        boolean allGone = true;

        for (Session p : peers) {
            if ( getPeerId(p).equals(id) ) {
                allGone = false;
            }
        }

        if ( allGone ) {
            // Announce to all peers that a user has left
            for (Session p : peers) {
                if ( p != peer ) {
                    p.getBasicRemote().sendText(
                        buildJSON("UserStateChange", "UserOffline", userName, id)
                    );
                }
            }
        }
    }

    /**
     * Sets the unread message status between one peer and another.
     * If the users are not already in the map, then allocate the storage.
     * 
     * @param localPeer
     * @param remotePeer
     * @param status 
     */
    private void configUnread(String localPeer, String remotePeer, boolean status) {
        if ( newMessage.containsKey(localPeer) == false ) {
            newMessage.put(localPeer, new HashMap());
        }
        HashMap<String, Boolean> a = newMessage.get(localPeer);

        newMessage.get(localPeer).put(remotePeer, status);
    }

    /**
     * Returns the unread message status between one peer and another.
     * 
     * @param localPeer
     * @param remotePeer
     * @return 
     */
    private boolean getUnread(Session localPeer, Session remotePeer) {
        return getUnread(getPeerId(localPeer), getPeerId(remotePeer));
    }

    private boolean getUnread(String localPeer, String remotePeer) {
        if ( newMessage.containsKey(localPeer) &&
            newMessage.get(localPeer).containsKey(remotePeer) ) {

            return (boolean)newMessage.get(localPeer).get(remotePeer);
        }
        return false;
    }

    /**
     * Automatically called when an error occurs with a WebSocket.
     * 
     * @param t 
     */
    //@OnError
    //public void onError(Throwable t) {
    //    System.out.println("Chat Server Error: "  + t.toString());
    //}

    // Hashtable which holds all remote peers/clients connected
    // to the server
    private static final Set<Session> peers = 
        Collections.synchronizedSet(new HashSet());

    private static final Map<String, HashMap> newMessage = 
        Collections.synchronizedMap(new HashMap<String, HashMap>());

    private String buildJSON(String type, String data, Session peer) {
        return buildJSON(type, data, getUserNameAlias(peer), getPeerId(peer));
    }

    private String buildJSON(String type, String data, String user, String sessionID) {
        JsonObjectBuilder o = Json.createObjectBuilder();

        o.add("Type", type);
        o.add("Data", data);
        o.add("User", user);
        o.add("SessionID", sessionID);

        return o.build().toString();
    }

    // Database access
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;

}





