<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml"/>

  <xsl:template match="/testResults">
    <xsl:variable name="right" select="finalCounts/right"/>
    <xsl:variable name="wrong" select="finalCounts/wrong"/>
    <xsl:variable name="ignores" select="finalCounts/ignores"/>
    <xsl:variable name="exceptions" select="finalCounts/exceptions"/>
    <xsl:variable name="total" select="$right + $wrong + $ignores + $exceptions"/>
    <testsuite name="FitNesse" time="0">
      <xsl:attribute name="errors"><xsl:value-of select="$exceptions"/></xsl:attribute>
      <xsl:attribute name="failures"><xsl:value-of select="$wrong"/></xsl:attribute>
      <xsl:attribute name="tests"><xsl:value-of select="$total"/></xsl:attribute>
    <properties/>
    <xsl:apply-templates select="result"/>
    <system-out/>
    <system-err/>
    </testsuite>
  </xsl:template>

  <xsl:template match="result">
    <testcase time="0">
      <xsl:attribute name="name"><xsl:value-of select="relativePageName"/></xsl:attribute>
      <xsl:attribute name="classname"><xsl:value-of select="relativePageName"/></xsl:attribute>
      <xsl:choose>
        <xsl:when test="counts/exceptions &gt; 0">
          <error>
            <xsl:attribute name="message"><xsl:apply-templates select="counts"/></xsl:attribute>
          </error>
        </xsl:when>
        <xsl:when test="counts/wrong &gt; 0">
          <failure>
            <xsl:attribute name="message"><xsl:apply-templates select="counts"/></xsl:attribute>
          </failure>
        </xsl:when>
      </xsl:choose>
    </testcase>
  </xsl:template>

  <xsl:template match="counts">
    <xsl:value-of select="right"/> right,
    <xsl:value-of select="wrong"/> wrong,
    <xsl:value-of select="ignores"/> ignored,
    <xsl:value-of select="exceptions"/> exceptions
  </xsl:template>
</xsl:stylesheet> <!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
   <scenarios>
      <scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\Implementation\DomainTier\Build\Reports\FitNesse\20140205113042_1_0_0_0.xml" htmlbaseurl=""
                outputurl="..\..\Implementation\DomainTier\Build\Reports\FitNesse\xml\20140205113042_1_0_0_0.xml" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath=""
                additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator="">
         <advancedProp name="sInitialMode" value=""/>
         <advancedProp name="schemaCache" value="||"/>
         <advancedProp name="bXsltOneIsOkay" value="true"/>
         <advancedProp name="bSchemaAware" value="true"/>
         <advancedProp name="bGenerateByteCode" value="true"/>
         <advancedProp name="bXml11" value="false"/>
         <advancedProp name="iValidation" value="0"/>
         <advancedProp name="bExtensions" value="true"/>
         <advancedProp name="iWhitespace" value="0"/>
         <advancedProp name="sInitialTemplate" value=""/>
         <advancedProp name="bTinyTree" value="true"/>
         <advancedProp name="xsltVersion" value="2.0"/>
         <advancedProp name="bWarnings" value="true"/>
         <advancedProp name="bUseDTD" value="false"/>
         <advancedProp name="iErrorHandling" value="fatal"/>
      </scenario>
   </scenarios>
   <MapperMetaTag>
      <MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
         <SourceSchema srcSchemaPath="..\..\Implementation\DomainTier\Build\Reports\FitNesse\20140205113042_1_0_0_0.xml" srcSchemaRoot="suiteResults" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
      </MapperInfo>
      <MapperBlockPosition>
         <template match="/testResults">
            <block path="testsuite/xsl:attribute/xsl:value-of" x="377" y="36"/>
            <block path="testsuite/xsl:attribute[1]/xsl:value-of" x="337" y="54"/>
            <block path="testsuite/xsl:attribute[2]/xsl:value-of" x="377" y="72"/>
            <block path="testsuite/xsl:apply-templates" x="337" y="18"/>
         </template>
      </MapperBlockPosition>
      <TemplateContext></TemplateContext>
      <MapperFilter side="source"></MapperFilter>
   </MapperMetaTag>
</metaInformation>
-->