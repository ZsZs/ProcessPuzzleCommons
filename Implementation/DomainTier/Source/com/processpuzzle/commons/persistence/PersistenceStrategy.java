package com.processpuzzle.commons.persistence;


import java.util.List;

import com.processpuzzle.commons.persistence.query.Query;

public interface PersistenceStrategy {
   boolean configure();
   String getName();
   boolean isConfigured();
   void release();
   
   // Public accessors
   <P extends PersistentObject> P findById( UnitOfWork work, Class<P> entityClass, Integer id );
   <P extends PersistentObject> RepositoryResultSet<P> findAll( UnitOfWork work, Class<P> entityClass );
   RepositoryResultSet<? extends PersistentObject> findByQuery( UnitOfWork work, Query query );

   // Public mutators
   Integer add( UnitOfWork work, Class<? extends PersistentObject> entityClass, PersistentObject entity );
   void update( UnitOfWork work, Class<? extends PersistentObject> entityClass, PersistentObject entitiy );
   void delete( UnitOfWork work, Class<? extends PersistentObject> entityClass, PersistentObject entity );

   public RepositoryEventHandler getEventHandler( String eventHandlerName );
   public List<RepositoryEventHandler> getEventHandlers();   
}
