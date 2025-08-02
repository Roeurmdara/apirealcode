package kh.edu.cstad.modilebankingaba.serivce;

import kh.edu.cstad.modilebankingaba.dto.CreateAccountRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseAccount;
import kh.edu.cstad.modilebankingaba.dto.UpdateAccount;

import java.util.List;

public interface AccountService {

    ResponseAccount createAccount(CreateAccountRequest createAccountRequest);

    List<ResponseAccount> getAllAccounts();

    ResponseAccount updateAccount(String actNO, UpdateAccount updateAccountRequest);

    List<ResponseAccount> findByCustomerId(Integer customerId);

    ResponseAccount getAccountByActNo(String actNo);

    boolean deleteByActNo(String actNo);

    boolean disableByActNo(String actNo);

}
