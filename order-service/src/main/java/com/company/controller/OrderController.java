package com.company.controller;

import com.company.common.Payment;
import com.company.common.TransactionRequest;
import com.company.common.TransactionResponse;
import com.company.entity.Order;
import com.company.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @PostMapping("/bookOrder")
    public TransactionResponse bookOrder(@RequestBody TransactionRequest request) {
        //do make REST call to Payment service to get to know which order has received the payment
        return orderService.saveOrder(request);
    }
}
