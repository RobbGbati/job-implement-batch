package com.gracetech.jobtest;

import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gracetech.jobtest.domain.Job;
import com.gracetech.jobtest.service.IJobService;

@Component
public class SimpleTaskTest {

	@Autowired
	IJobService jobservice;
	
	@PostConstruct
	private void init() {
		Job job = new Job();
		job.setActive(true);
		// every seconds
		job.setFrequency(" 0 * * * * * ?");
		job.setName("Ceci est un simple task");
		job.setStatus("START");
		job.setCreatedDate(Instant.now());
		job.setTaskName("simpletask");
		this.jobservice.createJob(job);
	}
}
