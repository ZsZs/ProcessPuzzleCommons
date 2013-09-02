package com.processpuzzle.commons.event;

public interface ComponentEventSubscriber<E extends ComponentEvent> {
   public void notifyOnEvent( E event );
}
