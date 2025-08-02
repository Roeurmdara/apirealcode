package kh.edu.cstad.modilebankingaba.serivce.impl;

import kh.edu.cstad.modilebankingaba.domain.Customer;
import kh.edu.cstad.modilebankingaba.dto.CreateCustomerRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseCustomer;
import kh.edu.cstad.modilebankingaba.dto.UpdateCustomer;
import kh.edu.cstad.modilebankingaba.mapper.CustomerMapper;
import kh.edu.cstad.modilebankingaba.repository.CustomerRepository;
import kh.edu.cstad.modilebankingaba.serivce.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public ResponseCustomer findByEmail(String email) {
        return customerRepository.findByEmail(email).map(customerMapper::mapResponseCustomer)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Email Not Found")
                );
    }


    public ResponseCustomer findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).map(customerMapper::mapResponseCustomer).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Phone number not found"
                )
        );
    }

    @Override
    public List<ResponseCustomer> findAllCustomers(Boolean isDelete) {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .filter(customer -> customer.getIsDeleted() == isDelete)
                .map(customerMapper::mapResponseCustomer).toList();
    }

    /***
     *
     * @param createCustomerRequest
     * @return
     */

    @Override
    public ResponseCustomer createCustomer(CreateCustomerRequest createCustomerRequest) {

        if (customerRepository.existsByEmail(createCustomerRequest.email()))
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Customer already exists with email: " + createCustomerRequest.email()
            );

        if (customerRepository.existsByPhoneNumber(createCustomerRequest.phoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Customer already exists with phone number: " + createCustomerRequest.phoneNumber()
            );
        }

        Customer customer = customerMapper.frmCreateCustomer(createCustomerRequest);
        customer.setIsDeleted(false);
        customer = customerRepository.save(customer);

        return customerMapper.mapResponseCustomer(customer);
    }


    public ResponseCustomer updateCustomer(String phoneNumber, UpdateCustomer updateCustomer) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"
                ));


        customerMapper.updateCustomer(
                updateCustomer,
                customer
                );
        customer = customerRepository.save(customer);
        return customerMapper.mapResponseCustomer(customer);
    }

    //Delete
    @Override
    public boolean deleteByCustomerNumber(String customerNumber) {
        Optional<Customer> customer = customerRepository.findByPhoneNumber(customerNumber);
        if (customer.isPresent()) {
            Customer customerToDelete = customer.get();
            customer.get().setIsDeleted(true);
            customerRepository.save(customerToDelete);
            return true;
        }
        return false;
    }
}
