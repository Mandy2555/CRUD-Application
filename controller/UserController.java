package com.myapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Validator.UserValidators;
import com.myapp.models.User;
import com.myapp.response.GenericResponse;
import com.myapp.response.ResponseModel;
import com.myapp.service.JWETokenService;
import com.myapp.service.UserService;
import com.myapp.utils.DateUtil;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
//	@Autowired
    private final Environment environment;
    public UserController(UserService userService, Environment environment) {
        this.userService = userService;
        this.environment = environment;
    }
	@Autowired
	private JWETokenService jweTokenService;
	
	 @Autowired
	    private ObjectMapper objectMapper;
	private static final Logger logger= LoggerFactory.getLogger(UserController.class);

//	Query query = new Query();
//	query.
//	Aggregation
	
	
//	@GetMapping(value = { "/{version:v[1]?}/users/{userId}" })
//	public ResponseEntity<ResponseModel> getUserList(@PathVariable final String userId) {
//
//		final User user = new User();
//
//		if (userId.equals("100")) {
//			user.setEmail("mandeep@gmail.com");
//			user.setFirstName("mandeep");
//			user.setLastName("siwach");
//			user.setPhonenumber("9992662555");
//			user.setDateOfBirth("2000-05-24");
//			
//		}
//
//		if (userId.equals("101")) {
//			user.setEmail("mandeep101@gmail.com");
//			user.setFirstName("mandeep101");
//			user.setLastName("singhnnnn101");
//			user.setPhonenumber("9992662555");
//			user.setDateOfBirth("2000-05-24");
//			
//		}
//
//		return new ResponseEntity<>(
//				new ResponseModel(new GenericResponse(true, 0, "1001", "userlist fetch successfully"), null, user),
//				HttpStatus.OK);
//
//	}

	@Autowired
    private UserService userService;
	
	
	@GetMapping("/aggregateUsersWithSort")
    public ResponseEntity<List<User>> getAggregatedUsers() {
        List<User> users = userService.aggregateUsersWithSort();
        return ResponseEntity.ok(users);
    }
	
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseModel> getUserById(@PathVariable String userId) {
    	logger.info("Get Api Initialization");
        User user = userService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(new ResponseModel(
                new GenericResponse(true, 0, "1007", "Id details fetched successfully "), null, user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseModel(), HttpStatus.OK);
        }
    }
    
    @GetMapping("/name/{firstName}")
    public ResponseEntity<List<User>> getUsersByName(@PathVariable String firstName) {
        List<User> users = userService.findUsersByName(firstName);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/allUsers")
    public ResponseEntity<ResponseModel> getAllUsers() {
    	logger.info("Get Api Initialization");
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(new ResponseModel(
                new GenericResponse(true, 0, "1008", "List of User fetched successfully"), null, userList), HttpStatus.OK);
    }
   
    @PutMapping("/{userId}")
    public ResponseEntity<ResponseModel> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
       int userResponse = userService.updateUser(userId, updatedUser);
       logger.info("userResponse value : {}",userResponse);
       if(userResponse==1) {
        return new ResponseEntity<>(new ResponseModel(
                new GenericResponse(true, 0, "1010", "user details updated successfully"), null, updatedUser), HttpStatus.OK);
    }
       else {
    	   return new ResponseEntity<>(new ResponseModel(
                   new GenericResponse(true, 0, "100", "user details not upsated "), null, updatedUser), HttpStatus.OK);
       }
       }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    	logger.info("Delete Api Initialization");
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

	
  @PostMapping(value = "/addUser", consumes = "multipart/form-data")
    public ResponseEntity<ResponseModel> addUser(
            @RequestPart("user") String userJson,
            @RequestPart("profileImage") MultipartFile profileImage) {

        logger.info("Received request to add a new user");

        try {
            // Convert JSON string to User object
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            // Validate inputs
            boolean isEmailValid = UserValidators.isValid(user.getEmail());
            boolean isNameValid = UserValidators.isValidName(user.getFirstName());
            boolean isValidPhoneNumber = UserValidators.isValidPhoneNumber(user.getPhoneNumber());
            boolean isValidDateOfBirth = UserValidators.isValidDateOfBirth(user.getDateOfBirth());

            logger.debug("Validation results - Email: {}, Name: {}, Phone Number: {}, Date of Birth: {}", 
                          isEmailValid, isNameValid, isValidPhoneNumber, isValidDateOfBirth);

            if (isEmailValid && isNameValid && isValidPhoneNumber && isValidDateOfBirth) {
                final String encryptToken = jweTokenService.encryptToken(user.getEmail(), "user");
                user.setAccessToken(encryptToken);
                user.setCreated(DateUtil.currentTimeStamp());
                user.setModified(DateUtil.currentTimeStamp());
                user.setProfileImage(profileImage.getBytes());
                userService.saveEntry(user);
                logger.info("User with email: {} saved successfully", user.getEmail());
                return new ResponseEntity<>(
                        new ResponseModel(new GenericResponse(true, 0, "1002", "successfully saved"), null, user),
                        HttpStatus.OK);
            } else {
                logger.warn("Validation failed for user with email: {}", user.getEmail());
                return new ResponseEntity<>(
                        new ResponseModel(new GenericResponse(false, 0, "1003", "invalid format"), null, null),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (final Exception e) {
            logger.error("Exception occurred while adding user", e);
            return new ResponseEntity<>(
                    new ResponseModel(new GenericResponse(false, 0, "1005", "Exception occurred while adding user"), null, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  @GetMapping("/downloadImage/{userId}")
  public ResponseEntity<String> downloadImageToFolder(@PathVariable String userId) {
      User user = userService.getImage(userId);
      if (user != null && user.getProfileImage() != null) {
          byte[] profileImage = user.getProfileImage();
          System.out.println("Profile image size: " + profileImage.length + " bytes");

          try {
              // Define the path to the folder
              String folderPath = "/home/vvdn/Desktop/image";
              File outputFolder = new File(folderPath);
              if (!outputFolder.exists()) {
                  outputFolder.mkdirs();
              }
              File outputFile = new File(outputFolder, userId + "_profileImage.jpg");

              // Save the image to the file
              try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                  fos.write(profileImage);
              }

              return ResponseEntity.ok("Image saved to: " + outputFile.getAbsolutePath());
          } catch (IOException e) {
              e.printStackTrace();
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image to folder");
          }
      } else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or profile image not found");
      }
  }
  @GetMapping("/createLink/{userId}")
  public ResponseEntity<String> getProfileImageURL(@PathVariable String userId) {
      User user = userService.getUserById(userId);
      if (user != null && user.getProfileImage() != null) {
		String serverPort = environment.getProperty("server.port");
          String imageURL = "http://localhost:" + serverPort + "/user/image/" + userId;
          return ResponseEntity.ok().body(imageURL);
      } else {
          return ResponseEntity.notFound().build();
      }
  }
  
}
// @PostMapping(value = "/addUser", consumes = "multipart/form-data")
// public ResponseEntity<ResponseModel> addUser(
//         @RequestParam("email") String email,
//         @RequestParam("firstName") String firstName,
//         @RequestParam("lastName") String lastName,
//         @RequestParam("phoneNumber") String phoneNumber,
//         @RequestParam("dateOfBirth") String dateOfBirth,
//         @RequestParam("profileImage") MultipartFile profileImage) {
//
//     logger.info("Received request to add a new user with email: {}", email);
//
//     try {
//         boolean isEmailValid = UserValidators.isValid(email);
//         boolean isNameValid = UserValidators.isValidName(firstName);
//         boolean isValidPhoneNumber = UserValidators.isValidPhoneNumber(phoneNumber);
//         boolean isValidDateOfBirth = UserValidators.isValidDateOfBirth(dateOfBirth);
//
//         logger.debug("Validation results - Email: {}, Name: {}, Phone Number: {}, Date of Birth: {}", 
//                       isEmailValid, isNameValid, isValidPhoneNumber, isValidDateOfBirth);
//
//         if (isEmailValid && isNameValid && isValidPhoneNumber && isValidDateOfBirth) {
//             final String encryptToken = jweTokenService.encryptToken(email, "user");
//             User user = new User(email, firstName, lastName, phoneNumber, dateOfBirth, encryptToken, DateUtil.currentTimeStamp(), DateUtil.currentTimeStamp(), profileImage.getBytes());
//             userService.saveEntry(user);
//             logger.info("User with email: {} saved successfully", email);
//             return new ResponseEntity<>(
//                     new ResponseModel(new GenericResponse(true, 0, "1002", "successfully saved"), null, user),
//                     HttpStatus.OK);
//         } else {
//             logger.warn("Validation failed for user with email: {}", email);
//             return new ResponseEntity<>(
//                     new ResponseModel(new GenericResponse(false, 0, "1003", "invalid format"), null, null),
//                     HttpStatus.BAD_REQUEST);
//         }
//     } catch (final Exception e) {
//         logger.error("Exception occurred while adding user with email: {}", email, e);
//         return new ResponseEntity<>(
//                 new ResponseModel(new GenericResponse(false, 0, "1005", "Exception occurred while adding user"), null, null),
//                 HttpStatus.INTERNAL_SERVER_ERROR);
//     }
// }
 
  


