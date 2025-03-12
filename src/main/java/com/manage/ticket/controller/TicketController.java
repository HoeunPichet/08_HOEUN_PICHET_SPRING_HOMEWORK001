package com.manage.ticket.controller;

import com.manage.ticket.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Ticket Management")
public class TicketController {
    List<Ticket> tickets = new ArrayList<>();
    LocalDate now = LocalDate.now();
    private int autoId = 1;

    public TicketController() {
        tickets.add(new Ticket(1, "Chanda", now, "Station A", "Station B", 7.5, true, TicketStatus.BOOKED, "A1"));
        tickets.add(new Ticket(2, "Chanda", now, "Station D", "Station B", 5.5, true, TicketStatus.CANCELLED, "A2"));
        tickets.add(new Ticket(3, "Nita", now, "Station A", "Station C", 4.5, true, TicketStatus.BOOKED, "A5"));
        tickets.add(new Ticket(4, "Roth", now, "Station B", "Station C", 8.5, true, TicketStatus.CANCELLED, "A32"));
        tickets.add(new Ticket(5, "Hanna", now, "Station C", "Station B", 5.5, true, TicketStatus.COMPLETED, "A21"));
        autoId += tickets.size();
    }

    @GetMapping
    @Operation(summary = "Get all tickets")
    public ResponseEntity<APIResponseTicket<PagedListTicket>> getTickets(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        size = size == null ? 10 : size;
        int totalPage = tickets.size() / size + (tickets.size() % size > 0 ? 1 : 0);
        page = page == null ? 1 : page;

        List<Ticket> lists = new ArrayList<>();
        if (!tickets.isEmpty()) {
            for (int i = 0; i < tickets.size(); i++) {
                if (i >= (page * size - size) && i < (page * size) && i <= (tickets.size() - 1)) {
                    lists.add(tickets.get(i));
                }
            }
        }

        APIResponseTicket<PagedListTicket> response = new APIResponseTicket<>();
        response.setSuccess(true);
        response.setMessage("All tickets retrieved successfully.");
        response.setStatus(HttpStatus.OK);
        response.setPayload(new PagedListTicket());

        response.getPayload().setItems(lists);
        response.getPayload().setPagination(new Pagination(tickets.size(), page, size, totalPage));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Bulk update payment status for multiple tickets")
    public ResponseEntity<APIResponseTicket<List<Ticket>>> updatePaymentStatus(@RequestBody RequestPayment paymentRequest) {
        List<Ticket> lists = new ArrayList<>();
        if (!tickets.isEmpty()) {
            if (!paymentRequest.getTicketIds().isEmpty()) {
                for (Ticket ticket : tickets) {
                    for (Integer ticketId : paymentRequest.getTicketIds()) {
                        if (ticket.getTicketId() == ticketId) {
                            ticket.setPaymentStatus(paymentRequest.isPaymentStatus());
                            lists.add(ticket);
                        }
                    }
                }
            }
        }

        APIResponseTicket<List<Ticket>> response = new APIResponseTicket<>();
        response.setSuccess(true);
        response.setMessage("Payment status updated successfully.");
        response.setStatus(HttpStatus.OK);
        response.setPayload(lists);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<APIResponseTicket<Ticket>> createTicket(@RequestBody RequestTicket ticket) {
        APIResponseTicket<Ticket> response = new APIResponseTicket<>();
        if (ticket.getPrice() > 0) {
            Ticket addTicket = new Ticket(autoId, ticket.getPassengerName(), ticket.getTravelDate(), ticket.getSourceStation(), ticket.getDestinationStation(), ticket.getPrice(), ticket.isPaymentStatus(), ticket.getTicketStatus(), ticket.getSeatNumber());
            tickets.add(addTicket);
            autoId += 1;

            response.setSuccess(true);
            response.setMessage("Ticket created successfully.");
            response.setStatus(HttpStatus.CREATED);
            response.setPayload(addTicket);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Price must be than 0!");
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{ticket-id}")
    @Operation(summary = "Get a ticket by ID")
    public ResponseEntity<APIResponseTicket<Ticket>> getTicketById(@PathVariable("ticket-id") Integer ticketId) {
        APIResponseTicket<Ticket> response = new APIResponseTicket<>();
        Ticket getTicket = tickets.stream().filter(ticket -> ticket.getTicketId() == ticketId).findFirst().orElse(null);

        if (getTicket != null) {
            response.setSuccess(true);
            response.setMessage("Ticket retrieved successfully.");
            response.setStatus(HttpStatus.OK);
            response.setPayload(getTicket);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("No ticket found with ID: " + ticketId);
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{ticket-id}")
    @Operation(summary = "Update an existing ticket by ID")
    public ResponseEntity<APIResponseTicket<Ticket>> updateTicketById(@RequestBody RequestTicket ticket, @PathVariable("ticket-id") Integer ticketId) {
        APIResponseTicket<Ticket> response = new APIResponseTicket<>();

        Ticket getTicket = null;
        for (Ticket tic : tickets) {
            if (tic.getTicketId() == ticketId) {
                tic.setPassengerName(ticket.getPassengerName());
                tic.setTravelDate(ticket.getTravelDate());
                tic.setSourceStation(ticket.getSourceStation());
                tic.setDestinationStation(ticket.getDestinationStation());
                tic.setPrice(ticket.getPrice());
                tic.setPaymentStatus(ticket.isPaymentStatus());
                tic.setTicketStatus(ticket.getTicketStatus());
                tic.setSeatNumber(ticket.getSeatNumber());
                getTicket = tic;
            }
        }

        if (getTicket != null) {
            if (ticket.getPrice() > 0) {
                response.setSuccess(true);
                response.setMessage("Ticket updated successfully.");
                response.setStatus(HttpStatus.OK);
                response.setPayload(getTicket);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMessage("Price must be than 0!");
                response.setStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        response.setMessage("No ticket found with ID: " + ticketId);
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{ticket-id}")
    @Operation(summary = "Delete an ticket by ID")
    public ResponseEntity<APIResponseTicket<Ticket>> deleteTicketById(@PathVariable("ticket-id") Integer ticketId) {
        APIResponseTicket<Ticket> response = new APIResponseTicket<>();

        Ticket ticket = tickets.stream().filter(tic -> tic.getTicketId() == ticketId).findFirst().orElse(null);
        boolean isDelete = tickets.removeIf(tic -> tic.getTicketId() == ticketId);

        if (isDelete) {
            response.setSuccess(true);
            response.setMessage("Ticket deleted successfully.");
            response.setStatus(HttpStatus.OK);
            response.setPayload(ticket);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("No ticket found with ID: " + ticketId);
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create tickets")
    public ResponseEntity<APIResponseTicket<List<Ticket>>> bulkCreateTickets(@RequestBody List<RequestTicket> ticketList) {
        APIResponseTicket<List<Ticket>> response = new APIResponseTicket<>();
        boolean isPrice = ticketList.stream().allMatch(tic -> tic.getPrice() > 0);
        if (isPrice) {
            List<Ticket> lists = new ArrayList<>();
            for (RequestTicket ticket : ticketList) {
                Ticket addTicket = new Ticket(autoId, ticket.getPassengerName(), ticket.getTravelDate(), ticket.getSourceStation(), ticket.getDestinationStation(), ticket.getPrice(), ticket.isPaymentStatus(), ticket.getTicketStatus(), ticket.getSeatNumber());
                autoId += 1;
                tickets.add(addTicket);
                lists.add(addTicket);
            }
            response.setSuccess(true);
            response.setMessage("Bulk tickets created successfully.");
            response.setStatus(HttpStatus.CREATED);
            response.setPayload(lists);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("All prices must be than 0!");
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search tickets by passenger name")
    public ResponseEntity<APIResponseTicket<List<Ticket>>> searchPassengerName(@RequestParam String passengerName) {
        List<Ticket> ticketList = tickets.stream().filter(tic -> tic.getPassengerName().equals(passengerName)).toList();

        APIResponseTicket<List<Ticket>> response = new APIResponseTicket<>();
        response.setSuccess(true);
        response.setMessage("Tickets searched successfully.");
        response.setStatus(HttpStatus.OK);
        response.setPayload(ticketList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter tickets by status and travel date")
    public ResponseEntity<APIResponseTicket<List<Ticket>>> filterTickets(@RequestParam TicketStatus ticketStatus, @RequestParam LocalDate travelTime) {
        List<Ticket> ticketList = tickets.stream().filter(tic -> (tic.getTicketStatus().equals(ticketStatus)) && tic.getTravelDate().equals(travelTime)).toList();

        APIResponseTicket<List<Ticket>> response = new APIResponseTicket<>();
        response.setSuccess(true);
        response.setMessage("Tickets filtered successfully.");
        response.setStatus(HttpStatus.OK);
        response.setPayload(ticketList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}