package com.gracetech.jobtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gracetech.jobtest.domain.JobResult;

@Repository
public interface JobResultRepository extends JpaRepository<JobResult, Long> {

}
