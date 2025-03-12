package com.manage.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private int totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}