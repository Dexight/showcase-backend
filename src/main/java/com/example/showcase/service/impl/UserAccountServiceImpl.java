package com.example.showcase.service.impl;

import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.UserAccount;
import com.example.showcase.repository.UserAccountRepository;
import com.example.showcase.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private UserAccountRepository userAccountRepository;

    @Override
    public UserAccount createUserAccount(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount getUserAccountById(int userAccountId) {
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found"));
        return userAccount;
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.findAll();
    }

    @Override
    public UserAccount updateUserAccount(int userAccountId, UserAccount updateUserAccount) {
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found"));
        userAccount.setFavouriteProjects(updateUserAccount.getFavouriteProjects());
        return userAccountRepository.save(userAccount);
    }

    @Override
    public void deleteUserAccount(int userAccountId) {
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found"));
        userAccountRepository.delete(userAccount);
    }
}
