<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl ="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method='html' version='1.0' encoding='utf-8' indent='yes'/>
    <xsl:template match="/">
        <html>
            <body>
                <table border="1">
<!--                    表名-->
                    <caption>学生成绩表</caption>
<!--                    列明-->
                    <tr>
                        <th>姓名</th>
                        <th>平均成绩</th>
                        <th>课程名称</th>
                        <th>单科成绩</th>
                    </tr>
<!--                    循环显示学生-->
                    <xsl:for-each select="//student">
<!--                        每个学生循环显示课程-->
                        <xsl:for-each select="course">
<!--                            课程按照成绩升序-->
                            <xsl:sort select="score"/>
                            <tr>
<!--                                显示姓名和成绩，只显示一次，从属性中获取跨行的数量-->
                                <xsl:if test="position()=1">
<!--                                    姓名-->
                                    <td>
<!--                                        设置跨行数-->
                                        <xsl:attribute name = "rowspan">
                                            <xsl:value-of select = "../name/@rowspan"/>
                                        </xsl:attribute>
<!--                                        显示姓名-->
                                        <xsl:value-of select="../name"/>
                                    </td>
<!--                                    平均成绩-->
                                    <td align="right">
<!--                                        设置跨行数-->
                                        <xsl:attribute name="rowspan">
                                            <xsl:value-of select = "../avgscore/@rowspan"/>
                                        </xsl:attribute>
<!--                                        计算平均成绩-->
                                        <xsl:value-of select='format-number(sum(../course/score) div count(../course), "##.00")'/>
                                    </td>
                                </xsl:if>
<!--                                课程名称-->
                                <td><xsl:value-of select="title"/></td>
<!--                                课程成绩-->
                                <td align="right"><xsl:value-of select="score"/></td>
                            </tr>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
