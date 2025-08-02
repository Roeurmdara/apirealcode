package kh.edu.cstad.modilebankingaba.serivce;

import kh.edu.cstad.modilebankingaba.dto.CreateCustomerRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseCustomer;
import kh.edu.cstad.modilebankingaba.dto.UpdateCustomer;

import java.util.List;
import java.util.Optional;


public interface CustomerService {
    ResponseCustomer findByPhoneNumber(String phoneNumber);

    List<ResponseCustomer> findAllCustomers(Boolean isDelete);
//

    ResponseCustomer findByEmail(String email);

    //
    ResponseCustomer createCustomer(CreateCustomerRequest createCustomerRequest);

//

    boolean deleteByCustomerNumber(String customerNumber);

    //
    ResponseCustomer updateCustomer(String phoneNumber, UpdateCustomer updateCustomer);
}
