<%@page import="vell.bibi.vview.label.LabelManager"%>
<%@page import="vell.bibi.vview.label.Label"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="vell.bibi.vview.comment.CommentManager"%>
<%@page import="vell.bibi.vview.comment.Comment"%>
<%@page import="vell.bibi.vview.Setting"%>
<%@page import="vell.bibi.vview.category.*"%>
<%@page import="vell.bibi.vview.RightManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="vell.bibi.vview.post.*"%>
<%@page import="java.util.*"%>
<%@page import="vell.bibi.vview.user.*"%>
<%@ page errorPage="errorpage/errpage.jsp" %>
<%
	request.setCharacterEncoding("UTF-8");
//判断登陆
Cookie[] cookies = request.getCookies();
String username = (String)session.getAttribute("username");
String username_li = null;
User u = null;
if(username == null && !UserManager.isLogin(cookies)){//登陆失败
	username_li = "<a href=\"register.jsp\">加入我们</a><br/><a href=\"login.jsp\">登陆</a>";
}
else {//登陆成功
	if(username == null) username = UserManager.getCookiesValue(cookies, "username");
	username_li = "<a href=\"room.jsp?author_name="+username+"\">"+username+"</a><br/><a href=\"manager.jsp\">管理</a>/<a href=\"logout.jsp\">退出</a>";
	u = UserManager.getUserByName(username);
}


String pid = request.getParameter("pid");
Post p = PostManager.getPostById(pid);
if(p == null){//试试uid访问
	String uid = request.getParameter("uid");
	p = PostManager.getPostByUid(uid);
}
if(!RightManager.couldViewPost(username, p)){//都不行。。。
%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。你访问的页面不存在。。。 "/> <%
}
String a_editor = RightManager.couldEditPost(username, p)?"&nbsp;【<a href=\"editor/index.jsp?uid="+p.getUid()+"\">编辑</a>/<a href=\"delete_post.jsp?pid="+p.getId()+"\">删除</a>】" :"";
if(p.getStatus()==0) a_editor = a_editor+"【本文未发布】";
//改变点击量
PostManager.addClickCount(p);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Author" content="VellBibi">
<meta name="Keywords" content="<%=Setting.getvKeywords() %>">
<meta name="Description" content="<%=Setting.getvDescription() %>">
<link href="css/global.css?ver=2013110701" rel="stylesheet" type="text/css">
<link href="css/post.css?ver=2013110701" rel="stylesheet" type="text/css">
<link href="css/comment.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/vview.js"></script>
<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js" type="text/javascript" charset="utf-8"></script>
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
			Category nav_c = null;
			String nav_id = "";
			while(nav_c_it.hasNext()){
				nav_c = (Category)nav_c_it.next();
				if(p.getCategory_id() == nav_c.getId()) nav_id = "nav_active";
				else nav_id = "";
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
	<div class="clear"></div>
</div>
<!-- /nav -->

<!-- content -->
<div class="content">
	<!-- post -->
	<div class="post">
		<h2 class="title"><%=p.getTitle() %></h2>
		<div class="info">
			<div class="info_item"> 
				作者：<a href="room.jsp?author_id=<%=p.getAuthor() %> "><%=UserManager.getUserNameById(String.valueOf(p.getAuthor())) %></a>| 
				栏目：<a href="category.jsp?id=<%=p.getCategory_id() %>"><%=CategoryManager.getCategoryNameById(String.valueOf(p.getCategory_id())) %> </a> 
				<%=a_editor %>
			</div>
			<div class="info_item"> 
				发布日期：<%=p.getMdate() %>| 点击： <%=p.getClick_count() %> 次| 评论：<%=p.getComment_count() %> 
			</div>
			<div class="info_item">
				标签：
				<%
				List<Label> ls = LabelManager.getPublishLabelsByPostUid(String.valueOf(p.getUid()));
				Iterator<Label> lit = ls.iterator();
				Label l = null;
				while(lit.hasNext()){
					l = lit.next();
				%>
				<a href="label.jsp?name=<%=l.getShiftName()%>"><%=l.getName()%></a>
				<%
				}
				%>
			</div>
		</div>
		<br/>
	    <div class="post_content">
	    	<%=p.getContent() %>
	    </div>
    </div>
    <!-- end post -->
    <div class="share_div">
    <!-- Baidu Button BEGIN -->
	<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
	<span class="bds_more">分享到：</span>
	<a class="bds_qzone"></a>
	<a class="bds_tsina"></a>
	<a class="bds_tqq"></a>
	<a class="bds_renren"></a>
	<a class="bds_t163"></a>
	<a class="shareCount"></a>
	</div>
	<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=243912" ></script>
	<script type="text/javascript" id="bdshell_js"></script>
	<script type="text/javascript">
	document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000)
	</script>
	<!-- Baidu Button END -->
	</div>
    <!-- comment -->
    <%
    if(p.getComment_status()==1){
    	%><p class="resp"><a href="#comment_0" onclick="move_form('0')">回复</a></p><%
	    String content = "";
	    content = CommentManager.printSonComment("0", p,content,u);
	    out.print(content);
    %>
    <div id="0" >
	    <div id="form_div">
	    	<a name="comment_0"></a>
		    <form class="comment_form" id="comment_form" action="comment.jsp" method="post">
		   		<p class="cm_name"><input name="author" type="text" value="<%=u==null ? "" : u.getUsername() %>"/>姓名 (必须填写)</p>
		   		<p class="cm_email"><input name="author_email" type="text" value="<%=u==null ? "" : u.getEmail() %>"/>电子邮件 (必须填写)</p>
		   		<p class="cm_url" ><input name="author_url" type="text" value="<%=u==null ? "" : u.getUser_url() %>"/>你的个人站点</p>
		   		<%
		   		if(u == null){
		   			%><p><input type="text" id="certcode" name="certcode" size="8"/><a href="javascript:;" onclick="changeimg()"><img id="certpic" src="util/makecertpic.jsp" title="换一张 "/></a></p><%
		   		}
		   		%>
		   		<p ><textarea class="cm_content" name="content" rows="8" cols="45"></textarea></p>
		   		<input class="button" name="submit" type="submit" value="提交"/>&nbsp;&nbsp;
				<input id="parent_id" type="hidden" name="parent_id" value="0"/>
				<input type="hidden" name="post_id" value="<%=p.getId() %>"/>
				<input type="hidden" name="user_id" value="<%=u!=null?u.getId():0 %>"/>
				<input class="notice" type="checkbox" name="notice" value="1" checked="checked" />有人回复时邮件通知我
		   	</form>
	   	</div>
   	</div>
   	<%
    }else {
    	%><h4 align="center">***本文章禁止评论***</h4><%
    }
   	%>
    <!-- end comment -->
    <div class="clear"></div>
</div>
<!-- /content -->

<!-- footer -->
<div class="footer">
	<div class="copyright">COPYRIGHT &copy; 2013 <a href="index.jsp" target="_blank">V&View维视</a> ALL RIGHT RESERVED</div>
	<div class="clear"></div>
</div>
<!-- /footer -->
</div>
</body>
</html>