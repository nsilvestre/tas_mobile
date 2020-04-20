package com.company.framework.utils;

import com.company.framework.base.BaseUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EMail {

    public static void sendEmail(String attachmentPath, String attachmentName)throws IOException{
        final String username = "thesufferfestappium@gmail.com";
        final String password = "Punisher85";
        String mailList = BaseUtil.getMailList();
        String[] mails = mailList.split(",");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        for(int i=0; i<mails.length; i++) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("thesufferfestappium@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(mails[i]));
                message.setSubject("AppiumTestReport " + BaseUtil.getDate());
                message.setText("Dear SufferFest Tester," + "\n\n Test report attached!");
                message.setSubject("Testing Subject");
                message.setText("PFA");

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                messageBodyPart = new MimeBodyPart();


                File att = new File(new File(attachmentPath), attachmentName);
                messageBodyPart.attachFile(att);

                DataSource source = new FileDataSource(att);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attachmentName);
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);

                System.out.println("Sending");
                Transport.send(message);
                Transport.send(message);
                System.out.println("Done");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
