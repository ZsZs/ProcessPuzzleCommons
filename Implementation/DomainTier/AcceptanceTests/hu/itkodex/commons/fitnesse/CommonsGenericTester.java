package hu.itkodex.commons.fitnesse;

import hu.itkodex.commons.fitnesse.generics.domain.TypeParameterInvestigator;
import fit.Fixture;
import fitlibrary.DoFixture;

public class CommonsGenericTester extends DoFixture {
   public Fixture typeParameterInvestigator() {
      return new TypeParameterInvestigator();
   }
}
