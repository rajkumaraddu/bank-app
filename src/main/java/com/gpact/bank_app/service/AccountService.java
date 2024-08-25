package com.gpact.bank_app.service;

import com.gpact.bank_app.dto.FundTransferDto;
import com.gpact.bank_app.enums.AccountType;
import com.gpact.bank_app.enums.AddressType;
import com.gpact.bank_app.enums.TransactionType;
import com.gpact.bank_app.exception.AccountNumberNotFoundException;
import com.gpact.bank_app.exception.InsufficientBalanceException;
import com.gpact.bank_app.model.*;
import com.gpact.bank_app.repository.*;
import com.gpact.bank_app.util.BankAppUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountFormRepo accountFormRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    BankAppUtil bankAppUtil;

    public List<AccountMaster> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Optional<AccountMaster> getAccountNumber(Integer accNumber) {
        return accountRepo.findById(accNumber);
    }

    @Transactional
    public FundTransferDto fundTransfer(FundTransferDto fundTransferDto) {

        Integer fromAccountNumber = fundTransferDto.getFromAccount();
        Integer toAccountNumber = fundTransferDto.getToAccount();
        Double transferAmount = fundTransferDto.getAmount();

        AccountMaster fromAccountMaster = accountRepo.findById(fromAccountNumber).orElse(null);
        AccountMaster toAccountMaster = accountRepo.findById(toAccountNumber).orElse(null);

        if (fromAccountMaster == null) {

            throw new AccountNumberNotFoundException("From account number '" + fromAccountNumber + "' is not valid");
        } else if (toAccountMaster == null) {

            throw new AccountNumberNotFoundException("To account number '" + toAccountNumber + "' is not valid");
        } else if (transferAmount > fromAccountMaster.getBalance()) {

            throw new InsufficientBalanceException("Insufficient balance!");
        } else {

            fromAccountMaster.setBalance(fromAccountMaster.getBalance() - transferAmount);
            accountRepo.save(fromAccountMaster);

            toAccountMaster.setBalance(toAccountMaster.getBalance() + transferAmount);
            accountRepo.save(toAccountMaster);
            fundTransferDto.setStatus("Success");

            this.logTransaction(fundTransferDto, TransactionType.FUNDTRANSFER);
        }
        return fundTransferDto;
    }

    @Transactional
    public FundTransferDto deposit(FundTransferDto fundTransferDto) {

        Integer toAccountNumber = fundTransferDto.getToAccount();
        Double transferAmount = fundTransferDto.getAmount();

        AccountMaster toAccountMaster = accountRepo.findById(toAccountNumber).orElse(null);

        if (toAccountMaster == null) {

            throw new AccountNumberNotFoundException("To account number '" + toAccountNumber + "' is not valid");
        } else {

            toAccountMaster.setBalance(toAccountMaster.getBalance() + transferAmount);
            accountRepo.save(toAccountMaster);

            fundTransferDto.setStatus("Success");
            this.logTransaction(fundTransferDto, TransactionType.DEPOSIT);
        }
        return fundTransferDto;
    }

    @Transactional
    public FundTransferDto withdraw(FundTransferDto fundTransferDto) {

        Integer fromAccountNumber = fundTransferDto.getFromAccount();
        Double transferAmount = fundTransferDto.getAmount();

        AccountMaster fromAccountMaster = accountRepo.findById(fromAccountNumber).orElse(null);

        if (fromAccountMaster == null) {

            throw new AccountNumberNotFoundException("From account number '" + fromAccountNumber + "' is not valid");
        } else {

            fromAccountMaster.setBalance(fromAccountMaster.getBalance() - transferAmount);
            accountRepo.save(fromAccountMaster);

            fundTransferDto.setStatus("Success");
            this.logTransaction(fundTransferDto, TransactionType.WITHDRAW);
        }
        return fundTransferDto;
    }

    @Async
    private void logTransaction(FundTransferDto fundTransferDto, TransactionType transactionType) {

        TransactionMaster transactionMaster = new TransactionMaster();
        transactionMaster.setTranType(transactionType.name());
        transactionMaster.setFromAccount(fundTransferDto.getFromAccount());
        transactionMaster.setToAccount(fundTransferDto.getToAccount());
        transactionMaster.setAmount(fundTransferDto.getAmount());
        transactionMaster.setTranDateTime(LocalDateTime.now());

        transactionRepo.save(transactionMaster);
    }

    public AccountFormMaster createAccount(AccountFormMaster accountFormMaster) {

        accountFormMaster.setStatus("Under process");
        accountFormMaster.setStatusMessage("Request submitted successfully!. Will notify you once your details verified.");
        return accountFormRepo.save(accountFormMaster);
    }

    public List<TransactionMaster> getStatement(Authentication authentication) {
        String username = authentication.getName();
        AccountMaster accountByUserName = bankAppUtil.getAccountByUserName(username);
        if (accountByUserName != null) {
            Integer accNumber = accountByUserName.getAccNumber();
            return transactionRepo.findByFromAccountOrToAccount(accNumber, accNumber);
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<AccountFormMaster> createAccountFromForm() {

        List<AccountFormMaster> accountFormMasterList = accountFormRepo.findByStatus("Under process");
        List<AccountFormMaster> accountFormMasterListUpdated = new ArrayList<>();

        for (AccountFormMaster accountFormMaster : accountFormMasterList) {

            UserMaster userMaster = new UserMaster();
            userMaster.setUsername(accountFormMaster.getUsername());
            userMaster.setPassword(passwordEncoder.encode(accountFormMaster.getUsername() + "pwd"));
            userMaster.setMobile(accountFormMaster.getMobile());
            userMaster.setAge(accountFormMaster.getAge());
            userMaster.setEmail(accountFormMaster.getEmail());
            userMaster.setAadhar(accountFormMaster.getAadhar());
            userMaster.setPan(accountFormMaster.getPan());
            userMaster.setRoles("ROLE_USER");
            UserMaster savedUser = userRepo.save(userMaster);

            AddressMaster addressMaster = new AddressMaster();
            addressMaster.setAddressType(AddressType.PERMANENT.name());
            addressMaster.setAddress(accountFormMaster.getAddress());
            addressRepo.save(addressMaster);

            AccountMaster accountMaster = new AccountMaster();
            accountMaster.setAccStatus("Active");
            accountMaster.setBalance(0.0);
            String accType = accountFormMaster.getAccType().startsWith("S") ? AccountType.SAVINGS.name() : AccountType.CURRENT.name();
            accountMaster.setAccType(accType);
            accountMaster.setUser(savedUser);
            AccountMaster savedAccount = accountRepo.save(accountMaster);

            if (savedAccount.getAccNumber() != null) {
                accountFormMaster.setStatus("Account created!");
                accountFormMaster.setStatusMessage("Account number is: " + savedAccount.getAccNumber());
            } else {
                accountFormMaster.setStatus("Failed!");
                accountFormMaster.setStatusMessage("Account failed to create the account with given details.");
            }
            accountFormMasterListUpdated.add(accountFormMaster);

        }
        return accountFormRepo.saveAll(accountFormMasterListUpdated);
    }

}
