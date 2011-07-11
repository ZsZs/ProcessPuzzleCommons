package hu.itkodex.commons.event;

public class DefaultComponentEvent implements ComponentEvent {
   private final String name;
   
   public DefaultComponentEvent( String name ) {
      this.name = name;
   }
   
   @Override
   public String getName() {
      return name;
   }

}
