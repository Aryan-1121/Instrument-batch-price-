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
            System.out.println("controller : /start ");
            jobLauncher.run(priceJob, new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/upload")
    public void uploadRecords(@RequestBody List<PriceRecord> records) {
        System.out.println("in controller : upload");
//        System.out.println(records);
            for(PriceRecord record : records){
                System.out.println(record);
            }

        priceService.uploadRecords(records);
    }

    @PostMapping("/complete")
    public void completeBatch() {
        try {
            System.out.println("in controller : /complete");
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
