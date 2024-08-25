package com.gpact.bank_app.util;

import com.gpact.bank_app.model.AccountMaster;
import com.gpact.bank_app.model.UserMaster;
import com.gpact.bank_app.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankAppUtil {

    @Autowired
    UserRepo userRepo;

    public AccountMaster getAccountByUserName(String username) {
        UserMaster user = userRepo.findByUsername(username).orElse(null);
        if (user != null) {
            return user.getAccounts().stream()
                    .findFirst()
                    .orElse(null);
        }else{
            return null;
        }
    }

}
