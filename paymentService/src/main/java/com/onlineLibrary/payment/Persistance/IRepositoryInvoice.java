package com.onlineLibrary.payment.Persistance;

import com.onlineLibrary.payment.Entities.DAO.InvoiceDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositoryInvoice extends JpaRepository<InvoiceDAO, Integer> {
}