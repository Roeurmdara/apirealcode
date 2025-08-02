package kh.edu.cstad.modilebankingaba.mapper;


import kh.edu.cstad.modilebankingaba.domain.Customer;
import kh.edu.cstad.modilebankingaba.dto.CreateCustomerRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseCustomer;
import kh.edu.cstad.modilebankingaba.dto.UpdateCustomer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(
            UpdateCustomer updateCustomer,
            @MappingTarget Customer customer
    );

    ResponseCustomer mapResponseCustomer(Customer customer);

    Customer frmCreateCustomer(CreateCustomerRequest request);
}
