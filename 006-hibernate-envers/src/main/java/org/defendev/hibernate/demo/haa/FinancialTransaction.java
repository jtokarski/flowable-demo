package org.defendev.hibernate.demo.haa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;



@Table(name = "FinancialTransaction")
@Entity
public class FinancialTransaction {

    public enum LifecycleStatus {
        NEW, APPROVED, POSTED;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "recordedBy")
    private String recordedBy;

    @Column(name = "memo")
    private String memo;

    @Column(name = "transactionDateTimeZulu")
    private LocalDateTime transactionDateTimeZulu;

    @Column(name = "lifecycleStatus")
    @Enumerated(EnumType.STRING)
    private LifecycleStatus lifecycleStatus;


}
