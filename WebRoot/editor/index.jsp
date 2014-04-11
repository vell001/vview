<%@page import="sun.rmi.runtime.Log"%>
<%@page import="vell.bibi.vview.label.LabelForPostManager"%>
<%@page import="vell.bibi.vview.label.Label"%>
<%@page import="vell.bibi.vview.label.LabelManager"%>
<%@page import="java.awt.image.TileObserver"%>
<%@page import="vell.bibi.vview.category.*"%>
<%@page import="java.util.*"%>
<%@page import="vell.bibi.vview.*"%>
<%@page import="vell.bibi.vview.util.UID"%>
<%@page import="vell.bibi.vview.user.*"%>
<%@page import="vell.bibi.vview.post.*"%>
<%@ page errorPage="../errorpage/errpage.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
User u = null;
Post p = null;
String labels_input = "";
String action = request.getParameter("action");
String uid = request.getParameter("uid");
Cookie[] cookies = request.getCookies();
boolean isnew = false;
String username = (String)session.getAttribute("username");
if(username == null && !UserManager.isLogin(cookies)){//登陆失败
	response.sendRedirect("../login.jsp");
	return;
}else {//登陆成功
	if(username == null && UserManager.isLogin(cookies)){
		username = UserManager.getCookiesValue(cookies, "username");
	}
	u = UserManager.getUserByName(username);
	if(!RightManager.couldPublishPost(u)) {//验证发帖权限
		%> <jsp:forward page="../errorpage/errpage.jsp?mes=亲。你暂时发帖权限哦。。。 "/> <%
		return;
	}
	if(uid != null){
		p = PostManager.getPostByUid(uid); //找到要编辑的文章
		if(p == null){//正在编辑的新文件
			p = new Post();
			p.setUid(Long.parseLong(uid));
		}else{//编辑旧文章
			if(!RightManager.couldEditPost(u, p)) {
				%> <jsp:forward page="../errorpage/errpage.jsp?mes=亲。你没有此权限哦。。。 "/> <%
 				return;
 		 	}
 		}
 		if(action != null){//保存旧文章
 	 		String content = request.getParameter("editor_content") != null ? request.getParameter("editor_content") : "";
 	 		String title = request.getParameter("title");
 	 		String category_id = request.getParameter("category");
 	 		String comment_status = request.getParameter("comment_status");
 	 		String[] labels_checkbox = request.getParameterValues("labels_checkbox");
 	 		labels_input = request.getParameter("labels_input");
 	 		
 	 		/* 保存p */
 	 		p.setContent(content!=null?content.trim()!=""?content:"":"");//内容
 	 		p.setTitle(title!=null?(title.trim()!="" || title.trim()!=null)?title.trim():"无题":"无题");//标题
 	 		p.setCategory_id(Integer.parseInt(category_id));
 	 		p.setComment_status(comment_status!=null?Integer.parseInt(comment_status) == 1?1:0:0);
 	 		if(p.getAuthor() == 0) p.setAuthor(u.getId());
 	 		if(action.equals("publish") || p.getStatus() == 1) {//发布
 	 			p.setStatus(1);
 	 		}
 	 		else p.setStatus(0);
 	 		p = PostManager.savePost(p);//完成pdate mdate excerpt excerpt_img
 	 		/* 标签处理  */
 	 		if(labels_input!=null && !labels_input.trim().equals("")){//保存新建的labels
 	 			LabelManager.saveLabelsFormString(labels_input);
 	 		}
 	 		String labels_string = labels_input;
 	 		if(labels_checkbox != null){
	 	 		for(int i=0; i<labels_checkbox.length; i++){
	 	 			labels_string = labels_string + " " + labels_checkbox[i];
	 	 		}
	 	 	}
 	 		LabelForPostManager.updateLabelsForPost(labels_string, Long.valueOf(uid));//更新 
 	 		
 	 		if(action.equals("publish")) {//发布
 %>
	 			<html><head><link rel="stylesheet" type="text/css" href="../css/global.css">
	 			<meta http-equiv="refresh" content="3;url=../post.jsp?uid=<%=p.getUid() %>"></head><body><center><br/><br/><br/><br/>
				<h2 style="color: blue">《<%=p.getTitle() %>》发布成功<br/>3秒后跳转到<a href="../post.jsp?uid=<%=p.getUid() %>">文章</a></h2>
				</center></body></html>
				<%
				return;
	 		}
	 	}
	}else{//创建新文章
		p = new Post();
		isnew = true;
		uid = String.valueOf(p.getUid());
	}
	
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../css/editor.css" rel="stylesheet" type="text/css">
<link href="../css/global.css" rel="stylesheet" type="text/css">
<title>VEditor</title>
<link rel="stylesheet" href="themes/default/default.css" />
<script charset="utf-8" src="editor.js"></script>
<script charset="utf-8" src="lang/zh_CN.js"></script>
<script type="text/javascript" src="../js/editor.js"></script>
<script type="text/javascript" src="../js/jquery.js"></script>
</head>
<body>
<div class="body_div">
<!-- header -->
<div class="header">
  <div class="header_main">
  	<div class="logo_div"> 
    	<a href="../index.jsp"><img class="logo_img" height="80px" border="0" src="../images/logo.png" /> </a>
    </div>
    <div class="top_menu_div">
    	<ul class="top_menu_ul">
    		<li class="single" id="username"><a href="../room.jsp?author_name=<%=u.getUsername() %> "><%=u.getUsername() %></a></li>
    		<li class="single" id=""><a href="">订阅</a></li>
    		<li class="single" id=""><a href="index.jsp">投稿</a></li>
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
			<li class="item" id=""><a href="../index.jsp">主页</a></li>
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
				<li class="item" id="<%=nav_id %>"><a href="../category.jsp?id=<%=nav_c.getId() %>"><%=nav_c.getName() %></a></li>
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
	<form name="editor" method="post" action="index.jsp">
		<h2>标题:&nbsp;<input id="title_input" type="text" name="title" maxlength="100" value="<%=p.getTitle()!=null?p.getTitle():"" %> " /></h2>
		<textarea name="editor_content" class="editor_content" cols="100" rows="8"><%=htmlspecialchars(p.getContent()!=null?p.getContent():"")%></textarea>
		<br/>
		<div class="editor_op" align="center">
			选择分类：
			<select class="category_select" name="category" id="category" >
				<%
				List<Category> select_c_list = CategoryManager.getAllCategory();
				Iterator<Category> select_c_it = select_c_list.iterator();
				Category select_c = null;
				String selected = "";
				while(select_c_it.hasNext()){
					select_c = (Category)select_c_it.next();
					if(p.getCategory_id() == select_c.getId()) selected = "selected=\"selected\"";
					else if(p.getCategory_id()==0 && select_c.getId() == select_c_list.size()) selected = "selected=\"selected\"";
					else selected = "";
					%><option value="<%=select_c.getId() %>" <%=selected %>><%=select_c.getName() %></option><%
				}
				%>
			</select>
			标签：<input name="labels_input" class="labels_input" id="labels_input" type="text" size="40" value="<%=labels_input %>"/>输入你的标签，空格分开 <a href="javascript:;" onclick="close_labels(this)">【收起已有标签】</a>
			<div class="labels_div" name="labels_div" id="labels_div" style="display:inline-block">
				<h3>已有标签：</h3>
				<%
				List<Label> l_list = LabelManager.getPublishLabels();
				Iterator<Label> l_it = l_list.iterator();
				Label l = null;
				String check_string = "";
				while(l_it.hasNext()){
					l = l_it.next();
					if(LabelForPostManager.belongToPostByUid(String.valueOf(l.getUid()), uid)){
						check_string = "checked=\"checked\"";
					}else{
						check_string = "";
					}
				%>
				<span class="label_span" name="label_span"><input type="checkbox" name="labels_checkbox" class="labels_checkbox" value="<%=l.getName()%>" <%=check_string%> /><%=l.getName()%></span>
				<%
				} 
				%>
			</div>
			<script type="text/javascript">
			$(".labels_div").ready(function(){
				var labels_checkbox = $(".labels_checkbox").toArray();
				var label_spans = $(".labels_checkbox").parent(".label_span").toArray();
				for(var i=0; i<labels_checkbox.length; i++){
					if(labels_checkbox[i].checked.toString() == "true"){
						label_spans[i].setAttribute("style","background-color:rgb(231, 235, 242)");
					}
				}
			});
			$(".label_span").mouseover(function(){
				this.setAttribute("style","background-color:rgb(231, 235, 242)");
			});
			$(".label_span").mouseout(function(){
				if(this.childNodes[0].checked.toString() != "true"){
					this.setAttribute("style","background-color:#fff");
				}
			});
			</script>
			<br/>
			<input name="comment_status" type="checkbox" value="1" <%=p.getComment_status()==1 || isnew?"checked=\"checked\"":"" %>/>&nbsp;允许评论
			<br/><br/>
			<input class="button" type="button" name="save" value="保存" />&nbsp;&nbsp;&nbsp;
			<!-- <input class="button" type="button" name="preview" value="预览" />&nbsp;&nbsp;&nbsp; -->
			<input class="button" type="button" name="publish" value="发布" />
			<br/><br/>
			<h5>您当前输入了 <span class="word_count">0</span> 个文字。</h5>
			<h4>小提示:ctrl+s = 保存</h4>
		</div>
		<input type="hidden" value="<%=uid %>" name="uid"/>
		<input type="hidden" value="save" name="action" id="action"/>
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
<%
}
%>