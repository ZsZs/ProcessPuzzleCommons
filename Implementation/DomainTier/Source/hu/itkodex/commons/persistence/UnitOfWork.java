package hu.itkodex.commons.persistence;

public interface UnitOfWork {
   void discard();
   void finish();
   boolean isStarted();
   void registerPersistenceProvicer( PersistenceProvider provider );
   void start();
}
