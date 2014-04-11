<%@page contentType="image/jpeg" %><%@page language="java" pageEncoding="utf-8"%>
<jsp:useBean id="image" scope="page" class="vell.bibi.vview.util.MakeCertPic"/>
<%
 String str = image.getCertPic(80,25,4,response.getOutputStream());
 //将验证码存入session中
 session.setAttribute("certcode",str);
%>