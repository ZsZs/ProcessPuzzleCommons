<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- Renames browserResult tag and generate test, error and failure counts -->
    <xsl:template match="browserResult">
        <xsl:variable name="browserPath" select="substring-after( substring-before( browser/fullFileName, '.exe' ), '/' )" />

        <xsl:choose>
            <xsl:when test="@type = 'TIMED_OUT'">
                <testsuite name="{@id}" time="{@time}" tests="1" errors="1" failures="0">
                    <testcase name="browserTimedOut" time="0.0">
                        <error message="Browser {$browserPath} timed out"/>
                    </testcase>
                </testsuite>
            </xsl:when>

            <xsl:otherwise>
                <xsl:variable name="testCount" select="count(testCaseResults/testCaseResult)"/>
                <xsl:variable name="failCount" select="count(testCaseResults/testCaseResult/failure)"/>
                <xsl:variable name="errorCount" select="count(testCaseResults/testCaseResult/error)"/>
                <xsl:variable name="browserName">
					<xsl:call-template name="stripLast">
				     	<xsl:with-param name="pText" select="$browserPath"/>
				    </xsl:call-template>
				</xsl:variable>
                <testsuite package="{$browserName}" name="{$browserName}" time="{@time}" tests="{$testCount}" errors="{$errorCount}" failures="{$failCount}">
                    <xsl:apply-templates />
                </testsuite>
            </xsl:otherwise>

        </xsl:choose>

    </xsl:template>

    <xsl:template match="browserResult/properties">
        <properties>
            <xsl:copy-of select="./*" />
        </properties>
    </xsl:template>

    <xsl:template match="browserResult/testCaseResults/testCaseResult">
        <xsl:variable name="browserPath" select="substring-after( substring-before( //browser/fullFileName, '.exe' ), '/' )" />
        <xsl:variable name="testClassName" select="substring-before( substring-after( substring-before( @name, ':'), '../ObjectTests/' ), '.html' )"/>
        <xsl:variable name="testMethodName" select="substring-after( @name, ':')"/>
        <xsl:variable name="browserName">
			<xsl:call-template name="stripLast">
				<xsl:with-param name="pText" select="$browserPath"/>
			</xsl:call-template>
		</xsl:variable>
        <xsl:variable name="standardClassName">
			<xsl:call-template name="replaceCharacters">
				<xsl:with-param name="subjectText" select="$testClassName"/>
				<xsl:with-param name="pattern" select="'/'"/>
				<xsl:with-param name="replaceText" select="'.'"/>
			</xsl:call-template>
		</xsl:variable>
        <testcase package="{$browserName}" classname="{$standardClassName}" name="{$testMethodName}" time="{@time}">
            <xsl:copy-of select="./*" />
        </testcase>
    </xsl:template>

  	<xsl:template name="stripLast">
    	<xsl:param name="pText"/>
    	<xsl:param name="pDelim" select="'/'"/>
		
		<xsl:choose>
			<xsl:when test="contains( $pText, $pDelim )">
           		<xsl:call-template name="stripLast">
	              	<xsl:with-param name="pText" select="substring-after( $pText, $pDelim )"/>
	            	<xsl:with-param name="pDelim" select="$pDelim"/>
           		</xsl:call-template>
			</xsl:when>
			
			<xsl:otherwise>
            	<xsl:value-of select="$pText"/>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>

	<xsl:template name="replaceCharacters">
	    <xsl:param name="subjectText"/>
	    <xsl:param name="pattern" />
	    <xsl:param name="replaceText" />

	    <xsl:if test="contains( $subjectText, $pattern )">
	    	<xsl:value-of select="substring-before( $subjectText, $pattern )"/>
	       	<xsl:call-template name="replaceCharacters">
		    	<xsl:with-param name="subjectText" select="concat( $replaceText, substring-after( $subjectText, $pattern ))"/>
	        	<xsl:with-param name="pattern" select="$pattern"/>
	        	<xsl:with-param name="replaceText" select="$replaceText"/>
		    </xsl:call-template>
	    </xsl:if>
	</xsl:template>
    <!-- Discard child nodes of browserResult/browser. -->
    <xsl:template match="browserResult/browser" />
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Transform JsUnit report to JUnit structure" userelativepaths="yes" externalpreview="no" url="..\..\..\..\ProcessPuzzleUI\Implementation\WebTier\Build\Reports\JsUnit\xml-raw\JSTEST-1328820134063.0.xml" htmlbaseurl=""
		          outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline=""
		          postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator="">
			<advancedProp name="sInitialMode" value=""/>
			<advancedProp name="bXsltOneIsOkay" value="true"/>
			<advancedProp name="bSchemaAware" value="true"/>
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