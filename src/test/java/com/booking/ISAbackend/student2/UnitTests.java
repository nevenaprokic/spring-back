package com.booking.ISAbackend.student2;

import com.booking.ISAbackend.config.WebConfig;
import com.booking.ISAbackend.dto.CottageDTO;
import com.booking.ISAbackend.dto.NewCottageDTO;
import com.booking.ISAbackend.dto.NewReservationReportDTO;
import com.booking.ISAbackend.exceptions.CottageAlreadyExistsException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.impl.CottageServiceImpl;
import com.booking.ISAbackend.service.impl.ReservationReportServiceImpl;
import com.booking.ISAbackend.service.impl.UserServiceImpl;
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
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class UnitTests {

    @InjectMocks
    ReservationReportServiceImpl reservationReportService;
    @InjectMocks
    CottageServiceImpl cottageService;

    @Mock
    UserServiceImpl userServiceMock;
    @Mock
    ClientRepository clientRepository;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ReservationReportRepository reservationReportRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CottageOwnerRepository cottageOwnerRepository;
    @Mock
    CottageRepository cottageRepository;



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
    @Test
    public void addCottageFail() throws Exception{
        NewCottageDTO c = new NewCottageDTO();
        c.setOfferName("Vikendica raj");
        c.setOwnerEmail("markoooperic123+mile@gmail.com");
        c.setPeopleNum("4");
        c.setPrice("30");
        c.setBedNumber("3");
        c.setRoomNumber("3");
        c.setCity("Divcibare");
        c.setState("Srbija");
        c.setStreet("Divcibare bb");
        c.setDescription("Objekat Apartmani Smigic Divcibare se nalazi u naselju Divčibare i nudi smeštaj sa pogledom na vrt i besplatnim WiFi internetom, kao i vrt sa priborom za pripremu roštilja.");
        c.setRulesOfConduct("Nije dozvoljeno bilo kakvo unistavanje imovine.");
        c.setCancelationConditions("Dozvoljeno je otkazivanje 3 dana pre.");
        c.setPhotos(new ArrayList<>());

        CottageOwner co = new CottageOwner();
        co.setDeleted(false);
        co.setEmail("markoooperic123+mile@gmail.com");
        co.setEmailVerified(true);
        co.setFirstName("Mile");
        co.setLastName("Kostic");
        co.setId(5);
        co.setPhoneNumber("063-111-1115");

        MyUser user = new MyUser();
        user.setDeleted(false);
        user.setEmail("markoooperic123+mile@gmail.com");
        user.setEmailVerified(true);
        user.setFirstName("Mile");
        user.setLastName("Kostic");
        user.setId(5);
        user.setPhoneNumber("063-111-1115");


        Cottage currentCottage = new Cottage();
        currentCottage.setId(1);
        currentCottage.setDeleted(false);
        currentCottage.setName("Vikendica raj");
        currentCottage.setNumberOfPerson(2);
        currentCottage.setPrice(30.00);
        currentCottage.setBedNumber(2);
        currentCottage.setRoomNumber(2);
        currentCottage.setAddress(new Address("Omladinska 19", "Novi Sad", "Srbija"));
        currentCottage.setDescription("Sjajan pogled na sumu i planinu! U blizini se nalazi ski staza.");
        currentCottage.setRulesOfConduct("Nije dozvoljeno bilo kakvo unistavanje imovine.");
        currentCottage.setCancellationConditions("Dozvoljeno je otkazivanje 3 dana pre.");
        currentCottage.setNumberOfModify(0L);
        currentCottage.setNumberOfReservations(0L);
        currentCottage.setNumberOfQuickReservation(0L);
        currentCottage.setUnavailableDate(new ArrayList<>());
        currentCottage.setVersion(0L);
        currentCottage.setSubscribedClients(new ArrayList<>());
        currentCottage.setReservations(new ArrayList<>());
        currentCottage.setQuickReservations(new ArrayList<>());
        currentCottage.setAdditionalServices(new ArrayList<>());
        currentCottage.setPhotos(new ArrayList<>());
        List<Cottage> cottages = new ArrayList<>();
        cottages.add(currentCottage);
        co.setCottages(cottages);


        Mockito.when(userRepository.findByEmail(c.getOwnerEmail())).thenReturn(user);
        Mockito.when(cottageOwnerRepository.findById(user.getId())).thenReturn(Optional.ofNullable(co));
        Mockito.when(userServiceMock.findCottageOwnerByEmail(c.getOwnerEmail())).thenReturn(Optional.ofNullable(co));

        Assertions.assertThrows(CottageAlreadyExistsException.class, () -> cottageService.addCottage(c));
    }

    @Test
    public void updateCottage() throws Exception{
        CottageDTO c = new CottageDTO();
        c.setId(1);
        c.setName("Vikendica raj");
        c.setNumberOfPerson(2);
        c.setPrice(40.00);
        c.setBedNumber(3);
        c.setRoomNumber(3);
        c.setCity("Novi Sad");
        c.setState("Srbija");
        c.setStreet("Omladinska 19");
        c.setDescription("Sjajan pogled na sumu i planinu! U blizini se nalazi ski staza. Takodje mozete uzivati u lepim setnjama u sumi.");
        c.setRulesOfConduct("Nije dozvoljeno bilo kakvo unistavanje imovine.");
        c.setCancellationConditions("Dozvoljeno je otkazivanje 3 dana pre.");
        c.setPhotos(new ArrayList<>());


        Cottage currentCottage = new Cottage();
        currentCottage.setId(1);
        currentCottage.setDeleted(false);
        currentCottage.setName("Vikendica raj");
        currentCottage.setNumberOfPerson(2);
        currentCottage.setPrice(30.00);
        currentCottage.setBedNumber(2);
        currentCottage.setRoomNumber(2);
        currentCottage.setAddress(new Address("Omladinska 19", "Novi Sad", "Srbija"));
        currentCottage.setDescription("Sjajan pogled na sumu i planinu! U blizini se nalazi ski staza.");
        currentCottage.setRulesOfConduct("Nije dozvoljeno bilo kakvo unistavanje imovine.");
        currentCottage.setCancellationConditions("Dozvoljeno je otkazivanje 3 dana pre.");
        currentCottage.setNumberOfModify(0L);
        currentCottage.setNumberOfReservations(0L);
        currentCottage.setNumberOfQuickReservation(0L);
        currentCottage.setUnavailableDate(new ArrayList<>());
        currentCottage.setVersion(0L);
        currentCottage.setSubscribedClients(new ArrayList<>());
        currentCottage.setReservations(new ArrayList<>());
        currentCottage.setQuickReservations(new ArrayList<>());
        currentCottage.setAdditionalServices(new ArrayList<>());
        currentCottage.setPhotos(new ArrayList<>());

        //set new parameter
        currentCottage.setPrice(40.00);
        currentCottage.setBedNumber(3);
        currentCottage.setRoomNumber(3);
        currentCottage.setDescription("Sjajan pogled na sumu i planinu! U blizini se nalazi ski staza. Takodje mozete uzivati u lepim setnjama u sumi.");

        Mockito.when(cottageRepository.findCottageById(c.getId())).thenReturn(currentCottage);
        cottageRepository.save(currentCottage);

        Assertions.assertEquals(40.00, currentCottage.getPrice());
        Mockito.verify(cottageRepository, Mockito.times(1)).save(currentCottage);

    }
}