package com.smartspending.transaction.repository;

import com.smartspending.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
