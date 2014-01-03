<%@page import="java.net.URLEncoder"%>
<%@page import="vell.bibi.vview.Setting"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page errorPage="errorpage/errpage.jsp" %>
<%@page import="vell.bibi.vview.category.*"%>
<%@page import="vell.bibi.vview.user.*"%>
<%
request.setCharacterEncoding("utf-8");
String action = request.getParameter("action");
String username = request.getParameter("username");
String password = request.getParameter("password");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
Cookie[] cookies = request.getCookies();
String login_username = (String)session.getAttribute("username");
if(login_username != null || UserManager.isLogin(cookies)){//登陆成功
	response.sendRedirect("logout.jsp");
	return;
}

if(action!=null && action.equals("login")){
	
	%><html><head><link rel="stylesheet" type="text/css" href="css/global.css"><%
	String certcode=request.getParameter("certcode");
	if(certcode.equals((String)session.getAttribute("certcode"))){
		User u = UserManager.getUserByName(username);
		if(u!=null && password.equals(u.getPassword())){
			session.setAttribute("username", u.getUsername());
			session.setMaxInactiveInterval(1800);
			if(request.getParameter("rememberpwd") != null && request.getParameter("rememberpwd").equals("true")){
				Cookie usernamecookie= new Cookie("username",username);
				usernamecookie.setMaxAge(2592000);
				Cookie passwordcookie= new Cookie("password",u.getCryptPassword());
				passwordcookie.setMaxAge(2592000);
				response.addCookie(usernamecookie);
				response.addCookie(passwordcookie);
			}
			%>
			<meta http-equiv="refresh" content="3;url=index.jsp"></head><body><center><br/><br/><br/><br/>
			<%
			out.print(username+",欢迎回来<br/>3秒后跳转到<a href=\"index.jsp\">主页</a>");
		} else {
			%>
			<SCRIPT language=javascript>
			function go()
			{
				window.history.go(-1);
			}
			setTimeout("go()",3000);
			</SCRIPT>
			</head><body><center><br/><br/><br/><br/><%
			out.print(username+",密码或用户名错误。。。<br/>");
			%>
				3秒后<a href="javascript:go();">返回登陆</a>
			<%
		}
	}else{
		%>
		<SCRIPT language=javascript>
		function go()
		{
			window.history.go(-1);
		}
		setTimeout("go()",3000);
		</SCRIPT>
		</head><body><center><br/><br/><br/><br/><%
		out.print("验证码输入错误");
		%>
		3秒后<a href="javascript:go();">返回登陆</a>
		<%
	}
	%></center></body></html><% 
	return;
}
%>
<html>
  <head>
	<meta name="Author" content="VellBibi">
	<meta name="Keywords" content="<%=Setting.getvKeywords() %>">
	<meta name="Description" content="<%=Setting.getvDescription() %>">
    <title>login V&View</title>
	<link rel="stylesheet" type="text/css" href="css/global.css"/>
	<script type="text/javascript" language=Javascript src="js/vview.js?ver=2013102801"> </script>
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
           		<input id="key" name="key" class="search_keyword" onfocus="if (this.value == '请输入关键字进行搜索') {this.value = '';}" onblur="if (this.value == '') {this.value = '请输入关键字进行搜索';}" value="请输入关键字进行搜索" type="text"/>   
         		<button type="submit" class="select_class" onmouseout="this.className='select_class'" onmouseover="this.className='select_over'">搜索</button>
	</form>
	</div>
	<div class="clear"></div>
</div>
<!-- /nav -->

<!-- content -->
<div class="content">
	<br/><br/><br/><br/><br/>
	<form name="login" id="login_form" action="login.jsp" method="post">
		<input type="hidden" name="action" value="login"/>
			<h1 align="center">用户登录</h1>
			<br/><br/>
			<table align="center">
				<tr>
					<td>用户名:</td>
					<td><input name="username" id="username" type="text" maxlength="40" onblur="login_validate(this)"/><span id="username_span">*</span></td>
				</tr>
				<tr>
					<td>密&nbsp;&nbsp;码:</td>
					<td><input name="password" id="password" type="password" maxlength="16" onblur="login_validate(this)"/><span id="password_span">*</span></td>
				</tr>
				<tr>
					<td>验证码:</td>
					<td><input type="text" id="certcode" name="certcode" size="8" onblur="login_validate(this)"/><a href="javascript:;" onclick="changeimg()"><img id="certpic" src="util/makecertpic.jsp" title="看不清，换一张 "/></a><span id="certcode_span">*</span></td>
				</tr>
				<tr align="center">
					<td colspan="2"><input name="rememberpwd" type="checkbox" value="true" checked="checked"/>记住密码&nbsp;&nbsp;&nbsp;<a href="#">忘记密码</a></td>
				</tr>
				<tr align="center">
					<td colspan="2"><input class="button" type="submit" value="登陆" />&nbsp;&nbsp;&nbsp;&nbsp;<a href="register.jsp">立即注册</a></td>
				</tr>
			</table>
			<br/><br/><br/><br/><br/><br/><br/><br/>
		</form>
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
