<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl ="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method='html' version='1.0' encoding='utf-8' indent='yes'/>
    <xsl:template match="/">
        <html>
            <body>
                <table border="1">
<!--                    表名-->
                    <caption>通讯录</caption>
<!--                    列名-->
                    <tr>
                        <td>姓名</td>
                        <td>会员编号</td>
                        <td>性别</td>
                        <td>地址</td>
                        <td>电话号码</td>
                        <td>电话类型</td>
                    </tr>
<!--                    循环显示每个人-->
                    <xsl:for-each select="//person">
<!--                        每个人循环显示电话-->
                        <xsl:for-each select="phone">
                            <tr>
<!--                                显示除了电话之外的属性,若电话有多个,这些属性只显示一次-->
                                <xsl:if test="position()=1">
<!--                                    显示姓名-->
                                    <td>
<!--                                        根据电话数量设置跨行数-->
                                        <xsl:attribute name = "rowspan">
                                            <!--                                            <xsl:value-of select = "count(../course)"/>-->
                                            <xsl:value-of select = "../../person/@phoneCount"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="../name"/>
                                    </td>
<!--                                    显示编号-->
                                    <td align="right">
                                        <xsl:attribute name="rowspan">
                                            <!--                                            <xsl:value-of select = "count(../course)"/>-->
                                            <xsl:value-of select = "../../person/@phoneCount"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="../no"/>
                                    </td>
<!--                                    显示性别-->
                                    <td>
                                        <xsl:attribute name = "rowspan">
                                            <!--                                            <xsl:value-of select = "count(../course)"/>-->
                                            <xsl:value-of select = "../../person/@phoneCount"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="../sex"/>
                                    </td>
<!--                                    显示地址-->
                                    <td align="right">
                                        <xsl:attribute name="rowspan">
                                            <!--                                            <xsl:value-of select = "count(../course)"/>-->
                                            <xsl:value-of select = "../../person/@phoneCount"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="../address"/>
                                    </td>
                                </xsl:if>
                                <td><xsl:value-of select="phonenumber"/></td>
                                <td align="right"><xsl:value-of select="phonetype"/></td>
                            </tr>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
