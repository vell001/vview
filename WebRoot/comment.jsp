<%@page import="vell.bibi.vview.util.IPUtil"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="vell.bibi.vview.comment.Comment"%>
<%@page import="vell.bibi.vview.Setting"%>
<%//@ page errorPage="errorpage/errpage.jsp" %>
<%@page import="vell.bibi.vview.category.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" import="vell.bibi.vview.post.*"%>
<%@page import="vell.bibi.vview.user.*"%>
<%!
private String htmlspecialchars(String str) {
	str = str.replaceAll("&", "&amp;");
	str = str.replaceAll("<", "&lt;");
	str = str.replaceAll(">", "&gt;");
	str = str.replaceAll("\"", "&quot;");
	return str;
}
%>
<%
request.setCharacterEncoding("UTF-8");
Cookie[] cookies = request.getCookies();
String username = (String)session.getAttribute("username");
String username_li = null;
boolean islogin = false;
if(username == null && !UserManager.isLogin(cookies)){//登陆失败
	username_li = "<a href=\"register.jsp\">加入我们</a><br/><a href=\"login.jsp\">登陆</a>";
}
else {//登陆成功
	islogin = true;
	if(username == null) username = UserManager.getCookiesValue(cookies, "username");
	username_li = "<a href=\"room.jsp?author_name="+username+"\">"+username+"</a><br/><a href=\"manager.jsp\">管理</a>/<a href=\"logout.jsp\">退出</a>";
}
%>
<%
String post_id = request.getParameter("post_id");
String parent_id = request.getParameter("parent_id");
String notice = request.getParameter("notice");
String author = request.getParameter("author");
String author_email = request.getParameter("author_email");
String author_url = request.getParameter("author_url");
String content = request.getParameter("content");
String user_id = request.getParameter("user_id");
String certcode=request.getParameter("certcode");
if(!islogin && (certcode == null || !certcode.equals((String)session.getAttribute("certcode")))){
	%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。验证码输入有误哦... <br/>请返回输入验证码。。。"/> <%
}
if( post_id == null || parent_id == null || author == null || author_email == null || content == null
	|| post_id.equals("") || parent_id.equals("") || author.equals("") || author_email.equals("") || content.equals("")){
	%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你的评论没有成功提交哦... <br/>请检查你的内容是否完整。。。"/> <%
}
Comment cm = new Comment();
cm.setAuthor(author);
cm.setAuthor_email(author_email);
cm.setAuthor_url(author_url);
cm.setCdate(new Timestamp(System.currentTimeMillis()));
cm.setContent(htmlspecialchars(content));
cm.setNotice(notice!=null? Integer.parseInt(notice): 0);
cm.setPost_id(Long.parseLong(post_id));
cm.setParent_id(Long.parseLong(parent_id));
cm.setAuthor_ip(IPUtil.getIpAddr(request));
cm.setUser_id(Long.parseLong(user_id));
cm.setComment_img(Setting.getDefaultUserImgSrc());
cm.setStatus(1);
cm.saveToDB();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Author" content="VellBibi">
<meta name="Keywords" content="<%=Setting.getvKeywords() %>">
<meta name="Description" content="<%=Setting.getvDescription() %>">
<meta http-equiv="refresh" content="3;url=post.jsp?pid=<%=post_id %>">
<link href="css/global.css?ver=2013110701" rel="stylesheet" type="text/css">
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
			<li class="item"><a href="index.jsp">主页</a></li>
			<%
			List<Category> nav_c_list = CategoryManager.getAllCategory();
			Iterator nav_c_it = nav_c_list.iterator();
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
<div class="content" align="center">
	<br/><br/><br/><br/><br/><br/><br/><br/>
	<h2 >
	<div style="color: blue">。。。提交成功。。。</div>
	内容如下:<div style="color: blue"><%=cm.getContent() %></div>
	</h2>
	<br/>
	3秒后<a href="post.jsp?pid=<%=post_id %>">返回</a>
    <div class="clear"></div>
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