/*
 * Copyright 2015-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.email;

import com.sun.mail.smtp.SMTPMessage;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * The SMTP SSL connection class.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class SmtpSslConnection {

    private Session session;
    private Transport transport;

    /**
     * Constructor.
     *
     * @param server the SMTP server
     * @param port the SMTP server port
     * @param user the user name (for authentication)
     * @param pw the user password (for authentication)
     * @throws EmailException if problems
     */
    public SmtpSslConnection(String server, int port, String user, String pw) throws EmailException {
        this(server, port, user, pw, false);
    }

    /**
     * Constructor.
     *
     * @param server the SMTP server
     * @param port the SMTP server port
     * @param user the user name (for authentication)
     * @param pw the user password (for authentication)
     * @param enableDebug if true enable debug trace/reporting
     * @throws EmailException if problems
     */
    public SmtpSslConnection(String server, int port, String user, String pw, boolean enableDebug) throws EmailException {
        session = Session.getInstance(new Properties());
        session.setDebug(enableDebug);
        try {
            transport = session.getTransport("smtps");
            transport.connect(server, port, user, pw);
        } catch (NoSuchProviderException ex) {
            throw new EmailException("SMTPS transport provider not available");
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Transport error - %s", ex.getMessage()));
        }
    }

    /**
     * Disconnect the connection.
     *
     * @throws EmailException if problems
     */
    public void disconnect() throws EmailException {
        try {
            transport.close();
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Transport error - %s", ex.getMessage()));
        }
    }

    /**
     * Get a new message object for use on this connection.
     *
     * @return the SMTP message object
     */
    public SMTPMessage newMessage() {
        return new SMTPMessage(session);
    }

    /**
     * Send a message using this connection.
     *
     * @param msg the SMTP message
     * @param recipients the set of recipients
     * @throws EmailException if problems
     */
    public void sendMessage(Message msg, Address[] recipients) throws EmailException {
        try {
            msg.setSentDate(new Date());
            msg.saveChanges();
            transport.sendMessage(msg, recipients);
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Transport error - %s", ex.getMessage()));
        }
    }
}
