package com.gpact.bank_app.controller;

import com.gpact.bank_app.model.LoanApplicationMaster;
import com.gpact.bank_app.model.LoanMaster;
import com.gpact.bank_app.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    LoanService loanService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/applyLoan")
    public ResponseEntity<LoanApplicationMaster> applyLoan(@Valid @RequestBody LoanApplicationMaster loanApplicationMaster,
                                                           Authentication authentication) {
        return loanService.applyLoan(loanApplicationMaster, authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createLoanFromApplication")
    public ResponseEntity<List<LoanApplicationMaster>> createLoanFromApplication() {
        return loanService.createLoanFromApplication();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getLoan/{id}")
    public ResponseEntity<LoanMaster> getLoanById(@PathVariable Integer id) {
        return new ResponseEntity<>(loanService.getLoanById(id), HttpStatus.OK);
    }

}
