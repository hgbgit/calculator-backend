package com.loanpro.calculator.repository;

import com.loanpro.calculator.common.EOperation;
import com.loanpro.calculator.models.Record;
import com.loanpro.calculator.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {


    Optional<Record> findByIdAndUser(Long id, User user);

    Page<Record> findAllByUser(User user, Pageable pageable);

    Page<Record> findAllByUserAndOperationType(User user, EOperation operation, Pageable pageable);
}
