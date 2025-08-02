package kh.edu.cstad.modilebankingaba.dto;

public record UpdateCustomer(
        String userName,
        String phoneNumber,
        String gender,
        String email
) {
}
