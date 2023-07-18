package com.gracetech.jobtest.service;

import java.util.List;

import com.gracetech.jobtest.domain.Job;

public interface IJobService {

	public Long createJob(Job job);
	public void deleteJob(Long id);
	public Long updateJob(Job job);
	public Job findById(Long id);
	public List<Job> findAll();
}
