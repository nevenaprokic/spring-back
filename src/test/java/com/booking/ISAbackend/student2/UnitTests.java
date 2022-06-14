package com.booking.ISAbackend.student2;

import com.booking.ISAbackend.config.WebConfig;
import com.booking.ISAbackend.dto.NewReservationReportDTO;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.ClientRepository;
import com.booking.ISAbackend.repository.ReservationReportRepository;
import com.booking.ISAbackend.repository.ReservationRepository;
import com.booking.ISAbackend.service.ReservationReportService;
import com.booking.ISAbackend.service.impl.ReservationReportServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class UnitTests {

    @InjectMocks
    ReservationReportServiceImpl reservationReportService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationReportRepository reservationReportRepository;



    @Test()
    public void makeReservationReport() throws Exception {
        Reservation r = new Reservation();
        r.setId(1);
        r.setStartDate(LocalDate.of(2022, 5, 12));
        r.setStartDate(LocalDate.of(2022, 5, 18));
        r.setPrice(300.00);
        r.setDeleted(false);
        r.setNumberOfPerson(4);

        Client c = new Client();
        c.setPoints(0);
        c.setPenal(2);
        c.setId(2);
        c.setFirstName("Marko");
        c.setLastName("savic");
        c.setPhoneNumber("065-111-1112");
        c.setEmail("markoooperic123+marko@gmail.com");
        c.setDeleted(false);
        c.setEmailVerified(true);

        Mockito.when(clientRepository.findById(2)).thenReturn(Optional.of(c));
        Mockito.when(reservationRepository.findById(1)).thenReturn(Optional.of(r));

        NewReservationReportDTO reportDTO = new NewReservationReportDTO();
        reportDTO.setReservationId(1);
        reportDTO.setClientId(2);
        reportDTO.setValueImpression(Impression.NEGATIVE);
        reportDTO.setValueShowUp(true);
        reportDTO.setComment("Napravili su zurku iako muzika nije dozvoljena posle 23h");
        reservationReportService.addReservationReport(reportDTO);

        Mockito.verify(reservationReportRepository, Mockito.times(1)).save(Mockito.any(ReservationReport.class));

    }
}