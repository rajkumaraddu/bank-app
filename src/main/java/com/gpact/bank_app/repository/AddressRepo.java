package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.AddressMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<AddressMaster, Integer> {
}
