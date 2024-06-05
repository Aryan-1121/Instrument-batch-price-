package com.lastvalue.demo.service;

import com.lastvalue.demo.entity.PriceRecord;

import java.util.List;

public interface PriceService {
    void startBatchRun();
    void uploadRecords(List<PriceRecord> records);
    void completeBatchRun();
    void cancelBatchRun();
    PriceRecord getLastPrice(String id);
}
