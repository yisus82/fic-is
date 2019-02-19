<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />
	<xsl:template match="events">
		<xsl:for-each select="event">
			<tr>
				<td>
					<xsl:value-of select="description" />
				</td>
				<td>
					<xsl:value-of select="category" />
				</td>
				<td>
					<xsl:value-of select="date" />
				</td>
			</tr>
			<xsl:apply-templates select="options" />
			<tr />
			<tr />
			<tr />
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="options">
		<xsl:for-each select="option">
			<td>
				<xsl:value-of select="optionDescription" />
			</td>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
