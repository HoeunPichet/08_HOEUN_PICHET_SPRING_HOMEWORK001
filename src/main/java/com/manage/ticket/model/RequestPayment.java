package com.manage.ticket.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestPayment {
    private List<Integer> ticketIds;
    private boolean paymentStatus;
}