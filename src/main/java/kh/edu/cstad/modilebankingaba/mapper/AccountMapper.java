package kh.edu.cstad.modilebankingaba.mapper;

import kh.edu.cstad.modilebankingaba.domain.Account;
import kh.edu.cstad.modilebankingaba.dto.CreateAccountRequest;
import kh.edu.cstad.modilebankingaba.dto.ResponseAccount;
import kh.edu.cstad.modilebankingaba.dto.UpdateAccount;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actNo", ignore = true)
    @Mapping(target = "overLimit", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    void toAccountPartially(UpdateAccount updateAccount, @MappingTarget Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    Account fromCreateRequest(CreateAccountRequest request);

    @Mapping(source = "customer.userName", target = "customerName")
    @Mapping(source = "customer.phoneNumber", target = "customerPhone")
    ResponseAccount toResponse(Account account);
}
