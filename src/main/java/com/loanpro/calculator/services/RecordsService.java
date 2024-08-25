package com.loanpro.calculator.services;

import com.loanpro.calculator.common.EOperation;
import com.loanpro.calculator.exception.RecordNotFoundException;
import com.loanpro.calculator.models.Record;
import com.loanpro.calculator.models.User;
import com.loanpro.calculator.payload.response.RecordResponse;
import com.loanpro.calculator.repository.RecordRepository;
import com.loanpro.calculator.security.services.UserDetailsServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordsService {

    private final UserDetailsServiceImpl userDetailsService;

    private final RecordRepository recordRepository;

    private static final Logger logger = LoggerFactory.getLogger(RecordsService.class);


    @Transactional(Transactional.TxType.REQUIRED)
    public Page<RecordResponse> listRecords(Pageable pageable, String operation) {
        User user = userDetailsService.getCurrentUser();
        Page<Record> recordPage;

        if (operation != null && !operation.isBlank()) {
            recordPage = recordRepository.findAllByUserAndOperationType(user, EOperation.valueOf(operation), pageable);
        } else {
            recordPage = recordRepository.findAllByUser(user, pageable);
        }

        logger.info("Total of {} records found, answering for current page {} a total of {} records.",
                recordPage.getTotalElements(),
                recordPage.getPageable().getPageNumber(),
                recordPage.getContent().size());

        return recordPage.map(record -> new RecordResponse(record.getId(), record.getOperation(),
                record.getAmount(), record.getUserBalance(),
                record.getOperationResponse(), record.getDate()));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteRecord(Long id) {
        User user = userDetailsService.getCurrentUser();

        Record record = recordRepository.findByIdAndUser(id, user).orElseThrow(() -> new RecordNotFoundException(id));
        recordRepository.delete(record);
    }
}