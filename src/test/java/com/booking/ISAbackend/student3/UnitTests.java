package com.booking.ISAbackend.student3;

import com.booking.ISAbackend.config.WebConfig;
import com.booking.ISAbackend.dto.AdventureDTO;
import com.booking.ISAbackend.dto.NewAdventureDTO;
import com.booking.ISAbackend.dto.UserProfileData;
import com.booking.ISAbackend.email.EmailSender;
import com.booking.ISAbackend.email.EmailService;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.impl.ClientCategoryServiceImpl;
import com.booking.ISAbackend.service.impl.ClientServiceImpl;
import com.booking.ISAbackend.service.impl.InstructorServiceImpl;
import com.booking.ISAbackend.service.impl.UserServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class UnitTests {

    @Mock
    ClientCategoryRepository clientCategoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    ClientCategoryServiceImpl clientCategoryService;

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    InstructorServiceImpl instructorService;

    @Mock
    EmailService emailSender;

    @Mock
    DeleteRequestRepository deleteRequestRepository;

    @Mock
    ReservationRepository reservationRepository;


    @Test(expected = OverlappingCategoryBoundaryException.class)
    public void createClientCategoryFailed() throws ExistingCategoryNameException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException, OverlappingCategoryBoundaryException, AutomaticallyChangesCategoryIntervalException {

        ClientCategory newCategory = new ClientCategory();
        newCategory.setName("PLATINUM");
        newCategory.setLowLimitPoints(20);
        newCategory.setHeighLimitPoints(50);
        newCategory.setDiscount(20.0);
        newCategory.setReservationPoints(5);
        newCategory.setCategoryColor("#050124");

        List<ClientCategory> exitingCategories = new ArrayList<ClientCategory>();
        Mockito.when(clientCategoryRepository.findByName(newCategory.getName())).thenReturn(exitingCategories);
        Assert.assertTrue(clientCategoryService.checkUniqueCategoryName(newCategory));

        Assert.assertTrue(clientCategoryService.categoryValidation(newCategory));

        ClientCategory overlappingCategory = new ClientCategory();
        overlappingCategory.setId(3);
        overlappingCategory.setName("BEST_CLIENT");
        overlappingCategory.setLowLimitPoints(11);
        overlappingCategory.setHeighLimitPoints(100);
        overlappingCategory.setDiscount(10.0);
        overlappingCategory.setReservationPoints(3);
        overlappingCategory.setCategoryColor("#CC7351");
        List<ClientCategory> overlappingIntervals = new ArrayList<ClientCategory>();
        overlappingIntervals.add(overlappingCategory);
        Mockito.when(clientCategoryRepository.findByInterval(newCategory.getLowLimitPoints(), newCategory.getHeighLimitPoints())).thenReturn(overlappingIntervals);

        Mockito.when(clientCategoryService.checkIntervalOverlaping(newCategory)).thenThrow(OverlappingCategoryBoundaryException.class);

        clientCategoryService.addClientCategory(newCategory);
        Assertions.assertThrows(OverlappingCategoryBoundaryException.class, () -> clientCategoryService.addClientCategory(newCategory));
        Mockito.verifyNoInteractions(clientCategoryRepository);
    }

    @Test
    public void addNewAdmin() throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, AlreadyExitingUsernameException, InvalidAddressException {

        UserProfileData data = new UserProfileData("markoooperic123+natasa@gmail.com", "Natasa",
                "Lakovic", "111-111-1111", "Mileve Maric 10", "Kikinda", "Srbija");

        Address address = new Address("Mileve Maric 10", "Kikinda", "Srbija");
        address.setId(13);
        Admin admin = new Admin();
        admin.setEmail("markoooperic123+natasa@gmail.com");
        admin.setFirstName("Natasa");
        admin.setLastName("Lakovic");
        admin.setPhoneNumber("111-111-1111");
        admin.setAddress(address);
        admin.setFirstLogin(true);
        admin.setDefaultAdmin(false);
        admin.setDeleted(false);


        Role role = new Role();
        role.setId(2l);
        role.setName("ADMIN");
        List<Role> rolesByName = new ArrayList<Role>();
        rolesByName.add(role);

        admin.setRole(role);

        String rawPassword = "ti0==&yNwT";
        String hashPassword = "$2a$10$zLjJsdqSQlrlfj.ZhORM2.7RNdai6EUDU.rtzQHmJXrMj/k58OUBu";
        admin.setPassword(hashPassword);
        Mockito.when(userRepository.findByEmail(admin.getEmail())).thenReturn(null);
        Assert.assertTrue(userService.validateUserNewData(data));
        Mockito.when(addressRepository.save(address)).thenReturn(address);
        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(rolesByName);
        Mockito.when(passwordEncoder.encode(rawPassword)).thenReturn(hashPassword);
        Mockito.when(userRepository.save(admin)).thenReturn(admin);

        userService.addNewAdmin(data);
        verify(emailSender, times(1)).notifyNewAdmin(data.getEmail(), data.getEmail());

    }

    @Test
    public void sendDeleteAccountRequestByInstuctor(){
        String email = "markoooperic123+milica@gmail.com";
        String reason = "Ne zelim vise da poslujem";

        MyUser user = new MyUser();
        user.setId(10);
        user.setEmail("markoooperic123+milica@gmail.com");
        user.setFirstName("Milica");
        user.setLastName("Matic");

        List<Reservation> listOfReservation = new ArrayList<>();
        Mockito.when(userService.findByEmail(email)).thenReturn(user);
        Mockito.when(reservationRepository.findFutureByInstructorEmail(email, LocalDate.now())).thenReturn(listOfReservation);
        DeleteRequest deleteRequest = new DeleteRequest(reason, user);
        deleteRequest.setId(3);
        Mockito.when(deleteRequestRepository.save(deleteRequest)).thenReturn(deleteRequest);

        Assert.assertTrue(instructorService.sendDeleteRequest(email, reason));

    }
}
