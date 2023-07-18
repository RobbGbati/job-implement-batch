package com.gracetech.jobtest.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gracetech.jobtest.domain.Job;
import com.gracetech.jobtest.domain.JobResult;
import com.gracetech.jobtest.service.IJobService;

/*
 * Une classe abstraite dont l'heritage permet 
 * D'exécuter la tâche
 */
@Component
public abstract class ManagedTask {
	
	@Autowired
	IJobService service;
	
	public abstract JobResult execute(Job job);

}
