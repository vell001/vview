<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="vell.bibi.vview.mail.VMailManager"%>
<%@page import="vell.bibi.vview.util.UID"%>
<%@page import="vell.bibi.vview.util.DesUtil"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="sun.font.EAttribute"%>
<%@page import="vell.bibi.vview.Setting"%>
<%@ page errorPage="errorpage/errpage.jsp" %>
<%@page import="vell.bibi.vview.category.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" import="vell.bibi.vview.post.*"%>
<%@page import="vell.bibi.vview.user.*"%>
<%
request.setCharacterEncoding("UTF-8");
String username_li = "<a href=\"register.jsp\">加入我们</a><br/><a href=\"login.jsp\">登陆</a>";
%>
<%
String action = request.getParameter("action");
if(action != null && action.equals("register")){//处理注册
	String certcode = request.getParameter("certcode");
	String username = request.getParameter("username");
	String password1 = request.getParameter("password1");
	String password2 = request.getParameter("password2");
	String email = request.getParameter("email");
	String user_url = request.getParameter("user_url");
	
	if(certcode == null || !certcode.equals((String)session.getAttribute("certcode"))){//验证码错误
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你验证码输入错误哦... "/> <%
		return ;
	}else if(username == null || username.trim().equals("")
			|| password1 == null || password1.trim().equals("")
			|| password2 == null || password2.trim().equals("")
			|| email == null || email.trim().equals("")){
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。请检查你的内容是否完整哦... "/> <%
		return;
	}else if(UserManager.getUserByName(username.trim()) != null){//用户已存在
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。用户已存在，请选择一个与众不同的用户名哦... "/> <%
		return;
	}else if(!password1.trim().equals(password2.trim())){//输入密码不一致
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你两次输入密码不一致哦... "/> <%
 	return;
 	}else {
 		User u = new User();
 		u.setCryptPassword(password1.trim());
 		u.setEmail(email.trim());
 		u.setUsername(username.trim());
 		u.setRdate(new Timestamp(System.currentTimeMillis()));
 		u.setStatus(0);
 		u.setUser_url(user_url.trim());
 		u.setVer_code(String.valueOf(UID.next()));
 		u.saveToDB();
 		/* 发送邮箱验证   */
 		VMailManager.sendUserVerificationCode(u);
 %><html><head><link rel="stylesheet" type="text/css" href="css/global.css">
		<meta http-equiv="refresh" content="3;url=index.jsp"></head><body><center><br/><br/><br/><br/>
		<%
		out.print(username+",注册完成<br/>请及时查收验证邮件哦。。。 <br/>3秒后跳转到<a href=\"index.jsp\">主页</a>");
		%></center></body></html><%
		return;
	}
}else if(action != null && action.equals("verification")){//处理邮箱验证
	String verification_code = request.getParameter("verification_code");
	String ver_name = URLDecoder.decode(request.getParameter("ver_name"),"GB2312");
	if(verification_code == null || verification_code.equals("")
		|| ver_name == null || ver_name.equals("")) {
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。验证数据出错了哦... "/> <%
		return;
	}
	User ver_u = UserManager.getUserByName(ver_name);
	if(ver_u == null){
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。用户不存在哦...<a href=register.jsp>立即注册</a> "/> <%
		return;
	}
	if(ver_u.getStatus() != 0){
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。用户已经激活了哦...<a href=login.jsp>立即登陆</a> "/> <%
		return;
	}
	String real_code = ver_u.getVer_code();
	if(real_code.equals(verification_code)) {//验证成功 
		ver_u.setStatus(1);//注册普通用户
		UserManager.updateStatus(ver_u);
		%><html><head><link rel="stylesheet" type="text/css" href="css/global.css">
		<meta http-equiv="refresh" content="3;url=login.jsp"></head><body><center><br/><br/><br/><br/>
		<%
		out.print(ver_name+",注册成功 <br/>谢谢支持V&View ！！！<br/>3秒后跳转到<a href=\"login.jsp\">登陆页面 </a>");
		%></center></body></html><%
		return;
	}else{
		%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。验证码不正确哦...<a href=register.jsp>立即注册</a> "/> <%
		return;
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
<link href="css/global.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/vview.js?ver=2013102801"></script>
<script type="text/javascript" ></script>
<title>V&View(维视)-注册</title>

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
	<form name="register" id="register_form" action="register.jsp" method="post">
		<input type="hidden" name="action" value="register"/>
		<h1 align="center">用户注册</h1>
		<br/><br/>
		<table align="center">
			<tr>
				<td>用&nbsp;户&nbsp;名:</td>
				<td><input name="username" id="username" type="text" maxlength="40" onblur="register_validate(this)"/><span id="username_span">*</span></td>
			</tr>
			<tr>
				<td>密&nbsp;&nbsp;&nbsp;&nbsp;码:</td>
				<td><input name="password1" id="password1" type="password" maxlength="16" onblur="register_validate(this)"/><span id="password1_span">*</span></td>
			</tr>
			<tr>
				<td>确认密码:</td>
				<td><input name="password2" id="password2" type="password" maxlength="16" onblur="register_validate(this)"/><span id="password2_span">*</span></td>
			</tr>
			<tr>
				<td>邮&nbsp;&nbsp;&nbsp;&nbsp;箱:</td>
				<td><input name="email" id="email" type="text" maxlength="40" onblur="register_validate(this)"/><span id="email_span">*</span></td>
			</tr>
			<tr>
				<td>个人站点:</td>
				<td><input name="user_url" type="text" maxlength="40"/></td>
			</tr>
			<tr>
				<td>验证码:</td>
				<td><input type="text" id = "certcode" name="certcode" size="8" onblur="register_validate(this)"/><a href="javascript:;" onclick="changeimg()"><img id="certpic" src="util/makecertpic.jsp" title="点击换一张"/></a><span id="certcode_span">*</span></td>
			</tr>
			<tr align="center">
				<td colspan="2"><input class="button" type="submit" value="立即注册" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="button" value="重置" /></td>
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