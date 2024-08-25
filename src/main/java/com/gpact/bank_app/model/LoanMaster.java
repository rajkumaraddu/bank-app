package com.gpact.bank_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;
    private String loanType;
    private String loanStatus;
    private Integer accNumber;
    private Double sanctionedAmount;
    private Double disbursedAmount;
    private Long tenure;
    private Integer emi;
    private Integer loanAppId;
}
