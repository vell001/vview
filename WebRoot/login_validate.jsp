<%@page import="vell.bibi.vview.user.User"%>
<%@page import="vell.bibi.vview.user.UserManager"%>
<%@page import="vell.bibi.vview.util.AjaxUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.PrintWriter"%>
<%
response.setContentType("text/xml");
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control", "no-store");//http 1.1
response.setHeader("Pragma", "no-cache");//http 1.0
response.setDateHeader("Expires", 0);//no cache
response.setHeader("Charset","UTF-8");

PrintWriter ajax_out = response.getWriter();
String id = AjaxUtil.unescape(request.getParameter("id"));
String value =  AjaxUtil.unescape(request.getParameter("value"));
String return_text = "";
%>
<%
if(id != null && id.equals("username")){//处理用户名
	if(value == null || id.trim().equals("")){//用户名为空 
		return_text = "用户名不能为空 ";
	}else{//填写正确 
		if(UserManager.getUserByName(value.trim()) == null){//用户不存在 
			return_text = "用户不存在，<a href=register.jsp>立即注册?</a>";
		}else{
			return_text = "ok...";
		}
	}
}else if(id != null && id.equals("password")){//密码
	if(value == null || id.trim().equals("")){
		return_text = "密码不能为空 ";
	}else{//填写正确 
		String username = AjaxUtil.unescape(request.getParameter("username"));
		User u = UserManager.getUserByName(username.trim());
		if(u == null){
			return_text = "用户不存在，<a href=register.jsp>立即注册?</a>";
		}else if(u.getPassword().equals(value.trim())){
			return_text = "ok...";
		}
	}
}else if(id != null && id.equals("certcode")){//处理验证码
	if(value == null || id.trim().equals("")){
		return_text = "验证码不能为空 ";
	}else{//填写正确 
		if(!value.equals((String)session.getAttribute("certcode"))){
			return_text = "验证码错误";
		}else{
			return_text = "ok...";
		}
	}
}
%>
<%ajax_out.write(return_text); %>