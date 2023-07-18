package com.gracetech.jobtest.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Robbile
 *
 */
@Entity
@Table(name = "t_job")
@Getter @Setter
public class Job implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "description", nullable = false) 
	private String name;
	private Instant lastExceutionDate;
	private Instant createdDate = Instant.now();
	private String status;
	
	@Column(name = "frequency", nullable = false) 
	private String frequency;
	private Boolean active;
	
	@Column(name = "taskname", nullable = false) 
	private String taskName;
}
