package org.defendev.hibernate.envers.demo.eaa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversPreUpdateEventListenerImpl;
import org.hibernate.event.spi.PreUpdateEvent;

import static org.defendev.hibernate.envers.demo.eaa.EaaConditionalAuditUtil.notMarkedForAudit;



public class EaaEnversPreUpdateEventListenerImpl extends EnversPreUpdateEventListenerImpl {

    private static final Logger log = LogManager.getLogger();

    public EaaEnversPreUpdateEventListenerImpl(EnversService enversService) {
        super(enversService);
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        final Object[] oldState = event.getOldState();
        final Object[] newState = event.getState();
        final String[] propertyNames = event.getPersister().getPropertyNames();

        final boolean notMarkedForAudit = notMarkedForAudit(oldState, newState, propertyNames, event.getEntity());

        if (notMarkedForAudit) {
            return false;
        }
        return super.onPreUpdate(event);
    }

}
