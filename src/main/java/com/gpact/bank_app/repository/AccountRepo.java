package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.AccountMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<AccountMaster, Integer> {
}
