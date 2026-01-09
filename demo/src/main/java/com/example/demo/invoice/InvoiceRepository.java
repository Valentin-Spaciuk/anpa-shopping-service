package com.example.demo.invoice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("select i from Invoice i left join fetch i.items where i.id = :id")
    Optional<Invoice> findByIdWithItems(@Param("id") Long id);
}
