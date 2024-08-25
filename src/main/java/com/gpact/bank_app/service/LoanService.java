package com.gpact.bank_app.service;

import com.gpact.bank_app.model.AccountMaster;
import com.gpact.bank_app.model.LoanApplicationMaster;
import com.gpact.bank_app.model.LoanMaster;
import com.gpact.bank_app.repository.LoanApplicationRepo;
import com.gpact.bank_app.repository.LoanRepo;
import com.gpact.bank_app.util.BankAppUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    LoanApplicationRepo loanApplicationRepo;

    @Autowired
    LoanRepo loanRepo;

    @Autowired
    BankAppUtil bankAppUtil;

    public ResponseEntity<LoanApplicationMaster> applyLoan(@Valid LoanApplicationMaster loanApplicationMaster,
                                                           Authentication authentication) {

        String username = authentication.getName();
        loanApplicationMaster.setUser(username);
        AccountMaster accountByUserName = bankAppUtil.getAccountByUserName(username);

        if (accountByUserName != null) {
            loanApplicationMaster.setAccNumber(accountByUserName.getAccNumber());
            loanApplicationMaster.setStatus("Under process");
            loanApplicationMaster.setStatusMessage("Loan application submitted successfully!. Will notify you once your details verified.");
            return new ResponseEntity<>(loanApplicationRepo.save(loanApplicationMaster), HttpStatus.ACCEPTED);
        } else {
            loanApplicationMaster.setStatus("Failed");
            loanApplicationMaster.setStatusMessage("You don't have account to apply loan. kindly create an account and try to apply.");
            return new ResponseEntity<>(loanApplicationRepo.save(loanApplicationMaster), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public ResponseEntity<List<LoanApplicationMaster>> createLoanFromApplication() {
        List<LoanApplicationMaster> loanApplicationMasterList = loanApplicationRepo.findByStatus("Under process");
        List<LoanApplicationMaster> loanApplicationMasterListSaved = new ArrayList<>();


        for (LoanApplicationMaster loanApplicationMaster : loanApplicationMasterList) {
            LoanMaster loanMaster = new LoanMaster();
            loanMaster.setAccNumber(loanApplicationMaster.getAccNumber());
            loanMaster.setLoanType(loanApplicationMaster.getLoanType().name());
            loanMaster.setLoanAppId(loanApplicationMaster.getLoanAppId());
            Double salary = loanApplicationMaster.getSalary();
            Double appliedAmount = loanApplicationMaster.getAppliedAmount();

            if (salary >= 5000 && salary < 20000) {
                loanMaster.setTenure(Math.round(appliedAmount/36.00));
                loanMaster.setEmi(12);
            } else if (salary > 20000 && salary < 50000) {
                loanMaster.setTenure(Math.round(appliedAmount/24.00));
                loanMaster.setEmi(24);
            } else if (salary > 50000) {
                loanMaster.setTenure(Math.round(appliedAmount/12.00));
                loanMaster.setEmi(36);
            }
            loanMaster.setSanctionedAmount(appliedAmount);
            loanMaster.setDisbursedAmount(appliedAmount);
            loanMaster.setLoanStatus("Approved");
            LoanMaster loanMasterSaved = loanRepo.save(loanMaster);

            if (loanMasterSaved.getAccNumber() != null) {
                loanApplicationMaster.setStatus("Approved");
                loanApplicationMaster.setStatusMessage("Your loan has been approved!");
            } else {
                loanApplicationMaster.setStatus("Rejected");
                loanApplicationMaster.setStatusMessage("Your loan application has been rejected");
            }

            loanApplicationMasterListSaved.add(loanApplicationMaster);
        }
        return new ResponseEntity<>(loanApplicationRepo.saveAll(loanApplicationMasterListSaved), HttpStatus.OK);
    }

    public LoanMaster getLoanById(Integer id) {
        return loanRepo.findById(id).orElse(new LoanMaster());
    }
}
