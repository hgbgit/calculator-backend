package com.loanpro.calculator.controllers;

import com.loanpro.calculator.payload.response.RecordResponse;
import com.loanpro.calculator.services.RecordsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(origins = "*", maxAge = 3600, methods = {GET, HEAD, POST, DELETE, OPTIONS})
@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordsController {

    private final RecordsService recordsService;

    private static final Logger logger = LoggerFactory.getLogger(RecordsController.class);


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public Page<RecordResponse> listRecords(Pageable pageable, @RequestParam(required = false) String operation) {
        logger.info("List records request: {}, with operation filter {}", pageable, operation);
        return recordsService.listRecords(pageable, operation);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/{id}")
    public void deleteRecord(@PathVariable("id") Long id) {
        logger.info("Delete record request for record id: {}", id);
        recordsService.deleteRecord(id);
    }

}
