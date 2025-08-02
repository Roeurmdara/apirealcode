package kh.edu.cstad.modilebankingaba.controller;

import kh.edu.cstad.modilebankingaba.dto.CreateAccountRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseAccount;
import kh.edu.cstad.modilebankingaba.dto.UpdateAccount;
import kh.edu.cstad.modilebankingaba.serivce.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseAccount createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }


    @GetMapping
    public List<ResponseAccount> getAllAccounts() {
        return accountService.getAllAccounts();
    }


    @GetMapping("/{actNo}")
    public ResponseAccount getAccountByActNo(@PathVariable String actNo) {

        return accountService.getAccountByActNo(actNo);
    }

    @GetMapping("/customer/{customerId}")
    public List<ResponseAccount> getAccountsByCustomer(@PathVariable Integer customerId) {
        return accountService.findByCustomerId(customerId);
    }


    @PutMapping("/{actNo}")
    public ResponseAccount updateAccount(
            @PathVariable String actNo,
            @RequestBody UpdateAccount updateRequest
    ) {
        return accountService.updateAccount(actNo, updateRequest);
    }


    @DeleteMapping("/{actNo}")
    public boolean deleteAccount(@PathVariable String actNo) {
        return accountService.deleteByActNo(actNo);
    }

    @PatchMapping("/{actNo}/disable")
    public boolean disableAccount(@PathVariable String actNo) {
        return accountService.disableByActNo(actNo);
    }
}
