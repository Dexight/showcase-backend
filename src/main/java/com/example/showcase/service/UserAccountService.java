package com.example.showcase.service;

import com.example.showcase.entity.UserAccount;

import java.util.List;

public interface UserAccountService {
    UserAccount createUserAccount(UserAccount userAccount);

    UserAccount getUserAccountById(int userId);

    List<UserAccount> getAllUserAccounts();

    UserAccount updateUserAccount(int userId, UserAccount updateUserAccount);

    void deleteUserAccount(int userId);
}
