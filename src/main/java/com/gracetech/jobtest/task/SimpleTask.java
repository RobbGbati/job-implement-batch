package com.gracetech.jobtest.task;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gracetech.jobtest.domain.Job;
import com.gracetech.jobtest.domain.JobResult;
import com.gracetech.jobtest.service.IJobService;

@Component("simpletask")
public class SimpleTask extends ManagedTask {
	
	@Autowired
	IJobService jobService;

	@Override
	public JobResult execute(Job job) {
		System.out.println("Début exécution tâche");
		JobResult jr = new JobResult();
		jr.setMessage("Je suis dans le simpletask");
		jr.setStatus("Début");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			jr.setMessage("Interruption dans l'execution");
			jr.setStatus("Echec");
			return jr;
		}
		jr.setMessage("Fin exécution");
		jr.setStatus("Fini");
		job.setLastExceutionDate(Instant.now());
		jobService.updateJob(job);
		System.out.println("Fin exécution tâche");
		return jr;
	}

}
