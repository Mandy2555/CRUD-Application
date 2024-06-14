package com.myapp.scheduler;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myapp.scheduler.configuration.JobConfig;

public interface JobConfigRepository extends MongoRepository<JobConfig, String> {
}
