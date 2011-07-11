package hu.itkodex.commons.event;

import java.util.Set;

public interface ComponentEventPublisher<E extends ComponentEvent> {
   public void subscribeToEvent( ComponentEventSubscriber<E> subscriber, Class<E> eventClass );
   public Set<ComponentEventSubscriber<E>> getSubscribers();
}
