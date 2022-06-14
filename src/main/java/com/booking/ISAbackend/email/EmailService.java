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

    /*
     * Koriscenje klase za ocitavanje vrednosti iz application.properties fajla
     */
    @Autowired
    private Environment env;

    /*
     * Anotacija za oznacavanje asinhronog zadatka
     * Vise informacija na: https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling
     */
    @Async
    public void sendConfirmationAsync(String email, String token) throws MailException, InterruptedException {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Primer slanja emaila pomoću asinhronog Spring taska");
        String link = "http://localhost:8081/confirmation?token=" + token;
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

    @Override
    public void notifyCliendDiscardMark(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Review rejected");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Override
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

    @Override
    public void notifyUserAboutReservationReport(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response for reservation report");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Override
    public void sendResponseOnComplaint(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response on complaint");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Override
    public void notifyUserForDeleteAccountResponse(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Response on delete account request");
        mail.setText(message);
        javaMailSender.send(mail);
    }


}