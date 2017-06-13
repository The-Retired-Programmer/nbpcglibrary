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

import java.io.File;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * Create a HTML message (with text alternative) for emailing and allow
 * attaching of other files to this message.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class HtmlMessageWithAttachments extends HtmlMessage {

    private final Multipart mpa;
    private final MimeBodyPart mbpBody;

    /**
     * Constructor.
     *
     * @param connection the SMTP connection
     * @param fromemail the from email address
     * @throws EmailException if problems
     */
    public HtmlMessageWithAttachments(SmtpSslConnection connection, String fromemail) throws EmailException {
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
    public HtmlMessageWithAttachments(SmtpSslConnection connection, String fromemail, String fromname) throws EmailException {
        super(connection, fromemail, fromname);
        mpa = new MimeMultipart();
        mbpBody = new MimeBodyPart();
        try {
            mpa.addBodyPart(mbpBody);
            msg.setContent(mpa);
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when creating message with attachments- %s", ex.getMessage()));
        }
    }

    @Override
    public void body(String htmlbodytext, String bodytext) throws EmailException {
        try {
            mbpBody.setContent(this.alternativeMimeMultipart(htmlbodytext, bodytext));
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when setting body text- %s", ex.getMessage()));
        }
    }

    /**
     * Attached a file to this message.
     *
     * @param file the file to be attached
     * @throws IOException if problems
     * @throws EmailException if problems
     */
    public void attachFile(File file) throws IOException, EmailException {
        MimeBodyPart attachment = new MimeBodyPart();
        try {
            attachment.attachFile(file);
            mpa.addBodyPart(attachment);
        } catch (MessagingException ex) {
            throw new EmailException(String.format("Mail failure when attaching file - %s", ex.getMessage()));
        }
    }
}
