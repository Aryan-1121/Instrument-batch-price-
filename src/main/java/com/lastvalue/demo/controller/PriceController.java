package com.lastvalue.demo.controller;

import com.lastvalue.demo.entity.PriceRecord;
import com.lastvalue.demo.service.PriceService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/price")
public class PriceController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job priceJob;

    @Autowired
    private PriceService priceService;

    @PostMapping("/start")
    public void startBatch() {
        try {
            jobLauncher.run(priceJob, new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload")
    public void uploadRecords(@RequestBody List<PriceRecord> records) {
        priceService.uploadRecords(records);
    }

    @PostMapping("/complete")
    public void completeBatch() {
        try {
            jobLauncher.run(priceJob, new JobParametersBuilder().addLong("completeAt", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/cancel")
    public void cancelBatch() {
        priceService.cancelBatchRun();
    }

    @GetMapping("/{id}")
    public PriceRecord getLastPrice(@PathVariable String id) {
        return priceService.getLastPrice(id);
    }
}
