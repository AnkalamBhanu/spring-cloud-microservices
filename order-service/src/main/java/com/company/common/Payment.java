package com.company.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    // As a DTO not an Entity
    private Integer paymentId;

    private String paymentStatus;
    private String transactionId;
    private Integer orderId;
    private Double amount;

}
