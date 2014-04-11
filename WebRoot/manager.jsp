<%@page import="vell.bibi.vview.Setting"%>
<%@ page errorPage="errorpage/errpage.jsp" %>
<%@page import="vell.bibi.vview.category.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" import="vell.bibi.vview.post.*"%>
<%@page import="vell.bibi.vview.user.*"%>
<%
request.setCharacterEncoding("UTF-8");
Cookie[] cookies = request.getCookies();
String username = (String)session.getAttribute("username");
String username_li = null;
if(username == null && !UserManager.isLogin(cookies)){//登陆失败
	%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你没有此权限哦...<br/>请先<a href=login.jsp>登陆</a> "/> <%
}
else {//登陆成功
	if(username == null) username = UserManager.getCookiesValue(cookies, "username");
	username_li = "<a href=\"room.jsp?author_name="+username+"\">"+username+"</a><br/><a href=\"manager.jsp\">管理</a>/<a href=\"logout.jsp\">退出</a>";
	
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Author" content="VellBibi">
<meta name="Keywords" content="<%=Setting.getvKeywords() %>">
<meta name="Description" content="<%=Setting.getvDescription() %>">
<link href="css/global.css" rel="stylesheet" type="text/css">
<title>V&View(维视)-管理</title>

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
    		<li class="double" id="username"><%=username_li %></li>
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
			<li class="item" id="nav_active"><a href="index.jsp">主页</a></li>
			<%
			List<Category> nav_c_list = CategoryManager.getAllCategory();
			Iterator<Category> nav_c_it = nav_c_list.iterator();
			Category nav_c = null;
			String nav_id = "";
			while(nav_c_it.hasNext()){
				nav_c = (Category)nav_c_it.next();
				%>
				<li class="item" id="<%=nav_id %>"><a href="category.jsp?id=<%=nav_c.getId() %>"><%=nav_c.getName() %></a></li>
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
	<br/><br/><br/><br/><br/><br/>
	<h2 align="center"><a href="change_pwd.jsp">修改密码</a></h2>
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