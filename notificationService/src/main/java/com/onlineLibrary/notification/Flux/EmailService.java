package com.onlineLibrary.notification.Flux;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("myappnotifier13000@gmail.com"); // expéditeur
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

//    /**
//     * Envoie une notification paiement done.
//     */
//    public void sendPaymentDoneNotification(String to, String firstName, double amount) {
//        String subject = "Confirmation de paiement";
//        String body = String.format(
//                "Bonjour %s,\n\nVotre paiement de %.2f $ a bien été pris en compte.\nMerci pour votre confiance.\n\nCordialement,\nL'équipe Online Library",
//                firstName, amount
//        );
//        sendEmail(to, subject, body);
//    }
}
