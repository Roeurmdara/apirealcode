package kh.edu.cstad.modilebankingaba.exception;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ErrorResponse<T>(
        String message,
        Integer code,
        LocalDateTime localDateTime,
        T details
) {
}
