package com.myapp.scheduler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@Component
public class ApiCallJob implements Job {

	private final RestTemplate restTemplate;
	
	   public ApiCallJob() {
	        this.restTemplate = new RestTemplate();
	    }
    
    @Autowired
    public ApiCallJob(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Define your API URL and any necessary request parameters
        String apiUrl = "http://localhost:8086/user/name/Car";
        
        // Make the API call
        try {
            String response = restTemplate.getForObject(apiUrl, String.class);
            
            // Process the API response as needed
            System.out.println("API call executed. Response: " + response);
            
            // Add your further processing logic here
        } catch (Exception e) {
            // Handle exceptions appropriately
            System.err.println("Error making API call: " + e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}

