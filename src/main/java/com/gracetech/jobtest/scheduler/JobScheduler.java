package com.gracetech.jobtest.scheduler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.gracetech.jobtest.domain.Job;
import com.gracetech.jobtest.domain.JobResult;
import com.gracetech.jobtest.repository.JobResultRepository;
import com.gracetech.jobtest.service.IJobService;
import com.gracetech.jobtest.task.ManagedTask;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobScheduler {

	@Autowired
	TaskScheduler taskScheduler;
	
	@Autowired
	IJobService jobService;
	
	@Autowired
	JobResultRepository resultRepo;
	
	@Autowired
	private BeanFactory beanFactory;
	
	// a map for keeping scheduling tasks
	Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();
	
	/**add or update a job task in scheduler
	 * @param job
	 */
	public void addOrUpdateTaskToScheduler(Job job) {
		if (Boolean.FALSE.equals(job.getActive())) return;
		Long id = job.getId();
		String taskname = job.getTaskName();
		ScheduledFuture<?> schedulerTask = jobsMap.get(id);
		if (schedulerTask != null) {
			schedulerTask.cancel(true);
			jobsMap.put(id, null);
		}
		try {
			String cron = job.getFrequency();
			if (cron != null && cron.trim().split(" ").length == 7) {
				 /*
                 * remove year field in cron must have 6 fields (sec min hour day month week) Seconds: 0-59 * , - Minutes: 0-59 * , - Hours: 0-23 * , - Day of
                 * Month: 1-31 * , - ? L LW Months: (JAN-DEC or 1-12) * , - Day of Week: (SUN-SAT or 1-7) * , L - ? #
                 */
				int idx = cron.lastIndexOf(" ");
				cron = cron.trim().substring(0, idx);
			}
			// if cron is well set
			if(CronSequenceGenerator.isValidExpression(cron)) {
				ManagedTask mtask = beanFactory.getBean(taskname, ManagedTask.class);
				schedulerTask = taskScheduler.schedule( () -> {
					// execute the job
					Instant i = Instant.now();
					JobResult result = mtask.execute(job);
					result.setStartAt(i);
					// after the execution...
					postJobExecution(result);
				}, new CronTrigger(cron, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
				
				// update taskSchedule list
				jobsMap.put(id, schedulerTask);
			} else {
				log.error("Cron expression ({}) not valid for the job {}", job.getFrequency(), taskname);
			}
		} catch (NoSuchBeanDefinitionException e) {
			log.error("No implementation found for the job {}", taskname);
		}
	}
	
	/*
	 * Post job execution to store result
	 * @param job
	 */
	private void postJobExecution(JobResult jobResult) {
		jobResult.setEndAt(Instant.now());
		resultRepo.save(jobResult);
	}
	

	/**
	 * Remove task from scheduler
	 * @param id
	 */
	public void removeTaskFromScheduler(Long id) {
		ScheduledFuture<?> scheduledTask = jobsMap.get(id);
		if (scheduledTask != null) {
			scheduledTask.cancel(true);
			jobsMap.put(id, null);
		}
	}
	
	// a context refresh event listener
	@EventListener({ ContextRefreshedEvent.class})
	void contextRefreshedEvent() {
		// get all task from DB and reschedule them in case of context restarted
		List<Job> jobs = jobService.findAll();
		
		for( Job j : jobs) {
			addOrUpdateTaskToScheduler(j);
		}
	}
}
