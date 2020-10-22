package com.senla.training.yeutukhovich.bookstore.model.service.request;

import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long bookId, String requesterData);

    List<RequestDto> findSortedAllRequests(String sortParam);

    List<RequestDto> exportAllRequests(String fileName);

    RequestDto exportRequest(Long requestId, String fileName);

    List<RequestDto> importRequests(String fileName);
}
