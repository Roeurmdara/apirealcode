package kh.edu.cstad.modilebankingaba.dto;

import jakarta.persistence.Column;
import lombok.Builder;

@Builder
public record CreateCustomerRequest(
        String userName,
        String phoneNumber,
        String gender,
        String email

) {
}
