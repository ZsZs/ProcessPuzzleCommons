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
  <xsl:output method="html"/>
  <xsl:template match="/testResults">
    <xsl:variable name="right" select="finalCounts/right"/>
    <xsl:variable name="wrong" select="finalCounts/wrong"/>
    <xsl:variable name="ignores" select="finalCounts/ignores"/>
    <xsl:variable name="exceptions" select="finalCounts/exceptions"/>
    <xsl:variable name="total" select="$right + $wrong + $ignores + $exceptions"/>
    <xsl:variable name="successRate" select="($ignores + $right) div $total"/>
    <html>
      <head>
        <title>fit4oaw Test Results for <xsl:value-of select="rootPath"/></title>
        <style type="text/css">
          /* JUnit-like report style */
          body {font:normal 68% verdana,arial,helvetica;color:#000000}
          table tr td, table tr th {font-size:68%}
          table.details tr th {font-weight:bold;text-align:left;background:#a6caf0}
          table.details tr td {background:#eeeee0}
          p {line-height:1.5em;margin-top:0.5em;margin-bottom:1.0em}
          h1, h2, h3, h4, h5, h6 {font:bold verdana,arial,helvetica}
          h2, h3, h4, h5, h6 {margin-bottom:0.5em}
          h1 {margin:0px 0px 5px;font:165%}
          h2 {margin-top:1em;font:125%}
          h3 {font:115%}
          h4, h5, h6 {font:100%}
          .Error {font-weight:bold;color:red}
          .Failure {font-weight:bold;color:purple}
          /* FitNesse reports */
          .pass {background-color:#AAFFAA}
          .fail {background-color:#FFAAAA}
          .error {background-color:#FFFFAA}
          .ignore {background-color:#CCCCCC}
          .fit_stacktrace {font-size:0.7em}
          .fit_label {font-style:italic;color:#C08080}
          .fit_grey {color:#808080}
          .fitnesse {width:95%}
          .setup, .teardown {background:#FFFFF0;margin-bottom:1em;margin-top:-1em;padding:0.5em;border:dotted 1px black}
          .setup .collapsable, .teardown .collapsable {margin-top:-1em}
          .setup .meta, .setup > a, .teardown .meta, .teardown > a, .collapse_rim .meta, .collapse_rim > a {display:none}
          .fitnesse td {padding:2px}
          .fitnesse table {border-collapse:collapse}
          .fitnesse h3 {background-color:#A6CAF0;font-size:100%;padding:5px}
        </style>
      </head>
      <body>
        <h1>fit4oaw Tests Results.</h1>
        <table width="100%">
          <tbody>
            <tr>
              <td align="left"/><td align="right">Designed for use with
                <a href="http://sourceforge.net/projects/fit4oaw/">fit4oaw</a>
                and <a href="http://ant.apache.org/">Ant</a>.
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
</xsl:stylesheet>