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
import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPSenderFailedException;
import java.io.UnsupportedEncodingException;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * Create a HTML message (with text alternative) for emailing.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class HtmlMessage {

    private SmtpSslConnection connection;

    /**
     * the email message to be sent.
     */
    protected final SMTPMessage msg;
    private InternetAddress rerouteRecipient;

    /**
     * Constructor.
     *
     * @param connection the SMTP connection
     * @param fromemail the from email address
     * @throws EmailException if problems
     */
    public HtmlMessage(SmtpSslConnection connection, String fromemail) throws EmailException {
        this(connection, fromemail, null);
    }

    /**
     * Constructor.
     *
     * @param connection the SMTP connection
     * @param fromemail the from email address
     * @param fromname the display text for the "from email"
     * @throws EmailException if problems
     */
    public HtmlMessage(SmtpSslConnection connection, String fromemail, String fromname) throws EmailException {
        this.connection = connection;
        msg = connection.newMessage();
        try {
            msg.setFrom(new InternetAddress(fromemail, fromname));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting from name and email - %s", ex.getMessage()));
        } catch (UnsupportedEncodingException ex) {
            throw new EmailException(String.format("Mail failure when encoding from name and email - %s", ex.getMessage()));
        }
    }

    /**
     * Change the To email address to this new address to allow change of
     * routing for testing purposes. The message body will be updated to
     * recognise the change, it now contains the original To address and also a
     * warning about this message.
     *
     * @param email the email address to reroute "To"
     * @throws EmailException if problems
     */
    public void rerouteTo(String email) throws EmailException {
        rerouteTo(email, null);
    }

    /**
     * Change the To email address and name to this new address and name to
     * allow change of routing for testing purposes. The message body will be
     * updated to recognise the change, it now contains the original To address
     * and also a warning about this message.
     *
     * @param email the email address to reroute "To"
     * @param name the display text for the reroute "To email"
     * @throws EmailException if problems
     */
    public void rerouteTo(String email, String name) throws EmailException {
        try {
            rerouteRecipient = new InternetAddress(email, name);
        } catch (UnsupportedEncodingException ex) {
            throw new EmailException(String.format("Mail failure when encoding rerouting to name and email - %s", ex.getMessage()));
        }
    }

    /**
     * Set the To email address and associated display name.
     *
     * @param email the To email address
     * @param name the To display name
     * @throws EmailException if problems
     */
    public void to(String email, String name) throws EmailException {
        try {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, name));
        } catch (UnsupportedEncodingException ex) {
            throw new EmailException(String.format("Mail failure when encoding to name and email - %s", ex.getMessage()));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting to name and email - %s", ex.getMessage()));
        }
    }

    /**
     * Set the To email address.
     *
     * @param email the To email address
     * @throws EmailException if problems
     */
    public void to(String email) throws EmailException {
        to(email, null);
    }

    /**
     * Add a CC email address and associated display name.
     *
     * @param email the cc email address
     * @param name the cc display name
     * @throws EmailException if problems
     */
    public void cc(String email, String name) throws EmailException {
        try {
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(email, name));
        } catch (UnsupportedEncodingException ex) {
            throw new EmailException(String.format("Mail failure when encoding cc name and email - %s", ex.getMessage()));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting cc name and email - %s", ex.getMessage()));
        }
    }

    /**
     * Add a CC email address
     *
     * @param email the cc email address
     * @throws EmailException if problems
     */
    public void cc(String email) throws EmailException {
        cc(email, null);
    }

    /**
     * Add a BCC email address and associated display name.
     *
     * @param email the bcc email address
     * @param name the bcc display name
     * @throws EmailException if problems
     */
    public void bcc(String email, String name) throws EmailException {
        try {
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(email, name));
        } catch (UnsupportedEncodingException ex) {
            throw new EmailException(String.format("Mail failure when encoding bcc name and email - %s", ex.getMessage()));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting bcc name and email - %s", ex.getMessage()));
        }
    }

    /**
     * Add a BCC email address.
     *
     * @param email the bcc email address
     * @throws EmailException if problems
     */
    public void bcc(String email) throws EmailException {
        bcc(email, null);
    }

    /**
     * Set the email's subject line.
     *
     * @param subjecttext the subject line text
     * @throws EmailException if problems
     */
    public void subject(String subjecttext) throws EmailException {
        try {
            msg.setSubject(subjecttext);
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting subject - %s", ex.getMessage()));
        }
    }

    /**
     * Set the message content - both html and plain text versions.
     *
     * @param htmlbodytext the html message content
     * @param bodytext the plain text message content
     * @throws EmailException if problems
     */
    public void body(String htmlbodytext, String bodytext) throws EmailException {
        try {
            msg.setContent(alternativeMimeMultipart(htmlbodytext, bodytext));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting body text - %s", ex.getMessage()));
        }
    }

    /**
     * Create the alternativeMimeMultipart message from two text message (html
     * and plain text).
     *
     * @param htmlbodytext the html message content
     * @param bodytext the plain text message content
     * @return the Multipart message content
     * @throws EmailException if problems
     */
    protected Multipart alternativeMimeMultipart(String htmlbodytext, String bodytext) throws EmailException {
        try {
            htmlbodytext = modifiedHtmlBodyWhenRerouting(htmlbodytext);
            bodytext = modifiedBodyWhenRerouting(bodytext);
            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(bodytext, "text/plain; charset=UTF-8");
            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlbodytext, "text/html; charset=UTF-8");
            final Multipart mpt = new MimeMultipart("alternative");
            mpt.addBodyPart(textPart);
            mpt.addBodyPart(htmlPart);
            return mpt;
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting body text - %s", ex.getMessage()));
        }
    }

    /**
     * Update the html message body when rerouting a message. It now contains
     * the original To address and also a warning about this message, in
     * addition to the original message.
     *
     * @param htmlbodytext the original message
     * @return the updated message
     * @throws EmailException if problems
     */
    protected String modifiedHtmlBodyWhenRerouting(String htmlbodytext) throws EmailException {
        try {
            if (rerouteRecipient == null) {
                return htmlbodytext;
            }
            String finalhtmlbodytext = "Target recipients:";
            Address[] rcps = msg.getRecipients(Message.RecipientType.TO);
            if (rcps != null) {
                for (Address ad : rcps) {
                    finalhtmlbodytext += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;To: " + ((InternetAddress) ad).toString();
                }
            }
            rcps = msg.getRecipients(Message.RecipientType.CC);
            if (rcps != null) {
                for (Address ad : rcps) {
                    finalhtmlbodytext += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;Cc: " + ((InternetAddress) ad).toString();
                }
            }
            rcps = msg.getRecipients(Message.RecipientType.BCC);
            if (rcps != null) {
                for (Address ad : rcps) {
                    finalhtmlbodytext += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;Bcc: " + ((InternetAddress) ad).toString();
                }
            }
            finalhtmlbodytext += "<hr/>"
                    + htmlbodytext
                    + "<hr/>This message was created by a development/testing system "
                    + "and should not have been sent to you.<br/>If you receive this message "
                    + "it is due to an error - please ignore and delete this message without further action.\n";
            return finalhtmlbodytext;
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when modifying the html body text when rerouting - %s", ex.getMessage()));
        }
    }

    /**
     * Update the plain text message body when rerouting a message. It now
     * contains the original To address and also a warning about this message,
     * in addition to the original message.
     *
     * @param bodytext the original message
     * @return the modified message
     * @throws EmailException if problems
     */
    protected String modifiedBodyWhenRerouting(String bodytext) throws EmailException {
        if (rerouteRecipient == null) {
            return bodytext;
        }
        try {
            String finalbodytext = "Target recipients:";
            Address[] rcps = msg.getRecipients(Message.RecipientType.TO);
            if (rcps != null) {
                for (Address ad : rcps) {
                    finalbodytext += "\n    To: " + ((InternetAddress) ad).toString();
                }
            }
            rcps = msg.getRecipients(Message.RecipientType.CC);
            if (rcps != null) {
                for (Address ad : rcps) {
                    finalbodytext += "\n    Cc: " + ((InternetAddress) ad).toString();
                }
            }
            rcps = msg.getRecipients(Message.RecipientType.BCC);
            if (rcps != null) {
                for (Address ad : rcps) {
                    finalbodytext += "\n    Bcc: " + ((InternetAddress) ad).toString();
                }
            }
            finalbodytext += "\n---------------------------------------------------\n"
                    + bodytext
                    + "\n---------------------------------------------------\n"
                    + "This message was created by a development/testing system and should not have\n"
                    + "been sent to you. If you receive this message it is due to an error.\n"
                    + "Please ignore and delete this message without further action.\n";
            return finalbodytext;
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when modifying the plain text body text when rerouting - %s", ex.getMessage()));
        }
    }

    /**
     * Modify the recipients if rerouting is requested - removes cc and bcc,
     * updated to.
     *
     * @throws EmailException if problems
     */
    protected void modifyRecipients() throws EmailException {
        try {
            if (rerouteRecipient != null) {
                msg.setRecipients(Message.RecipientType.TO, new InternetAddress[]{rerouteRecipient});
                msg.setRecipients(Message.RecipientType.CC, new InternetAddress[]{});
                msg.setRecipients(Message.RecipientType.BCC, new InternetAddress[]{});
            }
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when modifying the recipients when rerouting - %s", ex.getMessage()));
        }
    }

    /**
     * Send the message.
     *
     * @throws Email451Exception if 451 problem
     * @throws EmailException if problems
     */
    public void send() throws Email451Exception, EmailException {
        modifyRecipients();
        try {
            connection.sendMessage(msg, msg.getAllRecipients());
        } catch (SMTPSendFailedException ex) {
            if (ex.getReturnCode() == 451) {
                throw new Email451Exception(String.format("Send Failure 451 - %s", ex.getMessage()));
            } else {
                throw new EmailException(String.format("Send Failure - failed command is %s; rc=%d3.0", ex.getCommand(), ex.getReturnCode()));
                }
        } catch (SMTPAddressFailedException ex) {
            throw new EmailException(String.format("Sender Address Failure - failed command is %s; rc=%d3.0", ex.getCommand(), ex.getReturnCode()));
        } catch (SMTPSenderFailedException ex) {
            throw new EmailException(String.format("Recepient Address Failure - failed command is %s; rc=%d3.0", ex.getCommand(), ex.getReturnCode()));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when sending message - %s", ex.getMessage()));
        }
    }
}
