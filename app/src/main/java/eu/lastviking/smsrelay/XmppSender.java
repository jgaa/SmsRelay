package eu.lastviking.smsrelay;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by jgaa on 07.09.16.
 */
public class XmppSender {

    AbstractXMPPConnection conn;
    ChatManager chatmanager;
    Chat chat;

    public XmppSender() throws IOException, XMPPException, SmackException {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.ctx);
        String id = sp.getString(SettingsActivity.XMPP_ID,"");
        String pwd = sp.getString(SettingsActivity.XMPP_PWD,"");
        String domain = sp.getString(SettingsActivity.XMPP_DOMAIN,"");
        String to = sp.getString(SettingsActivity.XMPP_TO,"");

        conn = new XMPPTCPConnection(id, pwd, domain);
        conn.setPacketReplyTimeout(1000 * 20);
        conn.connect();
        conn.login();
        chatmanager = ChatManager.getInstanceFor(conn);
        chat = chatmanager.createChat(to, new ChatMessageListener() {

            @Override
            public void processMessage(Chat chat, Message message) {
                Log.d("SmsRelay","Received message: " + message);
            }
        });
    }

    public void sendMessage(final String message) throws SmackException.NotConnectedException {
        chat.sendMessage(message);
    }

    public void close() {
        if (conn.isConnected()) {
            conn.disconnect();
        }
    }
}
