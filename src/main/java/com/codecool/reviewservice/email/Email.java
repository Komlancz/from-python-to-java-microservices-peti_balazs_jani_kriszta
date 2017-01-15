package com.codecool.reviewservice.email;

import com.codecool.reviewservice.dao.ClientDao;
import com.codecool.reviewservice.dao.ReviewDao;
import com.codecool.reviewservice.dao.implementation.ClientDaoJdbc;
import com.codecool.reviewservice.dao.implementation.ReviewDaoJdbc;
import com.codecool.reviewservice.model.Client;
import com.codecool.reviewservice.model.Review;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
/**
 * The Email class handles everything related to the applications email-sending features.
 * To be able to use the application properly, you need to create a file (emaildata.properties) in the 'resources' directory,
 * and provide the following data: address=youremail@gmail.com, password=youremailaccountpassword
 */

public class Email {
    private static ReviewDao reviews = ReviewDaoJdbc.getInstance();
    private static ClientDao clients = ClientDaoJdbc.getInstance();

    ResourceBundle rb = ResourceBundle.getBundle("emaildata"); // connection.properties
    private final String FROM  = rb.getString("address");
    private final String password = rb.getString("password");

    String to;
    String subject;
    String body;
    /**
     * @return the 'to' email address.
     */
    public String getTo() {
        return to;
    }
    /**
     * @return the 'from' email address.
     */
    public String getFROM() {
        return FROM;
    }
    /**
     * @return the email subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @return the email text
     */
    public String getBody() {
        return body;
    }
        /**
         * @return the password of the sender account.
         */

    public String getPassword() {
        return password;
    }
    /**
     * @param to      Email address of the receiver.
     * @param subject Email address of the sender.
     * @param body    The text of the message.
    */
public Email(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    /**
     * This method get a client object (who registered) and builds the body and subject of an Email object,
     * with the client email address, subject, and body then calls the sendMail() method which sends the email.
     * @param client A Client object.
     */
    public static void newClientEmail (Client client) {
        String subject = "Welcome to the Horseshoe Review Service";
        String body = "Dear Madam/Sir,\n" +
                      "\nThank you for registering your company with the Horseshoe Review Service.\n" +
                      "You can find the details of your registration below: \n" + "\n" +
                      client.toString() +
                      "\nBest regards,\n" +
                      "     Horseshoe Team";

        Email newEmail = new Email(client.getEmail(), subject, body);
        sendEmail(newEmail);
    }
    /**
     *  This method get a review object (which waiting for moderation) and builds the body about two choice
     *  first is accept -generates a 'http://localhost:61000/changeStatus/" + client.getAPIKey() + "/" + review.getReviewKey()+ "/approved"'- link
     *  second is deny - generates a 'http://localhost:61000/changeStatus/" + client.getAPIKey() + "/" + review.getReviewKey()+ "/denied"'- link
     *  and subject of an Email object.
     * then calls the sendMail() method which sends the email.
     * @param review A Review object.
     */
    public static void ReviewForModerationEmail(Review review){
        Client client = clients.getById(review.getClientID());

        String subject = "New review has arrived for moderation";
        String body = "Dear Madam/Sir,\n" +
                      "\nA new product review has been submitted on your site: " + client.getName() +  ". \n" +
                      "You can review it below: \n" +
                      "\n" +
                      review.toString() +

                      "\n\nTo accept the review, click on the link below:\n http://localhost:61000/changeStatus/" + client.getAPIKey() + "/" + review.getReviewKey()+ "/approved" +
                      "\nTo deny it from being displayed on your site, click on this one:\n http://localhost:61000/changeStatus/" + client.getAPIKey() + "/" + review.getReviewKey()+ "/denied" +
                      "\nBest regards,\n" +
                      "     Horseshoe Team";

        Email newEmail = new Email(client.getEmail(), subject, body);
        sendEmail(newEmail);

    }

    /**
     * This method sets the properties of the SMTP connection (Whis is in this case is gmail smtp connection) and then
     * creates a Message object and sends it.
     * @param email An Email object.
     */
    private static void sendEmail(Email email) {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        try {
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email.getFROM(), email.getPassword());
                        }
                    });

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress("myhorseshoeisamazing@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email.getTo(), false));
            msg.setSubject(email.getSubject());
            msg.setText(email.getBody());
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Message sent.");
        } catch (MessagingException e) {
            System.out.println("Error during sending email: " + e);
        }
    }
}