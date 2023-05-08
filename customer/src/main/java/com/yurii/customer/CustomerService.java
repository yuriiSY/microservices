package com.yurii.customer;

import com.yurii.amqp.RabbitMQMessageProducer;
import com.yurii.clients.fraud.FraudCheckResponse;
import com.yurii.clients.fraud.FraudClient;
import com.yurii.clients.notification.NotificationClient;
import com.yurii.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer producer;
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);


        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to my web page", customer.getFirstName())
                );
        producer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
