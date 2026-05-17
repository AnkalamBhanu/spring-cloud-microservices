package com.company.controller;

import com.company.common.TransactionRequest;
import com.company.common.TransactionResponse;
import com.company.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @PostMapping("/bookOrder")
    @CircuitBreaker(name = "paymentService", fallbackMethod = "orderServiceFallback") //Circuit breaker should usually represent:downstream dependency being protected
    public TransactionResponse bookOrder(@RequestBody TransactionRequest request) {
        //do make REST call to Payment service to get to know which order has received the payment
        return orderService.saveOrder(request);
    }

    public TransactionResponse orderServiceFallback(
            TransactionRequest request,
            Exception ex) {
        TransactionResponse response = new TransactionResponse();
        response.setMessage("Payment service is down. Please try again later.");
        return response;
    }
}
