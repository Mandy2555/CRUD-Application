package com.myapp.scheduler.email;
//
//import java.time.ZonedDateTime;
//import org.quartz.TriggerBuilder;
//import org.quartz.TriggerKey;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.myapp.controller.UserController;
//
//import jakarta.validation.Valid;
//
//import java.util.Date;
//import java.util.UUID;
//
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SimpleScheduleBuilder;
//import org.quartz.Trigger;
//
//@RestController
//public class EmailSchedulerController {
//	private static final Logger logger= LoggerFactory.getLogger(UserController.class);
//	@Autowired
//	private Scheduler scheduler ;
//	@PostMapping("/schedule/email")
//	public ResponseEntity<EmailResponse> scheduleEmail(@Valid @RequestBody EmailRequest emailRequest){
//		try {
//			logger.info("try block entered successfully");
//			ZonedDateTime dateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getTimeZone());
//			
//
//			if(dateTime.isBefore(ZonedDateTime.now())) {
//				
//				EmailResponse emailResponse = new EmailResponse(false , "dateTime must be after current time");
//				return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
//						.body(emailResponse);
//			}
//			
//			JobDetail jobDetail = buildJobDetail(emailRequest);
//			Trigger trigger = buildTrigger(jobDetail, dateTime);
//			
//			scheduler.scheduleJob(jobDetail , trigger); 
//			
//			EmailResponse emailResponse	= new EmailResponse(true,jobDetail.getKey().getName(),jobDetail.getKey().getGroup(),"Email scheduled successfully");
//			return ResponseEntity.ok(emailResponse);
//					
//		}catch(SchedulerException se) {
//			logger.info("Catch block Exception ");
//			EmailResponse emailResponse	= new EmailResponse(false,"Error while scheduling email.please try later!");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(emailResponse);
//		}
//	}
//	
////	 @GetMapping("/trigger-job")
////	    public String triggerJob() {
////	        try {
////	            scheduler.triggerJob(TriggerKey.triggerKey("userDetailsJobTrigger"));
////	            return "Job triggered!";
////	        } catch (SchedulerException e) {
////	            e.printStackTrace();
////	            return "Error triggering job.";
////	        }
////	    }
//	
////	private JobDetail buildJobDetail(EmailRequest scheduleEmailRequest) {
////		JobDataMap jobDataMap = new JobDataMap() ;
////		jobDataMap.put("email", scheduleEmailRequest.getEmail());
////		jobDataMap.put("subject", scheduleEmailRequest.getSubject());
////		jobDataMap.put("body", scheduleEmailRequest.getBody());
////		
////		return JobBuilder.newJob(EmailJob.class)
////				.withIdentity(UUID.randomUUID().toString(), "email-jobs")
////				.withDescription("Send email job")
////				.usingJobData(jobDataMap)
////				.storeDurably()
////				.build();
////		
////	}
////	private Trigger buildTrigger (JobDetail jobDetail ,ZonedDateTime startAt) {
////		return TriggerBuilder.newTrigger()
////				.forJob(jobDetail)
////				.withIdentity(jobDetail.getKey().getName() , "email-triggers")
////				.withDescription("Send Email Trigger")
////				.startAt(Date.from(startAt.toInstant()))
////				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
////				.build();
////	}
//}



