package com.company.service;

import com.company.entity.Payment;
import com.company.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentService {

    @Autowired
    public PaymentRepository paymentRepository;

    Logger logger= LoggerFactory.getLogger(PaymentService.class);

    public Payment doPayment(Payment payment) {

//        logger.info("Payment service request: {}",new ObjectMapper().writeValueAsString(payment));

        payment.setPaymentStatus(paymentProcessing());
        payment.setTransactionId(UUID.randomUUID().toString());
        return paymentRepository.save(payment);
    }

    public String paymentProcessing(){
        //API should be 3rd party payment gateway(paypal,UPI etc)
        return new Random().nextBoolean()?"success":"false";
//        return "success";
    }

    public Payment findPaymentHistoryByOrderId(int orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
//        logger.info("paymentService findPaymentHistoryByOrderId : {}",new ObjectMapper().writeValueAsString(payment));
        return payment ;
    }
}
