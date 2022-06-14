package com.booking.ISAbackend.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.email.EmailService;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;

import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.AdventureService;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.ISAbackend.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private InstructorRepository instructorRepository;
	@Autowired
	private OwnerRepository ownerRepository;
	@Autowired
	private CottageOwnerRepository cottageOwnerRepository;
	@Autowired
	private ShipOwnerRepository shipOwnerRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private DeleteRequestRepository deleteRequestRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OwnerCategoryServiceImpl ownerCategoryService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public MyUser findById(Integer id) {
		return userRepository.findById(id).orElseGet(null);
	}

	@Override
	public MyUser findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<MyUser> findAll() {
		return userRepository.findAll();
	}

	@Override
	public MyUser save(UserRequest userRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public InstructorProfileData getInstructorDataByEmail(String email) {
		InstructorProfileData dto = null;
		Instructor i = findInstructorByEmail(email);
		if(i != null){
			OwnerCategory category = ownerCategoryService.findByReservationpoints(i.getPoints()).get(0);
			dto = new InstructorProfileData(i, category);
			return dto;
		}
		return dto;
	}

	@Override
	@Transactional
	public CottageOwnerProfileInfoDTO getCottageOwnerDataByEmail(String email) {
		CottageOwnerProfileInfoDTO dto = null;
		CottageOwner cottageOwner = findCottageOwnerByEmail(email);
		if(cottageOwner != null){
			OwnerCategory category = ownerCategoryService.findByReservationpoints(cottageOwner.getPoints()).get(0);
			dto = new CottageOwnerProfileInfoDTO(cottageOwner, category);
			return dto;
		}
		return dto;
	}

	@Override
	public CottageOwner findCottageOwnerByEmail(String email){
		MyUser user = userRepository.findByEmail(email);
		Optional<CottageOwner> cottageOwner = cottageOwnerRepository.findById(user.getId());

		return cottageOwner.orElse(null);
	}

	@Override
	@Transactional
	public ShipOwnerProfileInfoDTO getShipOwnerDataByEmail(String email) {
		ShipOwnerProfileInfoDTO dto = null;
		ShipOwner shipOwner = findShipOwnerByEmail(email);
		if(shipOwner != null){
			OwnerCategory category = ownerCategoryService.findByReservationpoints(shipOwner.getPoints()).get(0);
			dto = new ShipOwnerProfileInfoDTO(shipOwner, category);
			return dto;
		}
		return dto;
	}

	@Override
	public void cahngeAdminFirstPassword(String email, HashMap<String, String> data) throws InvalidPasswordException {
		Admin currentUser = adminRepository.findByEmail(email);
		System.out.println(email);
		String newPasswordHash = passwordEncoder.encode(data.get("newPassword1"));
		if (!data.get("newPassword1").equals("") && data.get("newPassword1").equals(data.get("newPassword2")) && passwordEncoder.matches(data.get("oldPassword"), currentUser.getPassword())) {
			currentUser.setPassword(newPasswordHash);
			currentUser.setFirstLogin(false);
			userRepository.save(currentUser);
			return;
		}
		throw new InvalidPasswordException("Data is invalid.");
	}

	@Override
	@Transactional
	public List<DeleteAccountRequestDTO> getAllDeleteAcountRequests() {
		List<DeleteRequest> deleteRequests = deleteRequestRepository.findAllActiveRequests();
		List<DeleteAccountRequestDTO> activeDeleteRequests = new ArrayList<DeleteAccountRequestDTO>();
		for(DeleteRequest request : deleteRequests){
			DeleteAccountRequestDTO dto  = createDeleteRequestDTO(request);
			activeDeleteRequests.add(dto);
		}
		return activeDeleteRequests;
	}

	@Override
	public void deleteAccount(String response, int userId, int deleteRequestId) {
		Optional<MyUser> user = userRepository.findById(userId);
		Optional<DeleteRequest> deleteRequest = deleteRequestRepository.findById(deleteRequestId);
		if(user.isPresent() && deleteRequest.isPresent()){
			MyUser myUser = user.get();
			DeleteRequest request = deleteRequest.get();
			myUser.setDeleted(true);
			request.setDeleted(true);
			userRepository.save(myUser);
			deleteRequestRepository.save(request);
			sendDeleteAccountMail(myUser.getEmail(), response);
		}
	}

	private void sendDeleteAccountMail(String email, String reason) {
		String message = "Yore account IS DELETED with following explanation: " + reason;
		emailService.notifyUserForDeleteAccountResponse(email, message);
	}

	private void sendRejectDeleteAccountMail(String email, String reason) {
		String message = "Yore account IS NOT DELETED with following explanation: " + reason;
		emailService.notifyUserForDeleteAccountResponse(email, message);
	}

	@Override
	public void rejectDeleteAccountRequest(String response, int userId, int deleteRequestId) {
		Optional<MyUser> user = userRepository.findById(userId);
		Optional<DeleteRequest> deleteRequest = deleteRequestRepository.findById(deleteRequestId);
		if(user.isPresent() && deleteRequest.isPresent()){
			MyUser myUser = user.get();
			DeleteRequest request = deleteRequest.get();
			request.setDeleted(true);
			deleteRequestRepository.save(request);
			sendRejectDeleteAccountMail(myUser.getEmail(), response);
		}
	}

	@Transactional
	public DeleteAccountRequestDTO createDeleteRequestDTO(DeleteRequest request) {
		MyUser user = request.getMyUser();
		DeleteAccountRequestDTO dto = new DeleteAccountRequestDTO(user.getId(),
																	user.getFirstName(),
																	user.getLastName(),
																	user.getRole().getName(),
																	request.getDescription(),
																	request.getId());
		return dto;
	}

	@Override
	public ShipOwner findShipOwnerByEmail(String email){
		MyUser user = userRepository.findByEmail(email);
		Optional<ShipOwner> shipOwner = shipOwnerRepository.findById(user.getId());
		return shipOwner.orElse(null);
	}

	@Override
	@Transactional
	public AdminDTO findAdminByEmail(String email) {
		Admin admin = adminRepository.findByEmail(email);
		AdminDTO adminData = new AdminDTO(admin.getEmail(), admin.getFirstName(), admin.getLastName(), admin.getPhoneNumber(),
				admin.getAddress().getStreet(), admin.getAddress().getCity(), admin.getAddress().getState(),
				admin.isEmailVerified(), admin.isDefaultAdmin(), admin.isFirstLogin());
		return adminData;
	}

	@Override
	@Transactional
	public void changeAdminData(UserProfileData newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		boolean validation = validateUserNewData(newData);
		if (validation){
			Admin admin = (Admin) userRepository.findByEmail(newData.getEmail());
			admin.setFirstName(newData.getFirstName());
			admin.setLastName(newData.getLastName());
			admin.setPhoneNumber(newData.getPhoneNumber());
			Address newAddress = new Address(newData.getStreet(), newData.getCity(), newData.getState());
			addressRepository.save(newAddress);
			admin.setAddress(newAddress);
			userRepository.save(admin);
		}

	}

	private boolean validateUserNewData(UserProfileData newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		return  Validator.onlyLetterAndSpacesValidation(newData.getFirstName()) &&
				Validator.onlyLetterAndSpacesValidation(newData.getLastName()) &&
				Validator.phoneNumberValidation(newData.getPhoneNumber()) &&
				Validator.isValidAdress(newData.getStreet(), newData.getCity(),newData.getState());

	}

	@Override
	public Boolean isOldPasswordCorrect(String email, HashMap<String, String> data) throws InvalidPasswordException {
		MyUser currentUser = userRepository.findByEmail(email);
		System.out.println(email);
		String newPasswordHash = passwordEncoder.encode(data.get("newPassword1"));
		if (!data.get("newPassword1").equals("") && data.get("newPassword1").equals(data.get("newPassword2")) && passwordEncoder.matches(data.get("oldPassword"), currentUser.getPassword())) {
			currentUser.setPassword(newPasswordHash);
			userRepository.save(currentUser);
			return true;
		}
		throw new InvalidPasswordException("Data is invalid.");
	}

	@Override
	@Transactional
	public void changeOwnerData(NewOwnerDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		Instructor instructor = findInstructorByEmail(newData.getEmail());
		if(instructor != null){
			Address address = instructor.getAddress();
			if(!newData.getFirstName().equals("")){
				if(Validator.onlyLetterAndSpacesValidation(newData.getFirstName())){
					instructor.setFirstName(newData.getFirstName());
				}
			}
			if(!newData.getLastName().equals("")){
				if(Validator.onlyLetterAndSpacesValidation(newData.getLastName())){
					instructor.setLastName(newData.getLastName());
				}

			}
			if(!newData.getPhoneNumber().equals("")){
				if(Validator.phoneNumberValidation(newData.getPhoneNumber())){
					instructor.setPhoneNumber(newData.getPhoneNumber());
				}


			}
			if(!newData.getStreet().equals("")){
				if(Validator.isValidAdress(newData.getStreet(), address.getCity(), address.getState()))
				{
					address.setStreet(newData.getStreet());
				}

			}
			if(!newData.getCity().equals("")){
				if(Validator.isValidAdress(address.getStreet(), newData.getCity(), address.getState()))
				{
					address.setCity(newData.getCity());
				}


			}
			if(!newData.getState().equals("")){
				if(Validator.isValidAdress(address.getStreet(), address.getCity(), newData.getState())) {
					address.setState(newData.getState());
				}

			}
			if(!newData.getBiography().equals("")){
				instructor.setBiography(newData.getBiography());

			}
			instructorRepository.save(instructor);
		}
	}

	@Override
	public void changeInstrctorData(InstructorNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		UserProfileData data = new UserProfileData(newData.getEmail(), newData.getFirstName(), newData.getLastName(), newData.getPhoneNumber(),
				newData.getStreet(), newData.getCity(), newData.getState());
		boolean validation = validateUserNewData(data);
		if(validation){
			MyUser user = userRepository.findByEmail(newData.getEmail());
			Optional<Instructor> instr = instructorRepository.findById(user.getId());
			if(instr.isPresent()){
				Instructor instructor = instr.get();
				instructor.setFirstName(newData.getFirstName());
				instructor.setLastName(newData.getLastName());
				instructor.setPhoneNumber(newData.getPhoneNumber());
				Address newAddress = new Address(newData.getStreet(), newData.getCity(), newData.getState());
				addressRepository.save(newAddress);
				instructor.setAddress(newAddress);
				userRepository.save(instructor);
			}
		}
	}

	@Override
	@Transactional
	public void addNewAdmin(UserProfileData data) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException, AlreadyExitingUsernameException {
		MyUser user = userRepository.findByEmail(data.getEmail());
		if(validateUserNewData(data) && user == null){
			Address address = new Address(data.getStreet(), data.getCity(), data.getState());
			addressRepository.save(address);
			boolean profileDeleted =false;
			String password = generateNewAdminPassword();
			Role role = roleRepository.findByName("ADMIN").get(0);
			Admin newAdmin = new Admin(data.getFirstName(),
					data.getLastName(), passwordEncoder.encode(password), data.getPhoneNumber(), data.getEmail(), profileDeleted,role, address, true, false );

			newAdmin.setEmailVerified(true);
			userRepository.save(newAdmin);
			emailService.notifyNewAdmin(data.getEmail(), password);
		}
		else{
			throw new AlreadyExitingUsernameException("Email address already exists.");
		}
	}

	private String generateNewAdminPassword() {
		String password = new Random().ints(10, 33, 122).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
		return password;
	}

	private boolean instructorDataValidation(InstructorNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		return  Validator.phoneNumberValidation(newData.getPhoneNumber()) &&
				Validator.onlyLetterAndSpacesValidation(newData.getFirstName()) &&
				Validator.onlyLetterAndSpacesValidation(newData.getLastName()) &&
				Validator.isValidAdress(newData.getStreet(), newData.getCity(), newData.getStreet());
	}

	@Override
	public Instructor findInstructorByEmail(String email){
		MyUser user = userRepository.findByEmail(email);
		Optional<Instructor> instructor = instructorRepository.findById(user.getId());
		return instructor.orElse(null);
	}

	@Override
	public void changeCottageOwnerData(CottageOwnerNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		UserProfileData data = new UserProfileData(newData.getEmail(), newData.getFirstName(), newData.getLastName(), newData.getPhoneNumber(),
				newData.getStreet(), newData.getCity(), newData.getState());
		boolean validation = validateUserNewData(data);
		if(validation){
			MyUser user = userRepository.findByEmail(newData.getEmail());
			Optional<CottageOwner> cottageOwner = cottageOwnerRepository.findById(user.getId());
			if(cottageOwner.isPresent()){
				CottageOwner owner = cottageOwner.get();
				owner.setFirstName(newData.getFirstName());
				owner.setLastName(newData.getLastName());
				owner.setPhoneNumber(newData.getPhoneNumber());
				Address newAddress = new Address(newData.getStreet(), newData.getCity(), newData.getState());
				addressRepository.save(newAddress);
				owner.setAddress(newAddress);
				userRepository.save(owner);
			}

		}
	}
	@Override
	public void changeShipOwnerData(ShipOwnerNewDataDTO newData) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
		UserProfileData data = new UserProfileData(newData.getEmail(), newData.getFirstName(), newData.getLastName(), newData.getPhoneNumber(),
				newData.getStreet(), newData.getCity(), newData.getState());
		boolean validation = validateUserNewData(data);
		if(validation){
			MyUser user = userRepository.findByEmail(newData.getEmail());
			Optional<ShipOwner> shipOwner = shipOwnerRepository.findById(user.getId());
			if(shipOwner.isPresent()){
				ShipOwner owner = shipOwner.get();
				owner.setFirstName(newData.getFirstName());
				owner.setLastName(newData.getLastName());
				owner.setPhoneNumber(newData.getPhoneNumber());
				Address newAddress = new Address(newData.getStreet(), newData.getCity(), newData.getState());
				addressRepository.save(newAddress);
				owner.setAddress(newAddress);
				userRepository.save(owner);
			}

		}
	}
	@Override
	@Transactional
	public boolean sendDeleteRequestCottageOwner(String email, String reason) {
		MyUser user = userRepository.findByEmail(email);
		List<Reservation> listOfReservation = reservationRepository.findByCottageOwnerEmail(email);
		LocalDate today = LocalDate.now();
		for(Reservation r:listOfReservation){
			if((today.compareTo(r.getEndDate())<0)){
				return false;
			}
		}
		DeleteRequest deleteRequest = new DeleteRequest(reason, user);
		deleteRequestRepository.save(deleteRequest);
		return true;
	}

	@Override
	@Transactional
	public boolean sendDeleteRequestShipOwner(String email, String reason) {
		MyUser user = userRepository.findByEmail(email);
		List<Reservation> listOfReservation = reservationRepository.findByShipOwnerEmail(email);
		LocalDate today = LocalDate.now();
		for(Reservation r:listOfReservation){
			if((today.compareTo(r.getEndDate())<0)){
				return false;
			}
		}
		DeleteRequest deleteRequest = new DeleteRequest(reason, user);
		deleteRequestRepository.save(deleteRequest);
		return true;
	}

}
