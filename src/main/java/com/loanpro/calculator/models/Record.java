package com.loanpro.calculator.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "record")
@SQLDelete(sql = "UPDATE record SET active = false WHERE id = ?")
@Where(clause = "active = true")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Operation operation;

    @ManyToOne
    private User user;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal userBalance;

    @Column
    private String operationResponse;

    @Column
    private Boolean active;

    @CreatedDate
    private LocalDateTime date;
}