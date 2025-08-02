package kh.edu.cstad.modilebankingaba.controller;


import kh.edu.cstad.modilebankingaba.dto.CreateCustomerRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseCustomer;
import kh.edu.cstad.modilebankingaba.serivce.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
private final CustomerService customerService;
//
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseCustomer createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

//
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ResponseCustomer> findAllCustomers(
            @RequestParam(defaultValue = "false") Boolean isDeleted
    )
    {
        return customerService.findAllCustomers(isDeleted);
    }
// find by phone number
    @GetMapping("/{phoneNumber}")
    public ResponseCustomer findCustomer(@PathVariable String phoneNumber) {
        return customerService.findByPhoneNumber(phoneNumber);
    }
//    find by email
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/{email}")
    public ResponseCustomer findByEmail(@PathVariable  String email) {
        return customerService.findByEmail(email);
    }

   @DeleteMapping("/{number}")
    public boolean deleteCustomer(@PathVariable String number) {
        return customerService.deleteByCustomerNumber(number);
   }
}
