package com.myapp.scheduler.configController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myapp.scheduler.configuration.JobConfig;
import com.myapp.scheduler.configuration.JobConfigService;
import com.myapp.scheduler.configuration.QuartzConfig;

@RestController
@RequestMapping("/api/jobs")
public class JobConfigController {

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private QuartzConfig quartzConfig;

    @PostMapping
    public ResponseEntity<JobConfig> addJobConfig(@RequestBody JobConfig jobConfig) {
        JobConfig savedJobConfig = jobConfigService.saveJobConfig(jobConfig);
        quartzConfig.scheduleJob(savedJobConfig);  // Schedule the job immediately after adding
        return new ResponseEntity<>(savedJobConfig, HttpStatus.CREATED);
    }
}
