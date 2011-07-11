package hu.itkodex.commons.event;

public interface ComponentEventSubscriber<E extends ComponentEvent> {
   public void notifyOnEvent( E event );
}
