package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.model.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/create/{id}")
    public RequestDto createRequest(@PathVariable("id") Long bookId, @RequestParam String requesterData) {
        return requestService.createRequest(bookId, requesterData);
    }

    @GetMapping("/allRequestsByBookTitle")
    public List<RequestDto> findSortedAllRequestsByBookTitle() {
        return requestService.findSortedAllRequestsByBookTitle();
    }

    @GetMapping("/allRequestsByState")
    public List<RequestDto> findSortedAllRequestsByIsActive() {
        return requestService.findSortedAllRequestsByIsActive();
    }

    @GetMapping("/allRequestsByRequesterData")
    public List<RequestDto> findSortedAllRequestsByRequesterData() {
        return requestService.findSortedAllRequestsByRequesterData();
    }

    @GetMapping("/exportAll")
    public List<RequestDto> exportAllRequests(@RequestParam String fileName) {
        return requestService.exportAllRequests(fileName);
    }

    @GetMapping("/export/{id}")
    public RequestDto exportRequest(@PathVariable("id") Long requestId, @RequestParam String fileName) {
        return requestService.exportRequest(requestId, fileName);
    }

    @PostMapping("/import")
    public List<RequestDto> importRequests(@RequestParam String fileName) {
        return requestService.importRequests(fileName);
    }
}
