package com.gracetech.jobtest.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gracetech.jobtest.domain.Job;
import com.gracetech.jobtest.service.IJobService;

@RestController("/job")
public class JobController {
	
	@Autowired
	IJobService service;
	
	@GetMapping("/all")
	public List<Job> getAllJobs() {
		return service.findAll();
	}

}
