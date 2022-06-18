package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.service.ClientService;
import com.booking.ISAbackend.service.OfferService;
import com.booking.ISAbackend.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private OfferService offerService;

    @GetMapping("profile-info/{email}")
    public ResponseEntity<AdminDTO> getAdminProfileInfo(@PathVariable("email") String email){
        try{
            AdminDTO adminData = userService.findAdminByEmail(email);
            return ResponseEntity.ok(adminData);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("change-data")
    public ResponseEntity<String> changeAdminData(@RequestBody UserProfileData newData){
        try{
            userService.changeAdminData(newData);
            return ResponseEntity.ok("Successfully changed you data");
        } catch (OnlyLettersAndSpacesException | InvalidPhoneNumberException | InvalidAddressException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("add-admin")
    public ResponseEntity<String> addNewAdmin (@RequestBody UserProfileData newAdminData){
        try{
                userService.addNewAdmin(newAdminData);
                return ResponseEntity.ok().body("Successfully added new admin.");
        } catch (OnlyLettersAndSpacesException | InvalidPhoneNumberException | InvalidAddressException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (AlreadyExitingUsernameException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong, please try agan later.");
        }

    }

    @PutMapping("change-password/{email}")
    public ResponseEntity<String> changeFirstLoginPassword(@PathVariable String email, @RequestBody HashMap<String, String> data){
        try {
            userService.cahngeAdminFirstPassword(email, data);
            return ResponseEntity.ok("Successfully changed password.");
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Something wnt wrong. Please try again later");
        }
    }

    @GetMapping("all-complaints")
    public ResponseEntity<List<ComplaintDTO>> gellAllNotReviewComplaints(){
        try{
            return ResponseEntity.ok().body(clientService.getAllNotDeletedComplaints());
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("complaint-response/{response}/{complaintId}")
    public ResponseEntity<String> respondOnComplaint(@PathVariable("response")  String response, @PathVariable("complaintId") int complaintId){
        try{
            clientService.respondOnComplaint(response, complaintId);
            return ResponseEntity.ok().body("Successfully send response on complaint");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("Other admin just the other admin has just responded to the complaint so you can't.");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong. Please try again");

        }
    }

    @GetMapping("delete-account-requets")
    public ResponseEntity<DeleteAccountRequestDTO> getAllDeleteAccountRequests(){
        try{
            List<DeleteAccountRequestDTO> deleteAccountRequest = userService.getAllDeleteAcountRequests();
            return new ResponseEntity(deleteAccountRequest, deleteAccountRequest.size() != 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("delete-request/delete/{userId}/{requestId}")
    public ResponseEntity<String> deleteAccountOnRequest(@PathVariable("userId") int userId, @PathVariable("requestId") int requestId, @RequestBody String message){
        try{
            userService.deleteAccount(message, userId, requestId);
            return ResponseEntity.ok().body("Successfully delete user account");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("The other admin has just responded to this delete request so you can't.");
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong. Please try again.");
        }
    }

    @PutMapping("delete-request/reject/{userId}/{requestId}")
    public ResponseEntity<String> rejectDeleteAccountOnRequest(@PathVariable("userId") int userId, @PathVariable("requestId") int requestId, @RequestBody String message){
        try{
            userService.rejectDeleteAccountRequest(message, userId, requestId);
            return ResponseEntity.ok().body("Successfully reject delete request");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("The other admin has just responded to this delete request so you can't.");
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong. Please try again.");
        }
    }


    @GetMapping("all-instructors")
    public ResponseEntity<List<UserDTO>> getAllInstructors( @RequestParam int page, @RequestParam int pageSize){
        try{
            return ResponseEntity.ok(userService.getAllActiveInstructors(page, pageSize));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("all-cottage-owners")
    public ResponseEntity<List<UserDTO>> getAllCottageOwners( @RequestParam int page, @RequestParam int pageSize){
        try{
            return ResponseEntity.ok(userService.getAllActiveCottageOwners(page, pageSize));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("all-ship-owners")
    public ResponseEntity<List<UserDTO>> getAllShipOwners( @RequestParam int page, @RequestParam int pageSize){
        try{
            return ResponseEntity.ok(userService.getAllActiveShipOwners(page, pageSize));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("all-admins/{currentAdmin}/{page}/{pageSize}")
    public ResponseEntity<List<UserDTO>> getAllAdminsExceptCurrent(@PathVariable("currentAdmin") String currentAdmin,  @PathVariable("page") int page,@PathVariable("pageSize") int pageSize){
        try{
            return ResponseEntity.ok(userService.getAllActiveAdmins(page, pageSize, currentAdmin));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
      
    }

    @GetMapping("business-report")
    public ResponseEntity<BusinessReportDTO> getBusinessReport(@RequestParam String startDate, @RequestParam String endDate){
        try{
            return ResponseEntity.ok().body(offerService.getAdminBusinessReportData(startDate, endDate));
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("all-clients")
    public ResponseEntity<List<UserDTO>> getAllClients( @RequestParam int page, @RequestParam int pageSize){
        try{
            return ResponseEntity.ok(clientService.getAllActiveClients(page, pageSize));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
