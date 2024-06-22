package com.study.springbatch;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

	private final Logger logger = LoggerFactory.getLogger(JobController.class);

	private AtomicInteger batchRunCounter = new AtomicInteger(0);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private Job writeCoffeeJob;

	@GetMapping("/batch-job")
	public void callBatchJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		// batchConfiguration.importUserJob(jobRepository, null, null);
		Date date = new Date();
		logger.debug("scheduler starts at " + date);
		JobExecution jobExecution = jobLauncher.run(writeCoffeeJob,
				new JobParametersBuilder().addDate("launchDate", date).toJobParameters());
		batchRunCounter.incrementAndGet();
		logger.debug("Batch job ends with status as " + jobExecution.getStatus());
		logger.debug("scheduler ends ");
	}

}
