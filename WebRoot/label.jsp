<%@page import="vell.bibi.vview.label.LabelManager"%>
<%@page import="vell.bibi.vview.label.Label"%>
<%@page import="vell.bibi.vview.comment.CommentManager"%>
<%@page import="vell.bibi.vview.comment.Comment"%>
<%@page import="vell.bibi.vview.DBSettingManager"%>
<%@page import="vell.bibi.vview.Setting"%>
<%@page import="vell.bibi.vview.category.*"%>
<%@page import="java.util.*" import="vell.bibi.vview.post.*"%>
<%@ page errorPage="errorpage/errpage.jsp" %>
<%@page import="vell.bibi.vview.user.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
username_li = "<a href=\"room.jsp?author_name="+username+"\">"+username+"</a><br/><a href=\"manager.jsp\">管理</a>/<a href=\"logout.jsp\">退出</a>";
}

String label_name = request.getParameter("name");
Label label = LabelManager.getPublishLabelByName(label_name);
if(label == null){
	String uid = request.getParameter("uid");
	label = LabelManager.getPublishLabelByUid(uid);
}
if(label == null){
	%> <jsp:forward page="errorpage/errpage.jsp?mes=亲。没有找到此标签哦... "/> <%
return;
}
%>

<%
List<Post> posts = null;
Iterator<Post> it = null;
Post p = null;
int i=0;
String label_uid = String.valueOf(label.getUid());
int post_info_num = Setting.getPostMaxNum();
String page_num_str = request.getParameter("page");
int page_num = 1;
if(page_num_str != null)  page_num = Integer.parseInt(page_num_str);
int post_num = PostManager.getLabelPostNumByUid(label_uid);
int page_count = 1;
if(post_num % post_info_num == 0)
	page_count = post_num/post_info_num;
else page_count = post_num/post_info_num + 1;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Author" content="VellBibi">
<meta name="Keywords" content="<%=Setting.getvKeywords() %>">
<meta name="Description" content="<%=Setting.getvDescription() %>">
<link href="css/global.css?ver=2013110701" rel="stylesheet" type="text/css">
<link href="css/home.css?ver=2013110701" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.easing-sooper.js"></script>
<script type="text/javascript" src="js/jquery.sooperfish.js"></script>
<script type="text/javascript" src="js/jquery.kwicks-1.5.1.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
    $('#top_topic_ul').kwicks({
      max : 660,
      spacing : 2
    });
    $('ul.sf-menu').sooperfish();
  });
</script>
<title>V&View(维视)-标签</title>

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
			<li class="item" id=""><a href="index.jsp">主页</a></li>
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
	<div class="clear"></div>
</div>
<!-- /nav -->

<!-- content -->
<div class="content">
	<!-- top_topic -->
	<div class="top_topic">
	<ul id="top_topic_ul">
		<%
		posts = PostManager.getPublishLabelPostsByScore(0, 6,label_uid);
			    	it = posts.iterator();
			    	p = null;
		for(i=0; i<6; i++) {
			if(it.hasNext()){
			p = (Post)it.next();
		%>
			<li><div class="top_topic_item">
		    	<div class="post_intro">
		    		<div class="post_img">
		    			<a href="post.jsp?pid=<%=p.getId() %> "><img src="<%=p.getExcerpt_img() %> " alt="post top image error"/></a>
		    		</div>
		    		<div class="post_info">
		    			<h2 class="title"><a href="post.jsp?pid=<%=p.getId() %> "><%=p.getTitle() %></a></h2>
		    			<div class="info_up"> 发布日期：<%=p.getMdate() %>| 点击： <%=p.getClick_count() %> 次| 评论：<%=p.getComment_count() %></div>
		    			<div class="info_down">
		    				<span class="column"> 作者：<a href="room.jsp?author_id=<%=p.getAuthor() %> "><%=UserManager.getUserNameById(String.valueOf(p.getAuthor())) %></a>|栏目：<a href="category.jsp?id=<%=p.getCategory_id() %>"><%=CategoryManager.getCategoryNameById(String.valueOf(p.getCategory_id())) %> </a> </span>
		    				<span class="labels"> 标签：
							<%
							List<Label> ls = LabelManager.getPublishLabelsByPostUid(0,3,String.valueOf(p.getUid()));
							Iterator<Label> lit = ls.iterator();
							Label l = null;
							while(lit.hasNext()){
								l = lit.next();
							%>
							<a href="label.jsp?name=<%=l.getShiftName()%>"><%=l.getName()%></a>
							<%
								}
							%></span>
		    			</div>
		    			<br/>
		    			<div class="post_content">
		    				<%=p.getExcerpt()%>
		    			</div>
		    		</div>
		    	</div>
		    	</div>
	    	</li>
	    	<%
	    		}else{
	    	%>
	        <li><div class="top_topic_item"><img src="images/<%=i+1%>.jpg" alt="top_topic_<%=i+1%>" /></div></li>
	        <%
	        	}
	        	        		}
	        %>
    </ul>
    </div>
    <!-- /top_topic -->
    <div class="posts">
    	<%
    		if(page_num != 0) {
    	    	    	    		posts = PostManager.getPublishLabelPosts(post_info_num*(page_num-1), post_info_num,label_uid);
    	    	    	    	}else {
    	    	    	    		posts = PostManager.getPublishLabelPosts(0,post_info_num,label_uid);
    	    	    	    	}
    	    	    	    	it = posts.iterator();
    	    	    	    	p = null;
    	    	    	    	
    	    	    			for(i=0; it.hasNext(); i++) {
    	    	    		p = (Post)it.next();
    	%>
    	<div class="post_intro">
    		<div class="post_img">
    			<a href="post.jsp?pid=<%=p.getId()%> "><img src="<%=p.getExcerpt_img()%> " alt="post top image error"/></a>
    		</div>
    		<div class="post_info">
    			<h2 class="title"><a href="post.jsp?pid=<%=p.getId()%> "><%=p.getTitle()%></a></h2>
    			<div class="info_up"> 发布日期：<%=p.getPdate()%>| 点击： <%=p.getClick_count()%> 次| 评论：<%=p.getComment_count()%></div>
    			<div class="info_down">
    				<span class="column"> 作者：<a href="room.jsp?author_id=<%=p.getAuthor()%> "><%=UserManager.getUserNameById(String.valueOf(p.getAuthor()))%></a>|栏目：<a href="category.jsp?id=<%=p.getCategory_id()%>"><%=CategoryManager.getCategoryNameById(String.valueOf(p.getCategory_id()))%> </a> </span>
    				<span class="labels"> 标签：
					<%
    					List<Label> ls = LabelManager.getPublishLabelsByPostUid(0,3,String.valueOf(p.getUid()));
    				    							Iterator<Label> lit = ls.iterator();
    				    							Label l = null;
    				    							while(lit.hasNext()){
    				    								l = lit.next();
    				%>
					<a href="label.jsp?name=<%=l.getShiftName()%>"><%=l.getName()%></a>
					<%
						}
					%></span>
    			</div>
    			<br/>
    			<div class="post_content">
    				<%=p.getExcerpt()%>
    			</div>
    		</div>
    	</div>
    	<%
    		}
    	%>
    </div>
    <div class="content_left">
    	<div class="notice" align="center">
    		<%=DBSettingManager.getNotice()%>
    	</div>
    	
    	<div class="new_comments">
    		<h3 align="center">最新评论</h3>
    		<ul>
    			<%
    				List<Comment> comments = null;
    			    			    			comments = CommentManager.getCommentsByCdate(0, 6);
    			    			    			Iterator<Comment> cm_it = comments.iterator();
    			    			    			Comment cm = null;
    			    			    			p = null;
    			    			    			while(cm_it.hasNext()){
    			    			    				cm = cm_it.next();
    			    			    				p = PostManager.getPostById(String.valueOf(cm.getPost_id()));
    			%>
    			<li class="new_comments_li"><a class="new_comments_title" href="post.jsp?pid=<%=cm.getPost_id()%>" ><%="《"+p.getTitle()+"》"%></a></br>
    			<a href="post.jsp?pid=<%=cm.getPost_id()%>#comment_<%=cm.getId()%>"><%=cm.getAuthor()+" : "+cm.getContent()%> </a></li>
    			<%
    				}
    			%>
    		</ul>
    	</div>
    	<div class="labels_show">
    		<h3 align="center">热门标签</h3>
    		<div class="labels" id="labels" ></div>
    		<%
    			String labels_xml = "";
    		    			List<Label> list = LabelManager.getPublishLabels();
    		    			Iterator<Label> l_it = list.iterator();
    		    			Label l = null;
    		    			
    		    			labels_xml += "<labels>";
    		    			while(l_it.hasNext()){
    		    				l = l_it.next();
    		    				if(l.getName() == null || l.getName().equals("")) continue;
    		    				labels_xml += "<label><name>"+l.getName()+"</name>"+
    		    				"<id>"+l.getId()+"</id>"+
    		    				"<uid>"+l.getUid()+"</uid></label>";
    		    			}
    		    			labels_xml += "</labels>";
    		%>
			<script type="text/javascript">
    		$(document).ready(function(){
    			labels_xml = "<%=labels_xml %>";
	
	   			if (window.DOMParser){
		   			parser=new DOMParser();
		   			xmlDoc=parser.parseFromString(labels_xml,"text/xml");
	   			}else{ // Internet Explorer
		   			xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		   			xmlDoc.async="false";
		   			xmlDoc.loadXML(labels_xml);
	   			}
	   			var names = xmlDoc.getElementsByTagName("name");
	   			var label_html = "";
	   			var i = 0;
	   			for(i=0; i<names.length; i++){
	   				name = names[i].childNodes[0].nodeValue;
	   				label_html += "<span class=\"label_span\" ><a target=\"_blank\" href=\"label.jsp?name="+name+"\">"+name+"</a></span>";
	   			}
	   			document.getElementById("labels").innerHTML = label_html;
	   			rand_style();
    		});
    		function rand_style(){
    			var label_span = $(".label_span");
    			label_span.each(function () {
                    var x = 15;
                    var y = 0;
                    var rand = parseInt(Math.random() * (x - y + 1) + y);
                    $(this).addClass("label" + rand);
                });
    		}
    		</script>
    	</div>
    </div>
    <div class="page_navi clear">
    <% if(page_count > 1) {%>
	    <% if(page_num != 1){ %>
		    <a title="跳转到第一页" href="index.jsp?page=<%=1 %> " class="extend"> 第一页 </a>
		<% } %>
		<% if(page_num-2 > 1){ %>
			<a href="index.jsp?page=<%=page_num-1 %> ">...</a>
		<% } %>
	    <% for(i=page_num-2; i<=page_count && i<=page_num+2; i++){ 
	    	if(i>0) {
	    %>
		    <a href="index.jsp?page=<%=i %> " <% if(page_num == i) out.print("class=\"current \""); %>><%=i %></a>
		<%  }
	       } %>
	    <% if(i>page_num+2){ %>
			<a href="index.jsp?page=<%=page_num+1 %> ">...</a>
		<% } %>
		<% if(page_num < page_count){ %>
			<a href="index.jsp?page=<%=page_num+1 %> "> 下一页 </a>
		    <a title="跳转到最后一页" href="index.jsp?page=<%=page_count %> " class="extend"> 最后一页 </a>
		<% } 
	   }%>
    </div>
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