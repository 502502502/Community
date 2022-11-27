<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl ="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method='html' version='1.0' encoding='utf-8' indent='yes'/>
    <xsl:template match="/">
        <html>
            <body>
<!--                表名-->
                <h2>My CD Collection</h2>
<!--                列名-->
                <table border="1">
                    <tr><th bgcolor="#98fb98">Title</th>
                        <th bgcolor="#98fb98">Artist</th>
                        <th bgcolor="#98fb98">country</th>
                        <th bgcolor="#98fb98">company</th>
                        <th bgcolor="#98fb98">price</th>
                        <th bgcolor="#98fb98">year</th>
                    </tr>
<!--                    循环显示唱片-->
                    <xsl:for-each select="catalog/cd">
<!--                        按照年份降序-->
                        <xsl:sort select="year" order="descending"/>
<!--                        如果年份在1980以后才显示-->
                        <xsl:if test="year &gt; 1980">
                            <tr>
<!--                                显示标题-->
                                <td>
                                    <xsl:value-of select="title"/>
                                </td>
<!--                                显示作者-->
                                <td>
                                    <xsl:value-of select="artist"/>
                                </td>
<!--                                显示国家-->
                                <td>
                                    <xsl:value-of select="country"/>
                                </td>
<!--                                显示公司-->
                                <td>
                                    <xsl:value-of select="company"/>
                                </td>
<!--                                判断价格区间,显示不同颜色-->
                                <xsl:choose>
<!--                                    在10以上-->
                                    <xsl:when test="price &gt; 10">
                                        <td bgcolor="#f08080">
                                            <xsl:value-of select="price"/>
                                        </td>
                                    </xsl:when>
<!--                                    在9~10-->
                                    <xsl:when test="price &gt; 9">
                                        <td bgcolor="#ffa07a">
                                            <xsl:value-of select="price"/>
                                        </td>
                                    </xsl:when>
<!--                                    在8~9-->
                                    <xsl:when test="price &gt; 8">
                                        <td bgcolor="#ffc0cb">
                                            <xsl:value-of select="price"/>
                                        </td>
                                    </xsl:when>
<!--                                    其余价格-->
                                    <xsl:otherwise>
                                        <td bgcolor="#e0ffff">
                                            <xsl:value-of select="price"/>
                                        </td>
                                    </xsl:otherwise>
                                </xsl:choose>
<!--                                显示年份-->
                                <td>
                                    <xsl:value-of select="year"/>
                                </td>
                            </tr>
                        </xsl:if>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
