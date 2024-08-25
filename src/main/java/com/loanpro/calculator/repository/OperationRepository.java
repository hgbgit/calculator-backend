package com.loanpro.calculator.repository;

import com.loanpro.calculator.common.EOperation;
import com.loanpro.calculator.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByType(EOperation type);
}
