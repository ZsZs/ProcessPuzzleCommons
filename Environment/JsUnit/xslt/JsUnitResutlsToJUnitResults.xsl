<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- Renames browserResult tag and generate test, error and failure counts -->
    <xsl:template match="browserResult">
        <xsl:variable name="browser" select="browser/fullFileName" />

        <xsl:choose>
            <xsl:when test="@type = 'TIMED_OUT'">
                <testsuite name="{@id}" time="{@time}" tests="1" errors="1" failures="0">
                    <testcase name="browserTimedOut" time="0.0">
                        <error message="Browser {$browser} timed out"/>
                    </testcase>
                </testsuite>
            </xsl:when>

            <xsl:otherwise>
                <xsl:variable name="testCount" select="count(testCaseResults/testCaseResult)"/>
                <xsl:variable name="failCount" select="count(testCaseResults/testCaseResult/failure)"/>
                <xsl:variable name="errorCount" select="count(testCaseResults/testCaseResult/error)"/>
                <testsuite name="{@id}" time="{@time}" tests="{$testCount}" errors="{$errorCount}" failures="{$failCount}">
                    <xsl:apply-templates />
                </testsuite>
            </xsl:otherwise>

        </xsl:choose>

    </xsl:template>

    <xsl:template match="browserResult/testCaseResults/testCaseResult">
        <xsl:variable name="testClassName" select="substring-before( @name, ':')"/>
        <xsl:variable name="testMethodName" select="substring-after( @name, ':')"/>
        <testcase classname="{$testClassName}" name="{$testMethodName}" time="{@time}">
            <xsl:copy-of select="./*" />
        </testcase>
    </xsl:template>

    <!-- Discard child nodes of browserResult/browser. -->
    <xsl:template match="browserResult/browser" />
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Transform JsUnit report to JUnit structure" userelativepaths="yes" externalpreview="no" url="..\..\..\..\ProcessPuzzleUI\Implementation\WebTier\Build\Reports\JsUnit\xml\JSTEST-1327431343675.0.xml" htmlbaseurl=""
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