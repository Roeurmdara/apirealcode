package kh.edu.cstad.modilebankingaba.dto;

import lombok.Builder;

@Builder
public record ResponseCustomer(
        String userName,
        String phoneNumber,
        String gender,
        String email
) {
}
