# SmsRelay
Android app to forward incoming SMS messages to an XMPP IM receiver

This is a very simple project written over a few hours a late evening just
to get it up and running.

Many internet sites today use so called 2 factor authentication. 
They pretend to strengthen the "security" by sending an SMS with a 
one time password whenever you need to log on, or whenever they
need to "verify your identity". 

In reality, SMS provides very little or none additional security, as
an adversary with ease can get all SMS messages to your phone forwarded
to himself. So the motivation for this trend is likely to be information
harvesting, rather than improved security. A phone number is a very
unique identifier that makes it trivial to correlate data about you from
various sources.

My defence against this is to use one phone number for each site I use that
require this. That means that I have plenty of cheap phones with dual SIM cards,
each used solely to receive SMS messages from one "2 factor autnentication" 
provider (like Twitter, or Google or different corporate VPN providers).
However, it is not practical to carry around with all these phones. So I wrote
this little app for myself to get all the authentication messages forwarded to
a XMPP IM account. That means that I only need to carry a laptop or one phone
with Pidgin (or another XMPP client) in order to receive any authentication
message.

This app is rough in the edges. There are plenty of room for improvements, and 
if there is interest, I may put some more work into it. However, it does the
job for me right now.

#Use 
Install the app. In Android 6 or newer, make sure that the app is granted
access to SMS. Start it, open settings, and enter name, password and XMPP domain.
In "To", enter your XMPP account (for example jgaa@xmpp.example.com) where
the messages will be sent.


# License
SmsRelay is released under GPLv3. It is Free. Free as in Free Beer. Free as in Free Air.

No *In App* advertising. No collecting data. When you use SmsRelay - your privacy is
respected. You are the valued consumer of the product. You yourself is not,
and will never be, the product. That is how *real*, *Free* Software works.
