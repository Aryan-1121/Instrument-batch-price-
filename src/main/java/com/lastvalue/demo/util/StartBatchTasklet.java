package com.lastvalue.demo.util;

import com.lastvalue.demo.service.PriceService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class StartBatchTasklet implements Tasklet {

    private final PriceService priceService;



    public StartBatchTasklet(PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        priceService.startBatchRun();
        return RepeatStatus.FINISHED;
    }
}


