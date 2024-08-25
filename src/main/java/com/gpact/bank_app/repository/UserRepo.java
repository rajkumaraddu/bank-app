package com.gpact.bank_app.repository;

import com.gpact.bank_app.model.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserMaster, Integer> {
    Optional<UserMaster> findByUsername(String username);
}
