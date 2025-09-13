package org.defendev.hibernate.envers.demo.eaa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.event.spi.EnversListenerDuplicationStrategy;
import org.hibernate.envers.event.spi.EnversPostCollectionRecreateEventListenerImpl;
import org.hibernate.envers.event.spi.EnversPostDeleteEventListenerImpl;
import org.hibernate.envers.event.spi.EnversPostInsertEventListenerImpl;
import org.hibernate.envers.event.spi.EnversPreCollectionRemoveEventListenerImpl;
import org.hibernate.envers.event.spi.EnversPreCollectionUpdateEventListenerImpl;
import org.hibernate.envers.internal.tools.ReflectionTools;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;



public class EaaEnversIntegrator implements Integrator {

    private static final Logger log = LogManager.getLogger();

	public void integrate(Metadata metadata, BootstrapContext bootstrapContext,
                          SessionFactoryImplementor sessionFactory) {
		final ServiceRegistry serviceRegistry = sessionFactory.getServiceRegistry();
		final EnversService enversService = serviceRegistry.getService( EnversService.class );

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Opt-out of registration if EnversService is disabled
		if ( !enversService.isEnabled() ) {
			log.debug( "Skipping Envers listener registrations : EnversService disabled" );
			return;
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Verify that the EnversService is fully initialized and ready to go.
		if ( !enversService.isInitialized() ) {
			throw new HibernateException(
					"Expecting EnversService to have been initialized prior to call to EnversIntegrator#integrate"
			);
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Opt-out of registration if no audited entities found
		if ( !enversService.getEntitiesConfigurations().hasAuditedEntities() ) {
			log.debug( "Skipping Envers listener registrations : No audited entities found" );
			return;
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Do the registrations
		final EventListenerRegistry listenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );
		listenerRegistry.addDuplicationStrategy( EnversListenerDuplicationStrategy.INSTANCE );

		if ( enversService.getEntitiesConfigurations().hasAuditedEntities() ) {
			listenerRegistry.appendListeners(
                EventType.POST_DELETE,
                new EnversPostDeleteEventListenerImpl(enversService)
			);
			listenerRegistry.appendListeners(
                EventType.POST_INSERT,
                new EnversPostInsertEventListenerImpl(enversService)
			);
			listenerRegistry.appendListeners(
                EventType.PRE_UPDATE,
                new EaaEnversPreUpdateEventListenerImpl(enversService)
			);
			listenerRegistry.appendListeners(
                EventType.POST_UPDATE,
                new EaaEnversPostUpdateEventListenerImpl(enversService)
			);
			listenerRegistry.appendListeners(
                EventType.POST_COLLECTION_RECREATE,
                new EnversPostCollectionRecreateEventListenerImpl(enversService)
			);
			listenerRegistry.appendListeners(
                EventType.PRE_COLLECTION_REMOVE,
                new EnversPreCollectionRemoveEventListenerImpl(enversService)
			);
			listenerRegistry.appendListeners(
                EventType.PRE_COLLECTION_UPDATE,
                new EnversPreCollectionUpdateEventListenerImpl(enversService)
			);
		}
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		ReflectionTools.reset();
	}

}
