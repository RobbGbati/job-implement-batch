package com.gracetech.jobtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gracetech.jobtest.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>  {

}
