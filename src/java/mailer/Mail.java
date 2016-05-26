package mailer;

import com.sun.mail.smtp.SMTPTransport;
import java.security.Security;
import java.util.Date;
import java.util.Properties;
import javax.faces.bean.RequestScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
    
 
    public void Send(String recipientEmail, String title, String message) throws AddressException, MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        String m_from = "CentralSoftUCO@gmail.com";
        String password = "UCOCentralSoft";
        
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        final MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(m_from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        msg.setSubject(title);
        msg.setText(message, "utf-8");
        msg.setSentDate(new Date());
        msg.setContent(message, "text/html");

        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
        
        t.connect("smtp.gmail.com", m_from, password);
        t.sendMessage(msg, msg.getAllRecipients());     
        t.close();
    }
}
