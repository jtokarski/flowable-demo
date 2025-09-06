package org.defendev.hibernate.envers.demo.eaa.service;

import jakarta.persistence.EntityManager;
import org.defendev.hibernate.envers.demo.eaa.model.GeneralLedgerPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UpdateGeneralLedgerPostingService {

    private final EntityManager em;

    @Autowired
    public UpdateGeneralLedgerPostingService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void execute(UpdateGeneralLedgerPostingCommand command) {
        final long postingId = command.postingId();
        final GeneralLedgerPosting posting = em.find(GeneralLedgerPosting.class, postingId);
        if (command.doUpdateAmount()) {
            posting.setAmount(command.amount());
        }
    }

}
