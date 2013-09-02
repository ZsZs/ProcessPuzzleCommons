package com.processpuzzle.commons.persistence;

import com.processpuzzle.commons.persistence.query.IdentityExpression;
import com.processpuzzle.commons.persistence.query.Query;

public interface Repository<E extends AggregateRoot> {
   RepositoryResultSet<E> findAll( UnitOfWork work );
   E findById( UnitOfWork work, Integer id );
   E findByIdentityExpression( UnitOfWork work, IdentityExpression<?> identity );
   RepositoryResultSet<E> findByQuery( UnitOfWork work, Query query );
   
   Integer add( UnitOfWork work, E entity );
   void update( UnitOfWork work, E entity );
   void delete( UnitOfWork work, E entity );
   
   PersistenceStrategy getPersistenceStrategy();
   Class<? extends AggregateRoot> getSupportedAggregateRootClass();
}
