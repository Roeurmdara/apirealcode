package kh.edu.cstad.modilebankingaba.dto;

import java.math.BigDecimal;

public record UpdateAccount(

        BigDecimal balance,

        String customerName,
        String customerPhone
) {
}
