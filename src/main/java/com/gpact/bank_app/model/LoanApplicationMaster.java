package com.gpact.bank_app.model;

import com.gpact.bank_app.enums.LoanType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanAppId;
    @Valid
    private LoanType loanType;
    @Min(value = 5000, message = "Minimum salary is 5000")
    private Double salary;
    private Double appliedAmount;
    private String user;
    private Integer accNumber;
    private String status;
    private String statusMessage;
}
