package com.booking.ISAbackend.email;

import com.booking.ISAbackend.confirmationToken.ConfirmationToken;
import com.booking.ISAbackend.dto.ReservationParamsDTO;
import com.booking.ISAbackend.model.Reservation;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

@Service
public class EmailService implements EmailSender{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendConfirmationAsync(String email, String token) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Primer slanja emaila pomoću Spring taska");
        String link = "https://spring-back-isa-mrs.herokuapp.com/confirmation?token=" + token;
        mail.setText("Pozdrav hvala što pratiš ISA, aktiviraj svoj account na " + link + ".");
        javaMailSender.send(mail);

    }

    @Async
    public void sendConfirmationRegistrationRequest(String email) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response for registration request");
        mail.setText("Hello, your registration request has been accepted. You can use our application now. Good Luck!");
        javaMailSender.send(mail);

    }
    @Async
    public void sendRejectionRegistrationRequest(String email, String message) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response for registration request");
        mail.setText("Hello, your registration request has been denied with the following explanation.\n" + message);
        javaMailSender.send(mail);
    }

    @Async
    public void reservationConfirmation(ReservationParamsDTO params) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(params.getEmail());
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Reservation confrimation");
        mail.setText("You have successfully made reservation.\n");
        javaMailSender.send(mail);
    }
    @Async
    public void notifySubscribersNewQuickReservation(String email, String offerName, String date) throws MailException{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("New special offer!!!");
        mail.setText("The new special offer will be valid from " + date +". Hurry up and book your favorite "+ offerName+"!\n");
        javaMailSender.send(mail);
    }

    @Async
    public void notifyClientNewReservation(String email, Reservation reservation) throws MailException{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Successful reservation!!!");
        mail.setText("A new reservation has been made for you! Reservation starts from " + reservation.getStartDate().toString() + " and lasting up to " + reservation.getEndDate().toString() + " for offer " + reservation.getOffer().getName() +".\n");
        javaMailSender.send(mail);
    }

    @Async
    public void notifyUserAboutMark(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Review information");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Async
    public void notifyNewAdmin(String email, String password) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("New admin registration");
        String message = "You have been added as one of admins in our application. Your password for first login is: " + password +
                " . After first login you have to change password.";
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Async
    public void notifyUserAboutReservationReport(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response for reservation report");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Async
    public void sendResponseOnComplaint(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response on complaint");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Async
    public void notifyUserForDeleteAccount(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response on delete account request");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Async
    public void notifyClientDeleteOffer(String clientEmail, String offerName){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(clientEmail);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Delete offer");
        mail.setText("The offer (" + offerName+ ") you subscribed to has been deleted!");
        javaMailSender.send(mail);
    }


}