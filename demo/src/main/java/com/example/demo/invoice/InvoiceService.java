package com.example.demo.invoice;

import com.example.demo.clients.CustomerClient;
import com.example.demo.clients.ProductClient;
import com.example.demo.clients.model.CustomerModel;
import com.example.demo.clients.model.ProductModel;
import com.example.demo.invoice.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repo;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public InvoiceService(InvoiceRepository repo, CustomerClient customerClient, ProductClient productClient) {
        this.repo = repo;
        this.customerClient = customerClient;
        this.productClient = productClient;
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> findAll() {
        return repo.findAll().stream().map(inv -> {
            InvoiceResponse r = new InvoiceResponse();
            r.setId(inv.getId());
            r.setInvoiceNumber(inv.getInvoiceNumber());
            r.setTotalAmount(inv.getTotalAmount());
            r.setIssuedAt(inv.getIssuedAt());
            r.setStatus(inv.getStatus());
            r.setCustomerId(inv.getCustomerId());
            return r;
        }).toList();
    }

    @Transactional(readOnly = true)
    public Invoice findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice no encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public InvoiceDetailsResponse findDetailsById(Long id) {
        Invoice invoice = repo.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Invoice no encontrada: " + id));

        CustomerModel customer = customerClient.getCustomerById(invoice.getCustomerId());

        List<InvoiceDetailsResponse.ProductLine> lines = new ArrayList<>();
        for (InvoiceItem item : invoice.getItems()) {
            ProductModel p = productClient.getProductById(item.getProductId());

            InvoiceDetailsResponse.ProductLine line = new InvoiceDetailsResponse.ProductLine();
            line.setProductId(p.getId());
            line.setSku(p.getSku());
            line.setName(p.getName());
            line.setPrice(p.getPrice());
            line.setQuantity(item.getQuantity());
            lines.add(line);
        }

        InvoiceDetailsResponse resp = new InvoiceDetailsResponse();
        resp.setId(invoice.getId());
        resp.setInvoiceNumber(invoice.getInvoiceNumber());
        resp.setTotalAmount(invoice.getTotalAmount());
        resp.setIssuedAt(invoice.getIssuedAt());
        resp.setStatus(invoice.getStatus());
        resp.setCustomer(customer);
        resp.setProducts(lines);
        return resp;
    }

    @Transactional
    public Invoice create(InvoiceCreateRequest req) {
        if (repo.existsByInvoiceNumber(req.getInvoiceNumber())) {
            throw new RuntimeException("Ya existe invoiceNumber: " + req.getInvoiceNumber());
        }

        Invoice invoice = Invoice.builder()
                .invoiceNumber(req.getInvoiceNumber())
                .totalAmount(req.getTotalAmount())
                .issuedAt(req.getIssuedAt())
                .status(req.getStatus())
                .customerId(req.getCustomerId())
                .items(new ArrayList<>())
                .build();

        for (InvoiceItemRequest itemReq : req.getItems()) {
            InvoiceItem item = InvoiceItem.builder()
                    .productId(itemReq.getProductId())
                    .quantity(itemReq.getQuantity())
                    .invoice(invoice)
                    .build();
            invoice.getItems().add(item);
        }

        return repo.save(invoice);
    }

    @Transactional
    public Invoice update(Long id, InvoiceUpdateRequest req) {
        Invoice current = repo.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Invoice no encontrada: " + id));

        if (!current.getInvoiceNumber().equalsIgnoreCase(req.getInvoiceNumber())
                && repo.existsByInvoiceNumber(req.getInvoiceNumber())) {
            throw new RuntimeException("Ya existe invoiceNumber: " + req.getInvoiceNumber());
        }

        current.setInvoiceNumber(req.getInvoiceNumber());
        current.setTotalAmount(req.getTotalAmount());
        current.setIssuedAt(req.getIssuedAt());
        current.setStatus(req.getStatus());
        current.setCustomerId(req.getCustomerId());

        current.getItems().clear();
        for (InvoiceItemRequest itemReq : req.getItems()) {
            InvoiceItem item = InvoiceItem.builder()
                    .productId(itemReq.getProductId())
                    .quantity(itemReq.getQuantity())
                    .invoice(current)
                    .build();
            current.getItems().add(item);
        }

        return repo.save(current);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Invoice no encontrada: " + id);
        }
        repo.deleteById(id);
    }
}
