package com.lastvalue.demo.repo;

import com.lastvalue.demo.entity.PriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRecordRepo extends JpaRepository<PriceRecord, String> {
}
