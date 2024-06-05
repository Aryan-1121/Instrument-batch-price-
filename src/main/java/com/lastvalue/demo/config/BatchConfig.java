package com.lastvalue.demo.config;

import com.lastvalue.demo.entity.PriceRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilder jobBuilderFactory;
    private final StepBuilder stepBuilderFactory;

    @Autowired
    private JobRepository jobRepository;

    public BatchConfig(JobBuilder jobBuilderFactory, StepBuilder stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job priceJob(Step startBatchStep, Step processPriceStep, Step completeBatchStep, JobRepository jobRepository) {
        return new JobBuilder("priceJob", jobRepository)
                .start(startBatchStep)
                .next(processPriceStep)
                .next(completeBatchStep)
                .build();
    }



    @Bean
    public Step startBatchStep(Tasklet startBatchTasklet, JobRepository jobRepository) {
        return new StepBuilder("startBatchStep", jobRepository)
                .tasklet(startBatchTasklet)
                .build();
    }

    @Bean
    public Step processPriceStep(ItemReader<PriceRecord> reader, ItemProcessor<PriceRecord, PriceRecord> processor,
                                 ItemWriter<PriceRecord> writer, JobRepository jobRepository) {
        return new StepBuilder("processPriceStep", jobRepository)
                .<PriceRecord, PriceRecord>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step completeBatchStep(Tasklet completeBatchTasklet) {
        return new StepBuilder("completeBatchStep", jobRepository)
                .tasklet(completeBatchTasklet)
                .build();
    }
}
