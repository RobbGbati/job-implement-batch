package com.gracetech.jobtest.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gracetech.jobtest.domain.Job;
import com.gracetech.jobtest.repository.JobRepository;
import com.gracetech.jobtest.scheduler.JobScheduler;
import com.gracetech.jobtest.service.IJobService;


/**
 * 
 * @author Robbile
 * This jobservice uses JobScheduler for scheduling
 *
 */
@Service
public class JobService implements IJobService {
	
	@Autowired
	private JobRepository repo;
	
	@Autowired
	JobScheduler jobScheduler;
	
	@Override
	public Long createJob(Job job) {
		Job saved = repo.save(job);
		jobScheduler.addOrUpdateTaskToScheduler(saved);
		return saved.getId();
	}

	@Override
	public void deleteJob(Long id) {
		if (this.findById(id)!=null) {
			this.repo.deleteById(id);
			jobScheduler.removeTaskFromScheduler(id);
		}
	}

	@Override
	public Long updateJob(Job job) {
		Optional<Job> j = this.repo.findById(job.getId());
		if (j.isPresent()) {
			jobScheduler.addOrUpdateTaskToScheduler(job);
			return this.repo.save(job).getId();
		}
		return null;
	}

	@Override
	public Job findById(Long id) {
		return this.repo.getById(id);
	}

	@Override
	public List<Job> findAll() {
		return this.repo.findAll();
	}

}
