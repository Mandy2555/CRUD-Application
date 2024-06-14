package com.myapp.scheduler.configController;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myapp.scheduler.UserDetailsJob;

@RestController
@RequestMapping("/api/jobs")
public class JobTriggerController {

    @Autowired
    private Scheduler scheduler;

    @GetMapping("/trigger-job/{userId}")
    public ResponseEntity<String> triggerJob(@PathVariable String userId) {
        try {
            // Create job detail with user ID
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("userId", userId);

            JobDetail jobDetail = JobBuilder.newJob(UserDetailsJob.class)
                    .usingJobData(jobDataMap)
                    .withIdentity("userDetailsJob" + userId)
                    .storeDurably()
                    .build();

            // Create trigger with job detail
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("userDetailsJobTrigger" + userId)
                    .startNow()
                    .build();

            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);

            return ResponseEntity.ok("Job triggered successfully for user ID: " + userId);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error triggering job for user ID: " + userId);
        }
    }
}
