package kh.edu.cstad.modilebankingaba.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(
        String actNo,
        BigDecimal balance,
        BigDecimal overLimit,
        Integer customerId,
        Integer accountTypeId
) {
}
