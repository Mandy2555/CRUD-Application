package com.myapp.scheduler.configuration;

import java.util.List;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.myapp.scheduler.ApiCallJob;

import jakarta.annotation.PostConstruct;

//@Configuration
//public class QuartzConfig {
//
//    // Define your Job class
//    @Bean
//    public JobDetailFactoryBean jobDetail() {
//        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
//        factoryBean.setJobClass(ApiCallJob.class); // Your job class
//        return factoryBean;
//    }
//
//    // Define the Trigger for the Job
//    @Bean
//    public SimpleTriggerFactoryBean trigger(JobDetail job) {
//        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
//        factoryBean.setJobDetail(job);
//        factoryBean.setStartDelay(0L); // Start immediately
//        factoryBean.setRepeatInterval(60000L); // Repeat every 60 seconds
//        return factoryBean;
//    }
//
//    // Define Quartz Scheduler
//    @Bean
//    public SchedulerFactoryBean scheduler(Trigger trigger) {
//        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//        factoryBean.setTriggers(trigger);
//        return factoryBean;
//    }
//    
//    @Bean
//    public CronTriggerFactoryBean cronTrigger(JobDetail job) {
//        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
//        factoryBean.setJobDetail(job);
//        factoryBean.setCronExpression("0 * * ? * *"); // Example cron expression to trigger every minute
//        return factoryBean;
//    }
//}


@Configuration
public class QuartzConfig {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobConfigService jobConfigService;

    @PostConstruct
    public void scheduleJobs() {
        List<JobConfig> jobConfigs = jobConfigService.getAllJobConfigs();
        jobConfigs.forEach(this::scheduleJob);
    }

    public void scheduleJob(JobConfig jobConfig) {
        try {
            // Load the job class
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobConfig.getJobClass());

            // Create job detail
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobConfig.getJobName(), jobConfig.getJobGroup())
                    .storeDurably()
                    .build();

            // Create trigger with cron expression
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobConfig.getJobName() + "Trigger", jobConfig.getJobGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobConfig.getCronExpression()))
                    .build();

            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException | SchedulerException e) {
            e.printStackTrace();
        }
    }
}
