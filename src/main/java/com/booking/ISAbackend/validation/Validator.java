package com.booking.ISAbackend.validation;

import com.booking.ISAbackend.dto.AdditionalServiceDTO;
import com.booking.ISAbackend.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidPrice(String priceStr) throws InvalidPriceException {

        String regex = "^(\\d+(\\.\\d{0,2})?|\\.?\\d{1,2})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(priceStr);
        if(matcher.matches()){
            return true;
        }
        else{
            throw new InvalidPriceException("Invalid price! Price has to be decimal number with a maximum of two decimal places.");

        }
    }

    public static boolean isValidPeopleNumber(String peopleNumStr) throws InvalidPeopleNumberException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(peopleNumStr);
        if(matcher.matches()) return true;
        else{
            throw new InvalidPeopleNumberException("People number mast be positive whole number! ");

        }
    }
    public static boolean isValidRoomNumber(String roomNumber) throws InvalidRoomNumberException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(roomNumber);
        if(matcher.matches()) return true;
        else{
            throw new InvalidRoomNumberException("Room number mast be positive whole number! ");

        }
    }
    public static boolean isValidBedNumber(String bedNumber) throws InvalidBedNumberException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(bedNumber);
        if(matcher.matches()) return true;
        else{
            throw new InvalidBedNumberException("Bed number mast be positive whole number! ");

        }
    }
    public static boolean isValidSize(String size) throws InvalidSizeException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(size);
        if(matcher.matches()) return true;
        else{
            throw new InvalidSizeException("Size mast be positive whole number! ");

        }
    }
    public static boolean isValidMotorNumber(String motorNumber) throws InvalidMotorNumberException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(motorNumber);
        if(matcher.matches()) return true;
        else{
            throw new InvalidMotorNumberException("Number of motor be positive whole number! ");

        }
    }
    public static boolean isValidMotorPower(String motorPower) throws InvalidMotorPowerException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(motorPower);
        if(matcher.matches()) return true;
        else{
            throw new InvalidMotorPowerException("Motor power be positive whole number! ");

        }
    }
    public static boolean isValidMaxSpeed(String motorSpeed) throws InvalidMaxSpeedException {
        String regex = "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(motorSpeed);
        if(matcher.matches()) return true;
        else{
            throw new InvalidMaxSpeedException("Max motor speed be positive whole number! ");

        }
    }

    public static boolean isValidAdress(String street, String city, String state) throws InvalidAddressException {
        String cityRegex = "^[a-zA-Z\\s]*$";
        String streetRegex = "^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$";
        Pattern cityPattern = Pattern.compile(cityRegex);
        Pattern streetPattern = Pattern.compile(streetRegex);
        Matcher matcherStreet = streetPattern.matcher(street);
        Matcher matcherCity = cityPattern.matcher(city);
        Matcher matcherState = cityPattern.matcher(state);

        if(matcherStreet.matches() && matcherCity.matches() && matcherState.matches()) return true;

        else if (!matcherStreet.matches()){
            throw new InvalidAddressException("Invalid street name! Only letters, numbers and spaces are allowed");

        }
        else if (!matcherCity.matches()){
            throw new InvalidAddressException("Invalid city name! Only letters and spaces are allowed");

        }
        else{
            throw new InvalidAddressException("Invalid city name! Only letters and spaces are allowed");
        }
    }

    public static boolean isValidAdditionalServices(List<HashMap<String, String>> additionalServices) throws RequiredFiledException, InvalidPriceException {
        for (HashMap<String, String> serviceMap: additionalServices
        ) {
            if(!(serviceMap.get("serviceName")).equals("")){
                try{
                    double price = Double.valueOf(String.valueOf(serviceMap.get("servicePrice")));
                    return isValidPrice(String.valueOf(price));
                }
                catch (Exception e){
                    throw new RequiredFiledException("Price is required for additional service" + serviceMap.get("serviceName") + "!It isn't enough to write only the name");
                }
            }
        }
        return true;
    }

    public static boolean onlyLetterAndSpacesValidation(String dataForValidation) throws OnlyLettersAndSpacesException {
        String regex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dataForValidation);
        if(matcher.matches()){
            return true;
        }
        else{
            throw new OnlyLettersAndSpacesException("invalid data: " + dataForValidation + "! Only letters and spaces are allowded.");
        }
    }

    public static boolean phoneNumberValidation(String phoneNumber) throws InvalidPhoneNumberException {
        String regex = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"; //neki validator za broj telefona, proveriti da li je okej ovaj
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(matcher.matches()){
            return true;
        }
        else{
            throw new InvalidPhoneNumberException("invalid phone number: " + phoneNumber + "!Allowded pattern for phone number: "); //OVDE DODATI KOJI PATERN
        }
    }
    public static boolean isMachPassword(String password, String confirmPassword) throws InvalidPasswordException{
        if(!password.equals(confirmPassword))
            throw new InvalidPasswordException("The passwords do not match!");
        return true;
    }

    public static boolean isValidEmail(String email) throws InvalidEmail{
        String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern emailPattern = Pattern.compile(regex);
        Matcher matcherEmail = emailPattern.matcher(email);

        if(matcherEmail.matches()) return true;

        else if (!matcherEmail.matches()) {
            throw new InvalidEmail("Invalid email address!");
        }
        return false;
    }
    public static boolean isValidCredentials(String credential) throws InvalidCredential{
        String regex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
        Pattern credentialPattern = Pattern.compile(regex);
        Matcher matcherCredential = credentialPattern.matcher(credential);

        if(matcherCredential.matches()) return true;

        else if (!matcherCredential.matches()) {
            throw new InvalidCredential("Invalid credential! Only letters are allowed");
        }
        return false;
    }
    public static boolean isValidPhoneNumber(String phoneNumber) throws  InvalidPhoneNumber{
        String regex = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
        Pattern phoneNumberPattern = Pattern.compile(regex);
        Matcher matcherPhoneNumber = phoneNumberPattern.matcher(phoneNumber);

        if(matcherPhoneNumber.matches()) return true;

        else if (!matcherPhoneNumber.matches()) {
            throw new InvalidPhoneNumber("Invalid phone number! Only numbers are allowed");
        }
        return false;
    }

    public static boolean isPositiveInteger(int number){
        String regex = "^0|[1-9]+[0-9]*$";
        Pattern numberPatern = Pattern.compile(regex);
        Matcher matcher = numberPatern.matcher(String.valueOf(number));

        if(matcher.matches()) return true;
        return false;
    }

    public static boolean isValidReservationPointsNum(int number) throws InvalidPointsNumberException {
        if(isPositiveInteger(number)) return true;
        else {
            throw new InvalidPointsNumberException();
        }
    }

    public static boolean isValidBoundaryNum(int number) throws InvalidBoundaryException {
        if(isPositiveInteger(number)) return true;
        else {
            throw new InvalidBoundaryException();
        }
    }

    public static boolean isValidPercentNum(double number) throws InvalidPointsNumberException, InvalidPercentException {
        String regex = "^100$|^\\d{0,2}(\\.\\d{1,2})? *%?$";
        Pattern percentPatern = Pattern.compile(regex);
        Matcher matcher = percentPatern.matcher(String.valueOf(number));

        if(matcher.matches()) return true;
        else{
            throw new InvalidPercentException();
        }
    }

}
