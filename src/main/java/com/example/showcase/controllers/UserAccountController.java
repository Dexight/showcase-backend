package com.example.showcase.controllers;

import com.example.showcase.entity.UserAccount;
import com.example.showcase.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user_accounts")
public class UserAccountController {
    private UserAccountService userAccountService;

    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount userAccount) {
        UserAccount savedUserAccount = userAccountService.createUserAccount(userAccount);
        return new ResponseEntity<>(savedUserAccount, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserAccount> getUserAccountById(@PathVariable("id") int userAccountId) {
        UserAccount userAccount = userAccountService.getUserAccountById(userAccountId);
        return ResponseEntity.ok(userAccount);
    }

    @GetMapping
    public ResponseEntity<List<UserAccount>> getAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountService.getAllUserAccounts();
        return ResponseEntity.ok(userAccounts);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable("id") int userAccountId, @RequestBody UserAccount updateuserAccount) {
        UserAccount userAccount = userAccountService.updateUserAccount(userAccountId, updateuserAccount);
        return ResponseEntity.ok(userAccount);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable("id") int userAccountId) {
        userAccountService.deleteUserAccount(userAccountId);
        return ResponseEntity.ok("UserAccount is deleted");
    }
}
