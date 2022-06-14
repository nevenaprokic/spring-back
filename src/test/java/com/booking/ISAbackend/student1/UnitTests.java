package com.booking.ISAbackend.student1;


import com.booking.ISAbackend.config.WebConfig;
import com.booking.ISAbackend.dto.ReservationParamsDTO;
import com.booking.ISAbackend.exceptions.ClientNotAvailableException;
import com.booking.ISAbackend.exceptions.NotAllowedToMakeReservationException;
import com.booking.ISAbackend.exceptions.OfferNotAvailableException;
import com.booking.ISAbackend.exceptions.PreviouslyCanceledReservationException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.impl.ClientServiceImpl;
import com.booking.ISAbackend.service.impl.ReservationServiceImpl;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class UnitTests {

    @InjectMocks
    ClientServiceImpl clientServiceMock;

    @InjectMocks
    ReservationServiceImpl reservationServiceMock;

    @Mock
    ClientRepository clientRepository;

    @Mock
    AdditionalServiceRepository additionalServiceRepository;

    @Mock
    OfferRepository offerRepository;

    @Mock
    QuickReservationRepository quickReservationRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    MarkRepository markRepository;

    @Test()
    public void makeReview() throws Exception {
        // 1. Definisanje ponašanja mock objekata
        Reservation r = new Reservation();
        r.setId(1);
        r.setStartDate(LocalDate.of(2023, 4, 17));
        r.setStartDate(LocalDate.of(2023, 4, 18));
        r.setPrice(600.00);
        r.setDeleted(false);
        r.setNumberOfPerson(4);

        Client c = new Client();
        c.setPoints(0);
        c.setPenal(2);
        c.setId(1);
        c.setFirstName("Petar");
        c.setLastName("Peric");
        c.setPhoneNumber("062-111-111");
        c.setEmail("pera@gmail.com");
        c.setDeleted(false);
        c.setEmailVerified(true);
        
        Mark m = new Mark();
        m.setClient(c);
        m.setApproved(false);
        m.setReservation(r);
        m.setComment("Super je bilo!");

        Mockito.when(reservationRepository.findById(1)).thenReturn(java.util.Optional.of(r));
        Mockito.when(clientRepository.findByEmail("pera@gmail.com")).thenReturn(c);
        Optional<Mark> opt = Optional.empty();
        Mockito.when(markRepository.alreadyReviewed(1, 1)).thenReturn(opt);
        Mockito.when(markRepository.save(m)).thenReturn(m);

        // 2. Akcija
        //boolean canReserve = clientService.canReserve("pera@gmail.com");
        clientServiceMock.makeReview(5, 1, "Super je bilo!", "pera@gmail.com");

        // 3. Verifikacija: asertacije i/ili verifikacija interakcije sa mock objektima
        //Assertions.assertThrows(FeedbackAlreadyGivenException.class, () -> clientService.makeReview(5, 1, "Super je bilo!", "pera@gmail.com"));
        //Mockito.verify(markRepository, Mockito.times(1)).save(m);

        Mockito.verify(markRepository, Mockito.times(1)).alreadyReviewed(1, 1);
        Assertions.assertEquals(false, m.getApproved());
        Mockito.verify(clientRepository, Mockito.times(1)).findByEmail(c.getEmail());
    }

    @Test
    public void makeReservation() throws NotAllowedToMakeReservationException, OfferNotAvailableException, PreviouslyCanceledReservationException, ClientNotAvailableException, InterruptedException {

        Reservation r = new Reservation();
        r.setId(13);

        ReservationParamsDTO params = new ReservationParamsDTO();
        params.setEmail("pera@gmail.com");
        params.setDate(LocalDate.of(2023, 6, 30));
        params.setEndingDate(LocalDate.of(2023, 7, 1));
        params.setGuests(1);
        params.setServices(new ArrayList<AdditionalService>());
        params.setTotal(40.00);
        params.setOfferId(2);

        Client c = new Client();
        c.setPoints(0);
        c.setPenal(2);
        c.setId(1);
        c.setFirstName("Petar");
        c.setLastName("Peric");
        c.setPhoneNumber("062-111-111");
        c.setEmail("pera@gmail.com");
        c.setDeleted(false);
        c.setEmailVerified(true);

        Offer o = new Offer();
        o.setVersion(1L);
        o.setNumberOfReservations(3L);
        o.setUnavailableDate(new ArrayList<>());
        List<Offer> nonAvailables = new ArrayList<>();
        Offer nonAvailable = new Offer();
        nonAvailable.setId(3);
        nonAvailables.add(nonAvailable);

        Optional<Integer> opt = Optional.empty();
        Mockito.when(reservationRepository.checkIfCanceled(params.getEmail(), params.getDate(), params.getOfferId())).thenReturn(opt);
        Mockito.when(clientRepository.getPenalties(params.getEmail())).thenReturn(2);
        Mockito.when(clientRepository.findByEmail(params.getEmail())).thenReturn(c);
        Mockito.when(offerRepository.findOfferById(params.getOfferId())).thenReturn(o);

        reservationServiceMock.makeReservation(params);

        Mockito.when(offerRepository.nonAvailableOffers(params.getDate(), params.getEndingDate())).thenReturn(nonAvailables);

        Mockito.verify(reservationRepository, Mockito.times(1)).save(Mockito.any(Reservation.class));
    }

    @Test(expected = NotAllowedToMakeReservationException.class)
    public void makeReservationFail() throws NotAllowedToMakeReservationException, OfferNotAvailableException, PreviouslyCanceledReservationException, ClientNotAvailableException, InterruptedException {

        Reservation r = new Reservation();
        r.setId(13);

        ReservationParamsDTO params = new ReservationParamsDTO();
        params.setEmail("pera@gmail.com");
        params.setDate(LocalDate.of(2023, 6, 30));
        params.setEndingDate(LocalDate.of(2023, 7, 1));
        params.setGuests(1);
        params.setServices(new ArrayList<AdditionalService>());
        params.setTotal(40.00);
        params.setOfferId(2);

        Client c = new Client();
        c.setPoints(0);
        c.setPenal(2);
        c.setId(1);
        c.setFirstName("Petar");
        c.setLastName("Peric");
        c.setPhoneNumber("062-111-111");
        c.setEmail("pera@gmail.com");
        c.setDeleted(false);
        c.setEmailVerified(true);

        Offer o = new Offer();
        o.setNumberOfReservations(3L);
        o.setUnavailableDate(new ArrayList<>());
        o.setVersion(1L);
        List<Offer> nonAvailables = new ArrayList<>();
        Offer nonAvailable = new Offer();
        nonAvailable.setId(3);
        nonAvailables.add(nonAvailable);

        Optional<Integer> opt = Optional.empty();
        Mockito.when(reservationRepository.checkIfCanceled(params.getEmail(), params.getDate(), params.getOfferId())).thenReturn(opt);
        Mockito.when(clientRepository.getPenalties(params.getEmail())).thenReturn(3);
        Mockito.when(clientRepository.findByEmail(params.getEmail())).thenReturn(c);
        Mockito.when(offerRepository.findOfferById(params.getOfferId())).thenReturn(o);

        reservationServiceMock.makeReservation(params);

        Assertions.assertThrows(NotAllowedToMakeReservationException.class, () -> reservationServiceMock.makeReservation(params));
        Mockito.verifyNoInteractions(additionalServiceRepository);
    }
}
