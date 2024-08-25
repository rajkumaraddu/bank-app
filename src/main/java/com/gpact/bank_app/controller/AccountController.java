package com.gpact.bank_app.controller;

import com.gpact.bank_app.dto.FundTransferDto;
import com.gpact.bank_app.model.AccountMaster;
import com.gpact.bank_app.model.AccountFormMaster;
import com.gpact.bank_app.model.LoanApplicationMaster;
import com.gpact.bank_app.model.TransactionMaster;
import com.gpact.bank_app.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<AccountFormMaster> createAccount(@Valid @RequestBody AccountFormMaster accountFormMaster) {
        return new ResponseEntity<>(accountService.createAccount(accountFormMaster), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allAccounts")
    public ResponseEntity<List<AccountMaster>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{accNumber}")
    public ResponseEntity<Optional<AccountMaster>> getAccountNumber(@PathVariable Integer accNumber) {
        return new ResponseEntity<>(accountService.getAccountNumber(accNumber), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/fundTransfer")
    public ResponseEntity<FundTransferDto> fundTransfer(@RequestBody FundTransferDto fundTransferDto) {
        return new ResponseEntity<>(accountService.fundTransfer(fundTransferDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/deposit")
    public ResponseEntity<FundTransferDto> deposit(@Valid @RequestBody FundTransferDto fundTransferDto) {
        return new ResponseEntity<>(accountService.deposit(fundTransferDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/withdraw")
    public ResponseEntity<FundTransferDto> withdraw(@RequestBody FundTransferDto fundTransferDto) {
        return new ResponseEntity<>(accountService.withdraw(fundTransferDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getStatement")
    public ResponseEntity<List<TransactionMaster>> getStatement(Authentication authentication) {
        return new ResponseEntity<>(accountService.getStatement(authentication), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createAccountFromForm")
    public ResponseEntity<List<AccountFormMaster>> createAccountFromForm() {
        return new ResponseEntity<>(accountService.createAccountFromForm(), HttpStatus.OK);
    }


}
