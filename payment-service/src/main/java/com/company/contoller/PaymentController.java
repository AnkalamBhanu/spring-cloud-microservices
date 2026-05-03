package com.company.contoller;

import com.company.entity.Payment;
import com.company.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/doPayment")
    public Payment doPayment(@RequestBody Payment payment){
        return paymentService.doPayment(payment);
    }

    @GetMapping("{orderId}")
    public Payment fetchPaymentHistoryByOrderId(@PathVariable int orderId){
        return paymentService.findPaymentHistoryByOrderId(orderId);
    }


}
