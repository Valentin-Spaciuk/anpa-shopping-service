package com.example.demo.invoice.dto;

import com.example.demo.clients.model.CustomerModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class InvoiceDetailsResponse {
    private Long id;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private LocalDateTime issuedAt;
    private String status;

    private CustomerModel customer;
    private List<ProductLine> products;

    @Getter @Setter
    public static class ProductLine {
        private Long productId;
        private String sku;
        private String name;
        private BigDecimal price;
        private Integer quantity;
    }
}
