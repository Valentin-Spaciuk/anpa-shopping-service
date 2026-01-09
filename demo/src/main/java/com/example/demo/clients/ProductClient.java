package com.example.demo.clients;

import com.example.demo.clients.model.ProductModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "anpa-product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductModel getProductById(@PathVariable("id") Long id);
}
