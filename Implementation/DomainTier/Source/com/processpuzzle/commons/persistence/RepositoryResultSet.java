package com.processpuzzle.commons.persistence;


import java.util.Iterator;
import java.util.List;

import com.processpuzzle.commons.persistence.PersistentObject;

public interface RepositoryResultSet<P extends PersistentObject> extends Iterable<P> {
   public P getEntityAt( int index );
   public List<P> getEntitiesAt( int start, int maxCount );
   public Iterator<P> iterator();
   public boolean isEmpty();
   public int size();
   public P[] toArray();
}
