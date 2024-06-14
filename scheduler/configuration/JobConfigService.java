package com.myapp.scheduler.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.scheduler.JobConfigRepository;
import java.util.List;

@Service
public class JobConfigService {

    @Autowired
    private JobConfigRepository jobConfigRepository;

    public List<JobConfig> getAllJobConfigs() {
        return jobConfigRepository.findAll();
    }

    public JobConfig saveJobConfig(JobConfig jobConfig) {
        return jobConfigRepository.save(jobConfig);
    }
}
