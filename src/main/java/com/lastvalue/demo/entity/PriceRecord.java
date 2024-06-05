package com.lastvalue.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PriceRecord {
    private String id;
    private LocalDateTime asOf;
    private Object payload;


}
