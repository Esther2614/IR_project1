<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
            <%@ page import="WebIR.Article" %>
                <%@ page import="java.util.List" %>
                    <%@ page import="WebIR.Page" %>
                        <%@ page import="java.util.Iterator" %>
                            <% List<Article> articlelist = (List<Article>) session.getAttribute("articlelist");
                                    Integer totalars = (Integer) session.getAttribute("totaln");
                                    String queryback = (String) session.getAttribute("queryback");
                                    double time = Double.parseDouble(session.getAttribute("time").toString());
                                    Page pageInfo=(Page)session.getAttribute("page");
                                    int p = 1, i ;
                                    %>

                                    <html>

                                    <head>
                                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                                        <title>Search Result</title>
                                        <link rel="icon" href="./search_line_icon.svg">
                                        <link
                                            href="${pageContext.request.contextPath}/css/main.css?<?php echo time(); ?"
                                            rel="stylesheet" type="text/css">
                                        <link rel="stylesheet"
                                              href="${pageContext.request.contextPath}/css/bootstrap.min.css?<?php echo time(); ?">
                                    </head>

                                    <body>

                                        <!--
                        <div class="nav">
                            <div class="nav_left">
                                <a href="search.jsp">
                                    <img alt="logo" src="logo.png">
                                </a>
                            </div>

                            <div class="nav_right">

                                <div class="nav_form">

                                    <form action="searchWeb" method="get">
                                        <input id="queryString" type="text" name="queryString" value="">
                                        <input class="search" type="submit" value="搜索"><br />
                                    </form>

                                    <form action="searchWeb" method="post">
                                        <input type="text" id="queryString" value="" name="queryString" class="searchbar">
                                        <input type="image" src="search_line_icon.png" name="submit" class="mg">
                                    </form>

                                </div>


                            </div>
                        </div>
                    -->
                                        <section>

                                            <form action="searchWeb" method="post">
                                                <div class="nav">
                                                    <a href="search.jsp" class="nav">
                                                        <img alt="logo" src="logo.png">
                                                    </a>
                                                </div>


                                                <input type="text" id="search" value="Search Academic Papers"
                                                    onfocus="if(value=='Search Academic Papers')value=''"
                                                    onblur="if(!value)value='Search Academic Papers'"
                                                    name="queryString" class="searchbar">
                                                <input type="image" src="search_line_icon.svg" name="submit" class="mg">
                                            </form>


                                        <div class="newmain">
                                            <h4>
                                                共搜到<span class="newsnum">
                                                    <%=totalars%>
                                                </span>条结果|用时<span class="newsnum">
                                                    <%=time%>
                                                </span>秒
                                            </h4>

                                            <% if (articlelist.size()> 0) {
                                                Iterator<Article> iter = articlelist.iterator();
                                                    Article news;
                                                    while (iter.hasNext()) {
                                                    news = iter.next();
                                                    %>

                                                    <div class="item">
                                                        <h4>
                                                            <a href="<%=news.getUrl()%> " target="_blank">
                                                                <%=news.getTitle()%>
                                                            </a>
                                                        </h4>
                                                        <p class="line-limit-length">
                                                        <% if(news.getAuthor().length()!=0) { %>
                                                            <b>
                                                                作者：
                                                            </b><%=news.getAuthor() %><br><% } %>
                                                        <% if(news.getAffiliation().length()!=0) { %>
                                                            <b>
                                                                机构：
                                                            </b><%=news.getAffiliation() %><br><% } %>
                                                        <% if(news.getAddress().length()!=0) { %>
                                                            <b>
                                                                地址：
                                                            </b><%=news.getAddress() %><br><% } %>
                                                        <% if(news.getDate().length()!=0) { %>
                                                            <b>
                                                                时间：
                                                            </b><%=news.getDate() %><br><% } %>
                                                        </p>
                                                        <p class="line-limit-length">
                                                        <% if(news.getAbKey().length()!=0) { %>
                                                            <b>
                                                                摘要与关键词：
                                                            </b><%=news.getAbKey() %><br><% } %>
                                                        </p>
                                                        <p class="full-line-limit-length">
                                                        <% if(news.getMaintext().length()!=0) { %>
                                                            <b>
                                                                正文：
                                                            </b><%=news.getMaintext() %><br><% } %>
                                                        </p>
                                                        <p class="line-limit-length">
                                                        <% if(news.getAckRef().length()!=0) { %>
                                                            <b>
                                                                致谢与参考文献：
                                                            </b><%=news.getAckRef() %><% } %>
                                                        </p>

                                                    </div>
                                                    <% } } %>
                                        </div>


                                        <div class="paging">
                                            <ul>
                                                <li><a href="searchWeb?queryString=<%=queryback%>&&p=1">首页</a></li>
                                                <li><a
                                                        href="searchWeb?queryString=<%=queryback%>&&p=<%=pageInfo.getPage()-1==0?1:pageInfo.getPage()-1%>">上一页</a>
                                                </li>
                                                <% int pp=pageInfo.getPage(); if(pp<=3){ pp=3; } int
                                                    endp=pageInfo.getPage()+7; if(pageInfo.getTotalPage()<endp){
                                                    endp=pageInfo.getTotalPage(); } for (i=pp-2; i <=endp; i++) {
                                                    if(i==pageInfo.getPage()){ %>
                                                    <li>
                                                        <%=i %>
                                                    </li>
                                                    <% }else{ %>
                                                        <li><a href="searchWeb?queryString=<%=queryback%>&&p=<%=i%>">
                                                                <%=i%>
                                                            </a></li>
                                                        <% } } %>
                                                            <li><a
                                                                    href="searchWeb?queryString=<%=queryback%>&&p=<%=pageInfo.getTotalPage()>pageInfo.getPage() ? pageInfo.getPage()+1 : pageInfo.getPage()%>">下一页</a>
                                                            </li>
                                                            <li><a
                                                                    href="searchWeb?queryString=<%=queryback%>&&p=<%=pageInfo.getTotalPage()%>">尾页</a>
                                                            </li>
                                            </ul>
                                            <hr>
                                        </div>

                                        <div class="footerinfo">
                                            <p>信息检索 搜索引擎</p>
                                        </div>
                                        </section>
                                    </body>

                                    </html>