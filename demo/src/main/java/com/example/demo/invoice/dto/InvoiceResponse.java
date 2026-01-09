package com.example.demo.invoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private LocalDateTime issuedAt;
    private String status;
    private Long customerId;
}
