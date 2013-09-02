package com.processpuzzle.commons.persistence;

public interface UnitOfWork {
   void discard();
   void finish();
   boolean isStarted();
   void registerPersistenceProvicer( PersistenceProvider provider );
   void start();
}
