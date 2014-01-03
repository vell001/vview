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
	
	
	String action = request.getParameter("action");
	if(action != null && action.equals("change_pwd")){//修改密码
		User u = UserManager.getUserByName(username);
		String certcode = request.getParameter("certcode");
		String old_password = request.getParameter("old_password");
		String new_password1 = request.getParameter("new_password1");
		String new_password2 = request.getParameter("new_password2");
		
		if(certcode == null || !certcode.equals((String)session.getAttribute("certcode"))){//验证码错误
			%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你验证码输入错误哦... "/> <%
			return ;
		}else if(old_password == null || old_password.trim() == "" 
			|| new_password1 == null || new_password1.trim() == ""
			|| new_password2 == null || new_password2.trim() == ""){//输入是空值
			%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。请检查你的内容是否完整哦... "/> <%
			return;
		}else if(!old_password.equals(u.getPassword())){//原密码输入错误
			%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你原密码输入错误哦... "/> <%
			return;
		}else if(!new_password1.equals(new_password2)){//输入密码不一致
			%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你两次输入密码不一致哦... "/> <%
	 		return;
	 	}else {
	 		u.setCryptPassword(new_password1.trim());
	 		UserManager.updatePassword(u);
	 		%><html><head><link rel="stylesheet" type="text/css" href="css/global.css?ver=2013110701">
			<meta http-equiv="refresh" content="3;url=index.jsp"></head><body><center><br/><br/><br/><br/>
			<%
			out.print(username+",修改密码成功 <br/>3秒后跳转到<a href=\"index.jsp\">主页</a>");
			%></center></body></html><% 
			return;
	 	}
	}
 }
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Author" content="VellBibi">
<meta name="Keywords" content="<%=Setting.getvKeywords() %>">
<meta name="Description" content="<%=Setting.getvDescription() %>">
<link href="css/global.css?ver=2013110701" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/vview.js"></script>
<title>V&View(维视)</title>

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
			Iterator nav_c_it = nav_c_list.iterator();
			Category nav_c = null;
			while(nav_c_it.hasNext()){
				nav_c = (Category)nav_c_it.next();
				%>
				<li class="item" id=""><a href="category.jsp?id=<%=nav_c.getId() %>"><%=nav_c.getName() %></a></li>
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
	<br/><br/><br/><br/><br/>
	<form name="change_pwd" id="change_pwd_form" action="change_pwd.jsp" method="post">
		<input type="hidden" name="action" value="change_pwd"/>
			<h1 align="center">密码修改</h1>
			<br/><br/>
			<table align="center">
				<tr>
					<td>原密码:</td>
					<td><input name="old_password" type="password" maxlength="40"/></td>
				</tr>
				<tr>
					<td>新密码:</td>
					<td><input name="new_password1" type="password" maxlength="16"/></td>
				</tr>
				<tr>
					<td>确认密码:</td>
					<td><input name="new_password2" type="password" maxlength="16"/></td>
				</tr>
				<tr>
					<td>验证码:</td>
					<td><input type="text" id="certcode" name="certcode" size="8"/><a href="javascript:;" onclick="changeimg()"><img id="certpic" src="util/makecertpic.jsp"/>看不清，换一张 </a></td>
				</tr>
				<tr align="center">
					<td colspan="2"><input class="button" type="submit" value="确认修改" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="button" value="重置" /></td>
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