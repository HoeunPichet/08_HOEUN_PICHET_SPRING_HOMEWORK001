package com.manage.ticket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponseTicket<T> {
    private boolean success;
    private String message;
    private HttpStatus status;
    private T payload;
    private LocalDateTime timestamp = LocalDateTime.now();
}