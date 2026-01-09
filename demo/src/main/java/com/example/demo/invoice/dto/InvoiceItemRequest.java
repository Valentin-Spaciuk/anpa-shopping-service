package com.example.demo.invoice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InvoiceItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;
}
