package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.AccountFormMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountFormRepo extends JpaRepository<AccountFormMaster, Integer> {
    List<AccountFormMaster> findByStatus(String underProcess);
}
