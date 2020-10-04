package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.model.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/create/{id}")
    public RequestDto createRequest(@PathVariable("id") Long bookId, @RequestHeader String requesterData) {
        return new RequestDto(requestService.createRequest(bookId, requesterData));
    }

    @GetMapping("/allRequestsByBookTitle")
    public List<RequestDto> findSortedAllRequestsByBookTitle() {
        return requestService.findSortedAllRequestsByBookTitle().stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/allRequestsByIsActive")
    public List<RequestDto> findSortedAllRequestsByIsActive() {
        return requestService.findSortedAllRequestsByIsActive().stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/allRequestsByRequesterData")
    public List<RequestDto> findSortedAllRequestsByRequesterData() {
        return requestService.findSortedAllRequestsByRequesterData().stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/exportAll")
    public List<RequestDto> exportAllRequests(@RequestHeader String fileName) {
        return requestService.exportAllRequests(fileName).stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/export/{id}")
    public RequestDto exportRequest(@PathVariable("id") Long requestId, @RequestHeader String fileName) {
        return new RequestDto(requestService.exportRequest(requestId, fileName));
    }

    @PutMapping("/import")
    public List<RequestDto> importRequests(@RequestHeader String fileName) {
        return requestService.importRequests(fileName).stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }
}
