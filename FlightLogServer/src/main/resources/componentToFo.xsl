<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:date="http://exslt.org/dates-and-times"
	exclude-result-prefixes="fo">

	<xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes" />

	<xsl:variable name="carrotDown" select="'&#9660;'"/>
	<xsl:param name="noData" select="'No maintenance history found'" />
	<xsl:param name="timeGenerated" select="'N/A'" />
	<xsl:param name="reportTitle" select="'C-GQGD, PA-28-181, Maintenance History'" />
	<xsl:param name="sortedColumn" select="'datePerformed'" />

	<xsl:variable name="componentNameCarrotDown">
		<xsl:if test="$sortedColumn = 'componentName'">
			<xsl:value-of select="$carrotDown"></xsl:value-of>
		</xsl:if>
	</xsl:variable>
	<xsl:variable name="datePerformedCarrotDown">
		<xsl:if test="$sortedColumn = 'datePerformed'">
			<xsl:value-of select="$carrotDown"></xsl:value-of>
		</xsl:if>
	</xsl:variable>
	<xsl:variable name="dateDueCarrotDown">
		<xsl:if test="$sortedColumn = 'dateDue'">
			<xsl:value-of select="$carrotDown"></xsl:value-of>
		</xsl:if>
	</xsl:variable>
	<xsl:variable name="hoursDueCarrotDown">
		<xsl:if test="$sortedColumn = 'hoursDue'">
			<xsl:value-of select="$carrotDown"></xsl:value-of>
		</xsl:if>
	</xsl:variable>
	
<!-- 	<xsl:variable name="datePerformedBgColor"> -->
<!-- 		<xsl:if test="$sortedColumn = 'datePerformed'"> -->
<!-- 			<xsl:text>rgb(220,220,220)</xsl:text> -->
<!-- 		</xsl:if> -->
<!-- 	</xsl:variable> -->
<!-- 	<xsl:variable name="dateDueBgColor"> -->
<!-- 		<xsl:if test="$sortedColumn = 'dateDue'"> -->
<!-- 			<xsl:text>rgb(220,220,220)</xsl:text> -->
<!-- 		</xsl:if> -->
<!-- 	</xsl:variable> -->

	<!-- ========================= -->
	<!-- ========================= -->

	<xsl:template match="componentAndHistoryVS">
	
		<fo:root>

			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-width="8.5in" page-height="11in" margin-top="1em"
					margin-bottom="1em" margin-left="1em" margin-right="1em">

					<fo:region-body margin-top="2em" margin-bottom="2em" />
					<fo:region-before extent="2em" /> <!-- extent should be equal or less than margin-top of region-body -->
					<fo:region-after extent="2em" /> <!-- extent should be equal or less than margin-bottom of region-body -->
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="simpleA4">
			<!-- Page content -->
				<!-- Header -->
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="center">
						<xsl:value-of select="$reportTitle" />
					</fo:block>
				</fo:static-content>
				<!-- Footer -->
				<fo:static-content flow-name="xsl-region-after">
					<fo:table table-layout="fixed" width="100%" border-collapse="separate">
						<fo:table-column column-width="45%" />
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="45%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="center">
										<fo:page-number />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right" font-size="8pt">
										Report generated on
										<xsl:value-of select="$timeGenerated" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>

				<!-- Body -->
				<fo:flow flow-name="xsl-region-body">
					<fo:table>
						<fo:table-column column-width="11em"/>
						<fo:table-column column-width="11em"/>
						<fo:table-column column-width="10em"/>
						<fo:table-column column-width="5em"/>
						<fo:table-column column-width="3em"/>
						<fo:table-column column-width="5em"/>
						<fo:table-column column-width="3em"/>
			
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Component  <xsl:value-of select="$componentNameCarrotDown"/> /</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Part /</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Work Performed</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Date P <xsl:value-of select="$datePerformedCarrotDown"/></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Hrs P</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Date D <xsl:value-of select="$dateDueCarrotDown"/></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Hrs D <xsl:value-of select="$hoursDueCarrotDown"/></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Description</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="10pt">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
			
						<fo:table-body>
							<xsl:choose>
								<xsl:when test="componentAndHistoryV">
									<xsl:apply-templates select="componentAndHistoryV" />
								</xsl:when>
								<xsl:otherwise>
									<fo:block font-style="italic">
										<xsl:value-of select="$noData"></xsl:value-of>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: componentAndHistoryV -->
	<!-- ========================= -->
	<xsl:template match="componentAndHistoryV">

	<fo:table-row>
		<!-- if componentName is not empty, print with top solid line -->
		<xsl:choose>
			<xsl:when test="componentName">
				<fo:table-cell border-top-style="solid" padding-right="0.7em">
					<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="componentName" /></fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:otherwise>
				<fo:table-cell padding-right="0.7em">
					<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="componentName" /></fo:block>
				</fo:table-cell>
			</xsl:otherwise>
		</xsl:choose>
		<!-- if partName and componentName are empty, don't print with top solid line -->
		<xsl:choose>
			<xsl:when test="(partName = null | partName = '') and (componentName = null | componentName = '')">
				<fo:table-cell padding-right="0.7em">
					<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="partName" /></fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:otherwise>
				<fo:table-cell border-top-style="solid" padding-right="0.7em">
					<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="partName" /></fo:block>
				</fo:table-cell>
			</xsl:otherwise>
		</xsl:choose>

		<fo:table-cell border-top-style="solid" padding-right="0.7em">
			<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="workPerformed" /></fo:block>
		</fo:table-cell>
		<fo:table-cell border-top-style="solid" padding-right="0.7em">
			<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="date:format-date(datePerformed,'yyyy-MM-dd')" /></fo:block>
		</fo:table-cell>
		<fo:table-cell border-top-style="solid" padding-right="0.7em">
			<fo:block margin-top="0.5em" font-size="10pt" text-align="right"><xsl:value-of select="hoursPerformed" /></fo:block>
		</fo:table-cell>
		<fo:table-cell border-top-style="solid" padding-right="0.7em">
			<fo:block margin-top="0.5em" font-size="10pt"><xsl:value-of select="date:format-date(dateDue, 'yyyy-MM-dd')" /></fo:block>
		</fo:table-cell>
		<fo:table-cell border-top-style="solid">
			<fo:block margin-top="0.5em" font-size="10pt" text-align="right"><xsl:value-of select="hoursDue" /></fo:block>
		</fo:table-cell>
	</fo:table-row>
	<!-- print componentDescription and partDescription on second row if not empty -->
	<xsl:if test="componentDescription | partDescription">
		<fo:table-row>
			<fo:table-cell padding-right="0.7em">
				<fo:block font-size="10pt"><xsl:value-of select="componentDescription" /></fo:block>
			</fo:table-cell>
			<fo:table-cell padding-right="0.7em">
				<fo:block font-size="10pt"><xsl:value-of select="partDescription" /></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:if>

	</xsl:template>
	<!-- ========================= -->
	<!-- child element: notes -->
	<!-- ========================= -->
	<xsl:template match="notes">
		<fo:block font-size="10pt">
			<!-- <fo:table table-layout="fixed" width="100%" border-collapse="separate"> -->
			<!-- <fo:table-column column-width="4cm"/> -->
			<!-- <fo:table-column column-width="16cm"/> -->
			<!-- <fo:table-body> -->
			<xsl:apply-templates select="note" />
			<!-- </fo:table-body> -->
			<!-- </fo:table> -->
		</fo:block>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: note -->
	<!-- ========================= -->
	<xsl:template match="note">
		<!-- <fo:table-row> -->
		<!-- <xsl:if test="function = 'lead'"> -->
		<!-- <xsl:attribute name="font-weight">bold</xsl:attribute> -->
		<!-- </xsl:if> -->
		<!-- <fo:table-cell> -->
		<fo:block space-after="0.2cm">
			<xsl:value-of select="timestamp" />
			-
			<xsl:value-of select="text" />
		</fo:block>
		<!-- </fo:table-cell> -->
		<!-- </fo:table-row> -->
	</xsl:template>

</xsl:stylesheet>