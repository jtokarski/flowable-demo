package org.defendev.hibernate.envers.demo.eaa.service;

import org.defendev.common.domain.command.Command;

import java.time.LocalDateTime;



public class UpdateFinancialTransactionCommand extends Command {

    private final long transactionId;

    private final boolean doUpdateMemo;

    private final String memo;

    private final boolean doUpdateTransactionDateTimeZulu;

    private final LocalDateTime transactionDateTimeZulu;

    public UpdateFinancialTransactionCommand(long transactionId, boolean doUpdateMemo, String memo, boolean doUpdateTransactionDateTimeZulu,
                                             LocalDateTime transactionDateTimeZulu) {
        this.transactionId = transactionId;
        this.doUpdateMemo = doUpdateMemo;
        this.memo = memo;
        this.doUpdateTransactionDateTimeZulu = doUpdateTransactionDateTimeZulu;
        this.transactionDateTimeZulu = transactionDateTimeZulu;
    }

    public long transactionId() {
        return transactionId;
    }

    public boolean doUpdateMemo() {
        return doUpdateMemo;
    }

    public LocalDateTime transactionDateTimeZulu() {
        return transactionDateTimeZulu;
    }

    public boolean doUpdateTransactionDateTimeZulu() {
        return doUpdateTransactionDateTimeZulu;
    }

    public String memo() {
        return memo;
    }
}
