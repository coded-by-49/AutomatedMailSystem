package com.example.automatedemailsender;

import java.util.Properties;
import java.util.Date;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import java.io.FileInputStream;
import java.io.IOException;


public class Main{
    void main() {
        Properties props = new Properties();
        System.setProperty("javax.net.debug", "ssl:handshake");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.from","49learns@gmail.com");
        props.put("mail.transport.protocol.address-type","rfc822");
        props.put("mail.smtp.auth",true);

        // setting up ssl
        SocketFactory myFactory = SSLSocketFactory.getDefault();
        props.put("mail.smtp.socketFactory", myFactory);
        props.put("mail.smtp.socketFactory.port",465);
        props.put("mail.smtp.ssl.enable",true);
        props.put("mail.smtp.starttls.required",true);

        Session session = Session.getInstance(props, null);

        // program secrets and encryption
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")){
            config.load(fis);
        }
        catch (IOException e) {
            System.out.println("Error: config.properties file not found");
            return;
        }

        String myEmail = config.getProperty("app.MyEmail");
        String myPassword = config.getProperty("app.password");
        String recipientMail = config.getProperty("app.recepientMail");

        try {
            // setting up the message via the message module
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(myEmail);
            msg.setRecipients(Message.RecipientType.TO,recipientMail);
            msg.setSubject("Jakarta Mail hello world example");
            msg.setSentDate(new Date());
            msg.setText("Hello, world! fabian is running test one\n");
            msg.saveChanges();

            // setting up the transport module
            Transport transport  = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "fabianbernard49@gmail.com",myPassword);
            transport.sendMessage(msg, msg.getAllRecipients());

            transport.close();
        }
        catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }

    }
}
