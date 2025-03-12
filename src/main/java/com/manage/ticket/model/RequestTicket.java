package com.manage.ticket.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestTicket {
    private String passengerName;
    private LocalDate travelDate;
    private String sourceStation;
    private String destinationStation;
    private double price;
    private boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}