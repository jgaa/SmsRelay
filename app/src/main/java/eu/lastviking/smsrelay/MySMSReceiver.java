package eu.lastviking.smsrelay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

class MySMSReceiver extends BroadcastReceiver {

    XmppSender xmpp = null;

    private class XmppTask extends AsyncTask<String, String, String> {

        final int retries = 20;

        @Override
        protected String doInBackground(String[] params) {
            try {
                synchronized (this) {
                    if (xmpp == null) {
                        for(int i = 0; (i < retries) && (xmpp == null); ++i) {
                            try {
                                xmpp = new XmppSender();
                            } catch(Throwable ex) {
                                Log.d("SmsRelay", "Failed to connect " + ex.toString() + (i < retries ? " retrying" : "Fatal"));
                                publishProgress("Failed to connect " + ex.toString() + (i < retries ? " retrying" : "Fatal"));
                                if (i >= retries) {
                                    throw ex;
                                }
                            }
                        }
                    }
                }

                xmpp.sendMessage("SMS from " + params[0].toString() + "\r\n" + (String)params[1]);
                return "Sent xmpp message: " + params[1];
            } catch(Throwable ex) {
                return "Failed to send xmpp message: " + ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                MainActivity.tell(result);
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            MainActivity.tell(progress[0]);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String body = "";
                String from = "";
                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // Get sender phone number
                    if (from.isEmpty()) {
                        from = sms.getDisplayOriginatingAddress();
                    }
                    body += sms.getDisplayMessageBody();
                }

                if (!body.isEmpty()) {
                    Log.d("SmsRelay", "SMS message sender: " + body);
                    MainActivity.tell("Incoming sms: " + body);
                    new XmppTask().execute(from, body);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}