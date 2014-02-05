<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes" />

  <xsl:template match="/testResults">
    <xsl:variable name="right" select="finalCounts/right"/>
    <xsl:variable name="wrong" select="finalCounts/wrong"/>
    <xsl:variable name="ignores" select="finalCounts/ignores"/>
    <xsl:variable name="exceptions" select="finalCounts/exceptions"/>
    <xsl:variable name="total" select="$right + $wrong + $ignores + $exceptions"/>
    <xsl:variable name="successRate" select="($ignores + $right) div $total"/>
    <html>
      <head>
        <title>FitNesse Test Results for <xsl:value-of select="rootPath"/></title>
        <link rel="stylesheet" type="text/css" href="Fitnesse-TestPage.css" />
      </head>
      <body>
        <h1>FitNesse Tests Results.</h1>
        <table width="100%">
          <tbody>
            <tr>
              <td align="left"/><td align="right">Designed for use with
                <a href="http://fitnesse.org/">FitNesse</a> and <a href="http://ant.apache.org/">Ant</a>.
              </td>
            </tr>
          </tbody>
        </table>
        <hr size="1"/>
        <h2>Summary</h2>
        <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
          <tr valign="top">
            <th>Tests</th><th>Ignored</th><th>Failures</th><th>Errors</th><th>Success rate</th>
          </tr>
          <tr>
            <xsl:attribute name="class">
              <xsl:choose>
                <xsl:when test="$exceptions != 0">Error</xsl:when>
                <xsl:when test="$wrong != 0">Failure</xsl:when>
                <xsl:otherwise>Pass</xsl:otherwise>
              </xsl:choose>
            </xsl:attribute> 
            <td><xsl:value-of select="$total"/></td>
            <td><xsl:value-of select="$ignores"/></td>
            <td><xsl:value-of select="$wrong"/></td>
            <td><xsl:value-of select="$exceptions"/></td>
            <td><xsl:value-of select="format-number($successRate, '0.00%')"/></td>
          </tr>
        </table>

        <h2>Tests</h2>
        <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
          <tr valign="top">
            <th width="70%">Name</th><th>Passed</th><th>Ignored</th><th>Failures</th><th>Errors</th><th>Success Rate</th>
          </tr>
          <xsl:apply-templates mode="results" select="result"/>
        </table>

        <h2>Test Details</h2>
        <div class="fitnesse">
          <xsl:apply-templates mode="details" select="result"/>
        </div>
      </body>
    </html>
    </xsl:template>

    <xsl:template mode="results" match="result">
      <xsl:variable name="right" select="counts/right"/>
      <xsl:variable name="wrong" select="counts/wrong"/>
      <xsl:variable name="exceptions" select="counts/exceptions"/>
      <xsl:variable name="ignores" select="counts/ignores"/>
      <xsl:variable name="successRate" select="$right div ($right + $wrong)"/>
      <tr>
        <xsl:attribute name="class">
          <xsl:choose>
            <xsl:when test="$exceptions != 0">Error</xsl:when>
            <xsl:when test="$wrong != 0">Failure</xsl:when>
            <xsl:otherwise>Pass</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <td>
          <a>
            <xsl:attribute name="href">#<xsl:value-of select="relativePageName"/></xsl:attribute>
            <xsl:value-of select="relativePageName"/>
          </a>
        </td>
        <td><xsl:value-of select="$right"/></td>
        <td><xsl:value-of select="$ignores"/></td>
        <td><xsl:value-of select="$wrong"/></td>
        <td><xsl:value-of select="$exceptions"/></td>
        <td>
          <xsl:choose>
            <xsl:when test="$exceptions != 0">N/A</xsl:when>
            <xsl:otherwise><xsl:value-of select="format-number($successRate, '0.00%')"/></xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>
    <xsl:template mode="details" match="result">
      <a><xsl:attribute name="name"><xsl:value-of select="relativePageName"/></xsl:attribute></a>
      <h3><xsl:value-of select="relativePageName"/></h3>
      <xsl:value-of select="content" disable-output-escaping="yes"/>
  </xsl:template>
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
   <scenarios>
      <scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no"
                url="..\..\..\..\EntwicklungsUmgebung\FitNesse\FitNesseRoot\files\testResults\FitNesse.SuiteAcceptanceTests.SuiteFixtureTests.SuiteColumnFixtureSpec.TestArraysInColumnFixture\20140205120236_1_0_0_0.xml" htmlbaseurl=""
                outputurl="..\..\Implementation\DomainTier\Build\Reports\FitNesse\html\20140205120236_1_0_0_0.html" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath=""
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
      <scenario default="no" name="Scenario2" userelativepaths="yes" externalpreview="no"
                url="..\..\..\..\EntwicklungsUmgebung\FitNesse\FitNesseRoot\files\testResults\FitNesse.SuiteAcceptanceTests.SuiteFixtureTests.SuiteColumnFixtureSpec.TestArraysInColumnFixture\20140205120236_1_0_0_0.xml" htmlbaseurl=""
                outputurl="..\..\Implementation\DomainTier\Build\Reports\FitNesse\html\test_results.html" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath=""
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
      <MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
      <MapperBlockPosition></MapperBlockPosition>
      <TemplateContext></TemplateContext>
      <MapperFilter side="source"></MapperFilter>
   </MapperMetaTag>
</metaInformation>
-->