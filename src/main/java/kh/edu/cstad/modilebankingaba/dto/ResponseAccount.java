package kh.edu.cstad.modilebankingaba.dto;

import java.math.BigDecimal;

public record ResponseAccount(
        BigDecimal balance,
        BigDecimal overLimit,
        String customerName,
        String customerPhone
) {
}
