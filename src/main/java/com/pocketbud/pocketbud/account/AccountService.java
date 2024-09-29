package com.pocketbud.pocketbud.account;

import com.pocketbud.pocketbud.account.dto.AccountRequestDTO;
import com.pocketbud.pocketbud.account.dto.AccountResponseDTO;
import com.pocketbud.pocketbud.model.User;
import com.pocketbud.pocketbud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));
        return mapToDTO(account);
    }

    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Account account = Account.builder()
                .name(requestDTO.getName())
                .balance(requestDTO.getBalance())
                .user(user)
                .description(requestDTO.getDescription())
                .build();

        account = accountRepository.save(account);
        return mapToDTO(account);
    }

    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO requestDTO) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));

        account.setName(requestDTO.getName());
        account.setBalance(requestDTO.getBalance());
        account.setDescription(requestDTO.getDescription());

        account = accountRepository.save(account);
        return mapToDTO(account);
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    private AccountResponseDTO mapToDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .balance(account.getBalance())
                .description(account.getDescription())
                .userId(account.getUser().getId())
                .build();
    }
}
