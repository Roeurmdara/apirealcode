package kh.edu.cstad.modilebankingaba.serivce.impl;

import jakarta.transaction.Transactional;
import kh.edu.cstad.modilebankingaba.domain.Account;
import kh.edu.cstad.modilebankingaba.domain.Customer;
import kh.edu.cstad.modilebankingaba.dto.CreateAccountRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseAccount;
import kh.edu.cstad.modilebankingaba.dto.UpdateAccount;
import kh.edu.cstad.modilebankingaba.mapper.AccountMapper;
import kh.edu.cstad.modilebankingaba.repository.AccountRepository;
import kh.edu.cstad.modilebankingaba.repository.CustomerRepository;
import kh.edu.cstad.modilebankingaba.serivce.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapper accountMapper;


    @Override
    public ResponseAccount getAccountByActNo(String actNo) {
        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return accountMapper.toResponse(account);
    }



    @Override
    public ResponseAccount createAccount(CreateAccountRequest createAccountRequest) {
        if (accountRepository.existsByActNo(createAccountRequest.actNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exists");
        }

        Customer customer = customerRepository.findById(createAccountRequest.customerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = accountMapper.fromCreateRequest(createAccountRequest);
        account.setCustomer(customer);
        account.setIsDeleted(false);

        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public List<ResponseAccount> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    public ResponseAccount updateAccount(String actNo, UpdateAccount updateAccountRequest) {
        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        accountMapper.toAccountPartially(updateAccountRequest, account);
        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public List<ResponseAccount> findByCustomerId(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        return accountRepository.findByCustomer(customer).stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteByActNo(String actNo) {
        if (!accountRepository.existsByActNo(actNo)) return false;
        accountRepository.deleteByActNo(actNo);
        return true;
    }

    @Override
    public boolean disableByActNo(String actNo) {
        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.setIsDeleted(true);
        accountRepository.save(account);
        return true;
    }
}
