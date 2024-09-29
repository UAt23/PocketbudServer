package com.pocketbud.pocketbud.account;

import com.pocketbud.pocketbud.account.dto.AccountRequestDTO;
import com.pocketbud.pocketbud.account.dto.AccountResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id) {
        AccountResponseDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    // Create new account
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO requestDTO) {
        AccountResponseDTO createdAccount = accountService.createAccount(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    // Update account by ID
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO requestDTO
    ) {
        AccountResponseDTO updatedAccount = accountService.updateAccount(id, requestDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    // Delete account by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
