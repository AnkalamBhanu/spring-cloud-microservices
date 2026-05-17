package com.company.service;

import com.company.common.Payment;
import com.company.common.TransactionRequest;
import com.company.common.TransactionResponse;
import com.company.entity.Order;
import com.company.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RefreshScope
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${microservice.payment-service.base-url}")
    private String paymentServiceBaseUrl;

    @Value("${microservice.payment-service.endpoints.do-payment}")
    private String doPaymentEndpoint;

    Logger logger= LoggerFactory.getLogger(OrderService.class);

//    @Value("${microservice.payment-service.endpoints.payment-history}")
//    private String paymentHistoryEndpoint;

    private RestTemplate restTemplate = new RestTemplate();

    public TransactionResponse saveOrder(TransactionRequest request) {
        String message = "";

        Order order = request.getOrder();
        Payment payment = request.getPayment();

        order.setId(null);
        orderRepository.save(order);

        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

//        logger.info("OrderService request: {}",new ObjectMapper().writeValueAsString(request));

        // Resolve Payment service URL from Eureka
        List<ServiceInstance> instances = discoveryClient.getInstances(paymentServiceBaseUrl);
        System.out.println("Instances found: " + instances.size());

        String baseUrl = instances.get(0).getUri().toString();
        System.out.println("Resolved Payment URL: " + baseUrl);

        Payment paymentResponse = restTemplate.postForObject(
                baseUrl + doPaymentEndpoint,
                payment,
                Payment.class
        );

//        logger.info("Payment service response from Order service REST call: {}",new ObjectMapper().writeValueAsString(paymentResponse));

        message = paymentResponse.getPaymentStatus().equalsIgnoreCase("success")
                ? "payment processing success order placed"
                : "payment failed, order added to cart";

        return new TransactionResponse(order, paymentResponse.getAmount(),
                paymentResponse.getTransactionId(), message);
    }
}