package com.booking.ISAbackend.service;

import java.util.HashMap;
import java.util.List;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;

import com.booking.ISAbackend.model.*;

public interface UserService {
	
	MyUser findById(Integer id);
	MyUser findByEmail(String email);
    List<MyUser> findAll ();
    MyUser save(UserRequest userRequest);
    InstructorProfileData getInstructorDataByEmail(String email);
    void changeOwnerData(NewOwnerDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException;
    void changeInstrctorData(InstructorNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException;
    void addNewAdmin(UserProfileData data) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException, AlreadyExitingUsernameException;

    CottageOwner findCottageOwnerByEmail(String email);

    Boolean isOldPasswordCorrect(String email, HashMap<String, String> data) throws InvalidPasswordException;
    ShipOwner findShipOwnerByEmail(String email);

    AdminDTO findAdminByEmail(String email);
    void changeAdminData(UserProfileData newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException;

    Instructor findInstructorByEmail(String email);
    void changeCottageOwnerData(CottageOwnerNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException;
    void changeShipOwnerData(ShipOwnerNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException;
    boolean sendDeleteRequestCottageOwner(String email, String reason);
    boolean sendDeleteRequestShipOwner(String email, String reason);
    CottageOwnerProfileInfoDTO getCottageOwnerDataByEmail(String email);
    ShipOwnerProfileInfoDTO getShipOwnerDataByEmail(String email);

    void cahngeAdminFirstPassword(String email, HashMap<String, String> data) throws InvalidPasswordException;

    List<DeleteAccountRequestDTO> getAllDeleteAcountRequests();
    void deleteAccount(String response, int clientId, int deleteRequestId);
    void rejectDeleteAccountRequest(String response, int userId, int deleteRequestId);


}
