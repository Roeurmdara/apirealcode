package kh.edu.cstad.modilebankingaba.repository;

import kh.edu.cstad.modilebankingaba.domain.Account;
import kh.edu.cstad.modilebankingaba.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByActNo(String actNo);

    boolean existsByActNo(String actNo);

    void deleteByActNo(String actNo);

    List<Account> findByCustomer(Customer customer);
}
