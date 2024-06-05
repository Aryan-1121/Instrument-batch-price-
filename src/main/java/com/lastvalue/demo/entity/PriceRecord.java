package com.lastvalue.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PriceRecord {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private LocalDateTime asOf;

//    private PayLoad payload;
    private double payLoad;

}
