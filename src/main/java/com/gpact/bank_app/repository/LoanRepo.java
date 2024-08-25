package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.LoanMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepo extends JpaRepository<LoanMaster, Integer> {
}
