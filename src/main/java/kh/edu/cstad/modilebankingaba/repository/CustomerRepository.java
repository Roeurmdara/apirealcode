package kh.edu.cstad.modilebankingaba.repository;

import kh.edu.cstad.modilebankingaba.domain.Customer;
import kh.edu.cstad.modilebankingaba.dto.ResponseCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
