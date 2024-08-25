package com.gpact.bank_app.service;

import com.gpact.bank_app.model.UserMaster;
import com.gpact.bank_app.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public Object createUser(UserMaster userMaster) {

        return userRepo.save(userMaster);
    }

    public Object createAdmin(List<UserMaster> userMasterList) {
        return userRepo.saveAll(userMasterList);
    }
}
