package com.gpact.bank_app.model;

import com.gpact.bank_app.enums.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tranId;
    private String tranType;
    private Integer fromAccount;
    private Integer toAccount;
    private Double amount;
    private LocalDateTime tranDateTime;

}
