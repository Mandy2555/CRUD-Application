package com.myapp.service;

import com.mongodb.client.result.UpdateResult;
import com.myapp.models.User;
import com.myapp.repository.*;
import com.myapp.utils.DateUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
@Component
public class UserService<Photo> {
	
	private MongoTemplate mongoTemplate;

	private final String Folder_Path = "/home/vvdn/Desktop/image";
    @Autowired
    public void UserService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
	private static final Logger logger= LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;

	public void saveEntry(User User) {
		userRepository.save(User);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(String userId) {
		User userDetails = userRepository.findByCustomUserId(userId);
		logger.info("User details on the basis of userId : {}",userDetails.getEmail());
		return userRepository.findById(userId).orElse(null);
	}

//	public void updateUser(String userId, User updatedUser) {
//		User existingUser = userRepository.findById(userId).orElse(null);
//		if (existingUser != null) {
//			existingUser.setEmail(updatedUser.getEmail());
//			existingUser.setFirstName(updatedUser.getFirstName());
//			existingUser.setLastName(updatedUser.getLastName());
//			existingUser.setPhonenumber(updatedUser.getphoneNumber());
//			existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
//
//			userRepository.save(existingUser);
//		}
//	}

	public void deleteUser(String userId) {
		userRepository.deleteById(userId);
	}
	

	public List<User> findUsersByName(String firstName) {
		// TODO Auto-generated method stub
		return userRepository.findByFirstName(firstName);
	}

	/**
	 * 
	 * @param userId
	 * @param updatedUser
	 * @return
	 */
	public int updateUser(final String userId,final User updatedUser) {
		
        final Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));

        final Update update = new Update();
        if (updatedUser.getFirstName() != null) {
            update.set("firstName", updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            update.set("lastName", updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            update.set("email", updatedUser.getEmail());
        }
        if (updatedUser.getPhoneNumber() != null) {
            update.set("phoneNumber", updatedUser.getPhoneNumber());
        }
        
        logger.info("Query: {}", query);
        logger.info("Update: {}", update);
        update.set("modified", DateUtil.currentTimeStamp());
        final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);
        
        logger.info("UpdateResult: {}", updateResult);
		if (updateResult.getModifiedCount() > 0) {
			return 1;
		} else {
			return 0;
		}
		
    }

	public List<User> aggregateUsersWithSort() {
        // Match stage to filter users with firstName "Ram"
        MatchOperation matchStage = Aggregation.match(Criteria.where("firstName").is("Car"));

        // Create the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(matchStage);

        // Execute the aggregation
        AggregationResults<User> results = mongoTemplate.aggregate(aggregation, "user", User.class);

        // Return the result list
        return results.getMappedResults();
    }

	  public User getImage(String userId) {
	        // Implement your logic to retrieve the user by ID
	        // This is a placeholder. Adjust according to your repository method.
	        return userRepository.findById(userId).orElse(null);
	    }
	  
	  
}
