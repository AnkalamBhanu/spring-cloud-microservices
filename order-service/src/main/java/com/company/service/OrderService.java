package com.company.service;

import com.company.common.Payment;
import com.company.common.TransactionRequest;
import com.company.common.TransactionResponse;
import com.company.entity.Order;
import com.company.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate = new RestTemplate();

    public TransactionResponse saveOrder(TransactionRequest request) {
        String message = "";

        Order order = request.getOrder();
        Payment payment = request.getPayment();

        order.setId(null);
        orderRepository.save(order);

        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

        // Resolve Payment service URL from Eureka
        List<ServiceInstance> instances = discoveryClient.getInstances("PAYMENT_SERVICE");
        System.out.println("Instances found: " + instances.size());

        String baseUrl = instances.get(0).getUri().toString();
        System.out.println("Resolved Payment URL: " + baseUrl);

        Payment paymentResponse = restTemplate.postForObject(
                baseUrl + "/payment/doPayment",
                payment,
                Payment.class
        );

        message = paymentResponse.getPaymentStatus().equalsIgnoreCase("success")
                ? "payment processing success order placed"
                : "payment failed, order added to cart";

        return new TransactionResponse(order, paymentResponse.getAmount(),
                paymentResponse.getTransactionId(), message);
    }
}