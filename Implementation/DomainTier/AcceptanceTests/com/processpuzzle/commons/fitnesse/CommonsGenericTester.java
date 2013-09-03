package com.processpuzzle.commons.fitnesse;

import com.processpuzzle.commons.fitnesse.generics.domain.TypeParameterInvestigator;

import fit.Fixture;
import fitlibrary.DoFixture;

public class CommonsGenericTester extends DoFixture {
   public Fixture typeParameterInvestigator() {
      return new TypeParameterInvestigator();
   }
}
