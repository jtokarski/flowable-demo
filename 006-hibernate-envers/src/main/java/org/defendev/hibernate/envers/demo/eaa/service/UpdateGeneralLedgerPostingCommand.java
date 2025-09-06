package org.defendev.hibernate.envers.demo.eaa.service;

import java.math.BigDecimal;



public class UpdateGeneralLedgerPostingCommand {

    private final long postingId;

    private final boolean doUpdateAmount;

    private final BigDecimal amount;

    public UpdateGeneralLedgerPostingCommand(long postingId, boolean doUpdateAmount, BigDecimal amount) {
        this.postingId = postingId;
        this.doUpdateAmount = doUpdateAmount;
        this.amount = amount;
    }

    public long postingId() {
        return postingId;
    }

    public boolean doUpdateAmount() {
        return doUpdateAmount;
    }

    public BigDecimal amount() {
        return amount;
    }
}
