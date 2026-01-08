package com.example.demo.invoice;

import com.example.demo.invoice.dto.InvoiceCreateRequest;
import com.example.demo.invoice.dto.InvoiceUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repo;

    public InvoiceService(InvoiceRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Invoice findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice no encontrada: " + id));
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
                .build();

        return repo.save(invoice);
    }

    @Transactional
    public Invoice update(Long id, InvoiceUpdateRequest req) {
        Invoice current = findById(id);

        if (!current.getInvoiceNumber().equalsIgnoreCase(req.getInvoiceNumber())
                && repo.existsByInvoiceNumber(req.getInvoiceNumber())) {
            throw new RuntimeException("Ya existe invoiceNumber: " + req.getInvoiceNumber());
        }

        current.setInvoiceNumber(req.getInvoiceNumber());
        current.setTotalAmount(req.getTotalAmount());
        current.setIssuedAt(req.getIssuedAt());
        current.setStatus(req.getStatus());

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
