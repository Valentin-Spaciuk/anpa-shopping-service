package com.example.demo.invoice;

import com.example.demo.invoice.dto.InvoiceCreateRequest;
import com.example.demo.invoice.dto.InvoiceDetailsResponse;
import com.example.demo.invoice.dto.InvoiceResponse;
import com.example.demo.invoice.dto.InvoiceUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<InvoiceResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public InvoiceDetailsResponse getById(@PathVariable Long id) {
        return service.findDetailsById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Invoice create(@Valid @RequestBody InvoiceCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Invoice update(@PathVariable Long id, @Valid @RequestBody InvoiceUpdateRequest request) {
        return service.update(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
