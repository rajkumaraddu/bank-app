package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.LoanApplicationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepo extends JpaRepository<LoanApplicationMaster, Integer> {
    List<LoanApplicationMaster> findByStatus(String underProcess);
}
