package com.lastvalue.demo.config;

import com.lastvalue.demo.entity.PriceRecord;
import com.lastvalue.demo.util.CompleteBatchTasklet;
import com.lastvalue.demo.util.StartBatchTasklet;
import lombok.NonNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private StartBatchTasklet startBatchTasklet;

    @Autowired
    private CompleteBatchTasklet completeBatchTasklet;






    @Bean
    public Job priceJob(Step startBatchStep, Step processPriceStep, Step completeBatchStep, JobRepository jobRepository) {
        return new JobBuilder("priceJob", jobRepository)
                .start(startBatchStep)
                .next(processPriceStep)
                .next(completeBatchStep)
                .build();
    }




    @Bean
    public Step startBatchStep(StartBatchTasklet startBatchTasklet, JobRepository jobRepository,  PlatformTransactionManager transactionManager) {
        System.out.println("in startBatchstep bean ");
        return new StepBuilder("startBatchStep", jobRepository)
                .tasklet(startBatchTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step processPriceStep(ItemReader<PriceRecord> reader, ItemProcessor<PriceRecord, PriceRecord> processor,
                                 ItemWriter<PriceRecord> writer, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processPriceStep", jobRepository)
                .<PriceRecord, PriceRecord>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step completeBatchStep(CompleteBatchTasklet completeBatchTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("completeBatchStep", jobRepository)
                .tasklet(completeBatchTasklet, transactionManager)
                .build();
    }






    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }





    @Bean
    public ItemReader<PriceRecord> reader() {
        // Sample data for demonstration
        List<PriceRecord> sampleData = List.of(
                new PriceRecord("instrument1", LocalDateTime.now(), 100.0),
                new PriceRecord("instrument2", LocalDateTime.now(), 200.0)
        );
        return new ListItemReader<>(sampleData);


    }


    @Bean
    public ItemProcessor<PriceRecord, PriceRecord> processor() {
        return new ItemProcessor<PriceRecord, PriceRecord>() {
            @Override
            public PriceRecord process(PriceRecord priceRecord) {
                // Processing logic (e.g., validation, transformation)
                return priceRecord;
            }
        };
    }

    @Bean
    public ItemWriter<PriceRecord> writer() {
        return items -> items.forEach(item -> System.out.println("Writing item: " + item));
    }



}
