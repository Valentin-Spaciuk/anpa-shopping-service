package com.example.demo.clients;

import com.example.demo.clients.model.CustomerModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "anpa-customer-service")
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}")
    CustomerModel getCustomerById(@PathVariable("id") Long id);
}
