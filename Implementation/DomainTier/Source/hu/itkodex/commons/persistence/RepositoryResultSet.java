package hu.itkodex.commons.persistence;

import hu.itkodex.commons.persistence.PersistentObject;

import java.util.Iterator;
import java.util.List;

public interface RepositoryResultSet<P extends PersistentObject> extends Iterable<P> {
   public P getEntityAt( int index );
   public List<P> getEntitiesAt( int start, int maxCount );
   public Iterator<P> iterator();
   public boolean isEmpty();
   public int size();
   public P[] toArray();
}
