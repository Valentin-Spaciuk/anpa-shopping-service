package com.example.demo.invoice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class InvoiceCreateRequest {

    @NotBlank
    private String invoiceNumber;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @NotNull
    private LocalDateTime issuedAt;

    @NotBlank
    private String status;

    @NotNull
    private Long customerId;

    @Valid
    @NotNull
    private List<InvoiceItemRequest> items;
}
