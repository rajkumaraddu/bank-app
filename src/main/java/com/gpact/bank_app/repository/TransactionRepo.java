package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.TransactionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionMaster, Integer> {

    List<TransactionMaster> findByFromAccountOrToAccount(Integer accNumber, Integer number);
}
