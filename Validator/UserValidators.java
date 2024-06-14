package com.myapp.Validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.regex.Pattern;

public class UserValidators {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValid(String email) {
        if (email == null && email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
   
    
    
    private static final String NAME_REGEX = "^[a-zA-Z]{1,20}$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches();
    }
    
    
    
    private static final String PHONE_NUMBER_REGEX = "^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }
    
    
    
    private static final String DATE_OF_BIRTH_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final Pattern DATE_OF_BIRTH_PATTERN = Pattern.compile(DATE_OF_BIRTH_REGEX);

    public static boolean isValidDateOfBirth(String dateOfBirth) {
        return DATE_OF_BIRTH_PATTERN.matcher(dateOfBirth).matches();
    }
    
    
}
