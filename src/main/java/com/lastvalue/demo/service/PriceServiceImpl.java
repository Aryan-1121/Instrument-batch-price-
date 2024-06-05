package com.lastvalue.demo.service;

import com.lastvalue.demo.entity.PriceRecord;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceServiceImpl implements PriceService {
    private final Map<String, PriceRecord> priceStore = new ConcurrentHashMap<>();
    private List<PriceRecord> currentBatch;
    private boolean batchInProgress = false;

    @Override
    public synchronized void startBatchRun() {
            if (batchInProgress) {
                throw new IllegalStateException("Batch run already in progress");
            }
            currentBatch = new ArrayList<>();
            batchInProgress = true;

    }

    @Override
    public void uploadRecords(List<PriceRecord> records) {
        batchInProgress = true;
        System.out.println("batch status : " + batchInProgress);
        if (!batchInProgress) {
            throw new IllegalStateException("No batch run in progress");
        }
        if (records.size() > 1000) {
            throw new IllegalArgumentException("Cannot upload more than 1000 records at a time");
        }
        currentBatch.addAll(records);
    }

    @Override
    public synchronized void completeBatchRun() {
        System.out.println("complete batch service impl ");
        if (!batchInProgress) {
            throw new IllegalStateException("No batch run in progress");
        }
        for (PriceRecord record : currentBatch) {
            PriceRecord existingRecord = priceStore.get(record.getId());
            if (existingRecord == null || existingRecord.getAsOf().isBefore(record.getAsOf())) {
                priceStore.put(record.getId(), record);
            }
        }
        currentBatch = null;
        batchInProgress = false;
    }

    @Override
    public synchronized void cancelBatchRun() {
        if (!batchInProgress) {
            throw new IllegalStateException("No batch run in progress");
        }
        currentBatch = null;
        batchInProgress = false;
    }

    @Override
    public PriceRecord getLastPrice(String id) {
        return priceStore.get(id);
    }
}
