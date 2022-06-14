package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.service.ClientService;
import com.booking.ISAbackend.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("change-data")
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

    @PostMapping("change-password/{email}")
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
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong. Please try again.");
        }
    }

}
