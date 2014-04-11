<%@page import="vell.bibi.vview.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@page import="vell.bibi.vview.user.*"%>
<%@page import="vell.bibi.vview.category.*"%>
<%
request.setCharacterEncoding("UTF-8");
Cookie[] cookies = request.getCookies();
String username = (String)session.getAttribute("username");
String username_li = null;
if(username == null && !UserManager.isLogin(cookies)){//登陆失败
	username_li = "<a href=\"register.jsp\">加入我们</a><br/><a href=\"login.jsp\">登陆</a>";
}
else {//登陆成功
	if(username == null) username = UserManager.getCookiesValue(cookies, "username");
	username_li = "<a href=\"room.jsp?username="+username+"\">"+username+"</a><br/><a href=\"logout.jsp\">退出</a>";
}
%>

<%
String mes = request.getParameter("mes");
if(mes == null){
	if(exception != null)
		mes = exception.getMessage();
	else
		mes = "访问错误。。。";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  	<base href="<%=Setting.getBasepath()%>">
    <title>error page</title>
	<link href="css/global.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/vview.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  
  <body>
  <div class="body_div">
<!-- header -->
<div class="header">
  <div class="header_main">
  	<div class="logo_div"> 
    	<a href="index.jsp"><img class="logo_img" height="80px" border="0" src="images/logo.png" /> </a>
    </div>
    <div class="top_menu_div">
    	<ul class="top_menu_ul">
    		<li class="double" id=""><%=username_li %></li>
    		<li class="single" id=""><a href="">订阅</a></li>
    		<li class="single" id=""><a href="editor/index.jsp">投稿</a></li>
    		<li class="single" id=""><a href="">建议箱</a></li>
    	</ul>
    </div>
  </div>
</div>
<!-- /header -->
<!-- nav -->
<div class="nav">
	<div class="nav_main">
		<ul class="nav_ul">
			<li class="item" id=""><a href="index.jsp">主页</a></li>
			<%
			List<Category> nav_c_list = CategoryManager.getAllCategory();
			Iterator nav_c_it = nav_c_list.iterator();
			Category c = null;
			while(nav_c_it.hasNext()){
				c = (Category)nav_c_it.next();
				%>
				<li class="item" id=""><a href="category.jsp?id=<%=c.getId() %>"><%=c.getName() %></a></li>
				<%
			}
			%>
		</ul>
	</div>
	<div class="nav_search">
	<form name="search_form" class="search" method="get" action="">
           		<label for="key"></label>
           		<input id="key" name="key" class="search_keyword" onfocus="if (this.value == '请输入关键字进行搜索') {this.value = '';}" onblur="if (this.value == '') {this.value = '请输入关键字进行搜索';}" value="请输入关键字进行搜索" type="text">   
         		<button type="submit" class="select_class" onmouseout="this.className='select_class'" onmouseover="this.className='select_over'">搜索</button>
	</form>
	</div>
</div>
<!-- /nav -->

<!-- content -->
<div class="content">
	<br/><br/><br/><br/><br/><br/><br/><br/>
	<h2 align="center">
	不好意思，出错了哦。。。请与<a href="mailto:<%=Setting.getAdminEmail()%>?subject=出错了&body=<%=mes%>">管理员</a>联系呗...<br/>
	管理员邮箱:<%=Setting.getAdminEmail()%> <br/>
	错误信息：<div style="color: red"><%=mes %></div>
	<a href="javascript:window.history.back()">返回上一页</a>
	</h2>
</div>
<!-- /content -->

<!-- footer -->
<div class="footer">
	<div class="copyright">COPYRIGHT &copy; 2013 <a href="index.jsp" target="_blank">V&View维视</a> ALL RIGHT RESERVED</div>
</div>
<!-- /footer -->
</div>
  </body>
</html>
