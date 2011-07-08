<?xml version="1.0"?>
<!--

   Copyright 2009 Ingo Feltes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
</xsl:stylesheet> 