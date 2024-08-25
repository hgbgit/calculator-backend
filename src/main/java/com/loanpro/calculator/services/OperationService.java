package com.loanpro.calculator.services;

import com.loanpro.calculator.exception.InsufficientBalanceException;
import com.loanpro.calculator.exception.OperationNotSupportedException;
import com.loanpro.calculator.models.Operation;
import com.loanpro.calculator.models.Record;
import com.loanpro.calculator.models.User;
import com.loanpro.calculator.payload.request.CalculationRequest;
import com.loanpro.calculator.repository.OperationRepository;
import com.loanpro.calculator.repository.RecordRepository;
import com.loanpro.calculator.repository.UserRepository;
import com.loanpro.calculator.security.services.UserDetailsServiceImpl;
import com.loanpro.calculator.services.strategy.OperationStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final List<OperationStrategy> operationStrategies;

    private final UserDetailsServiceImpl userDetailsService;

    private final OperationRepository operationRepository;

    private final RecordRepository recordRepository;

    private final UserRepository userRepository;

    @Transactional(Transactional.TxType.REQUIRED)
    public String calculate(CalculationRequest calculationRequest) {
        User user = userDetailsService.getCurrentUser();
        Operation operation = operationRepository
                .findByType(calculationRequest.operation()).orElseThrow(
                        () -> new OperationNotSupportedException(calculationRequest.operation().toString()));

        validateUserBalance(user, operation);

        Object result = operationStrategies.stream()
                .filter(op -> op.canHandle(calculationRequest))
                .map(op -> op.handle(calculationRequest))
                .findFirst().orElseThrow(() -> new OperationNotSupportedException("Unable to perform operation"));

        Record record = buildRecord(user, operation, result);

        recordRepository.save(record);
        updateUserBalance(user, operation);

        return result.toString();
    }

    private synchronized void validateUserBalance(User user, Operation operation) {
        if (operation.getCost().compareTo(user.getUserBalance()) > 0) {
            throw new InsufficientBalanceException(user.getUserBalance(), operation.getCost());
        }
    }

    private synchronized void updateUserBalance(User user, Operation operation) {
        user.setUserBalance(user.getUserBalance().subtract(operation.getCost()));
        userRepository.save(user);
    }

    private Record buildRecord(User user, Operation operation, Object result) {
        Record record = new Record();
        record.setOperation(operation);
        record.setUser(user);
        record.setAmount(operation.getCost());
        record.setUserBalance(user.getUserBalance());
        record.setOperationResponse(result.toString());
        record.setActive(true);
        record.setDate(LocalDateTime.now());
        return record;
    }
}
