<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <xsl:template match="/">
        <newXML>
            <xsl:apply-templates select="//studentGrades"/>
        </newXML>
    </xsl:template>

    <xsl:template match="studentGrades">
        <xsl:variable name="totalGradeValue" select="grade/totalGrade/text()"/>
        <xsl:if test="$totalGradeValue &lt;= 60">
            <xsl:copy-of select="."/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>