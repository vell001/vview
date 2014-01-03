package vell.bibi.vview.mail;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vell.bibi.vview.Setting;
import vell.bibi.vview.comment.Comment;
import vell.bibi.vview.comment.CommentManager;
import vell.bibi.vview.post.Post;
import vell.bibi.vview.post.PostManager;
import vell.bibi.vview.user.User;
import vell.bibi.vview.user.UserManager;

public class VMailManager {
	
	public static boolean sendCommentRemindToParent(Comment cm){
		if(cm == null) return false;
		if(cm.getParent_id() == 0) return false;
		cm = CommentManager.getCommentByUid(String.valueOf(cm.getUid()));
		//得到父类
		Comment parent = CommentManager.getCommentById(String.valueOf(cm.getParent_id()));
		if(parent.getNotice() == 0) return false; //设置不提醒
		
		VMail m = new VMail();
		m.setSubject("你的评论在V&View(维视)上有一条新回复");
		String content = "你的评论在V&View(维视)上有一条新回复,回复内容如下：<br/>" +
				"作者：" + cm.getAuthor() + "<br/>" +
				"作者邮箱：" + cm.getAuthor_email() + "<br/>" +
				"作者站点：" + cm.getAuthor_url() + "<br/>" +
				"作者ip：" + cm.getAuthor_ip() + "<br/>" +
				"回复内容：" + cm.getContent() + "<br/>" +
				"<a href=\"" + Setting.getBasepath()+"post.jsp?pid="+cm.getPost_id()+"#comment_"+cm.getId()+"\" target=\"_blank\">点击回复</a>" +
				"<br/><br/><br/><br/>" +
				"COPYRIGHT &copy; 2013 <a href=\""+Setting.getBasepath()+"index.jsp\" target=\"_blank\">V&View维视</a> ALL RIGHT RESERVED</div>";
		m.setContent(content);
		m.setMail_to(parent.getAuthor_email());
		VMailManager.sendByHtml(m);
		return true;
	}
	
	public static boolean sendCommentRemindToPostAuthor(Comment cm){
		if(cm == null) return false;
		cm = CommentManager.getCommentByUid(String.valueOf(cm.getUid()));
		//得到父类
		Post p = PostManager.getPostById(String.valueOf(cm.getPost_id()));
		User u = UserManager.getUserById(String.valueOf(p.getAuthor()));
		
		VMail m = new VMail();
		m.setSubject("你的文章《"+p.getTitle()+"》在V&View(维视)上有一条新评论");
		String content = "你的文章《"+p.getTitle()+"》在V&View(维视)上有一条新评论,评论内容如下：<br/>" +
				"作者：" + cm.getAuthor() + "<br/>" +
				"作者邮箱：" + cm.getAuthor_email() + "<br/>" +
				"作者站点：" + cm.getAuthor_url() + "<br/>" +
				"作者ip：" + cm.getAuthor_ip() + "<br/>" +
				"评论内容：" + cm.getContent()  + "<br/>" +
				"<a href=\"" + Setting.getBasepath()+"post.jsp?pid="+cm.getPost_id()+"#comment_"+cm.getId()+"\" target=\"_blank\">点击回复</a>" +
				"<br/><br/><br/><br/>" +
				"COPYRIGHT &copy; 2013 <a href=\""+Setting.getBasepath()+"index.jsp\" target=\"_blank\">V&View维视</a> ALL RIGHT RESERVED</div>";
		m.setContent(content);
		m.setMail_to(u.getEmail());
		VMailManager.sendByHtml(m);
		return true;
	}
	
	public static boolean sendUserVerificationCode(User u) {
		if(u == null) return false;
		
		VMail m = new VMail();
		m.setSubject("欢迎注册V&View(维视)");
		String content = null;
		try {
			content = "<center>" +
					"<h2>欢迎注册<a href=\""+Setting.getBasepath()+"index.jsp\">V&View(维视)</a></h2>" +
					"你的详细信息如下：<br/>" +
					"用户名：" + u.getUsername() + "<br/>" +
					"邮箱：" + u.getEmail() + "<br/>" +
					"如果信息无误，请点击：" +
					"<a href=\""+Setting.getBasepath()+"register.jsp?" +
							"verification_code="+u.getVer_code()+
							"&action=verification" +
							"&ver_name="+URLEncoder.encode(u.getUsername(),"gb2312")+
							"\" target=\"_blank\">确认注册</a>" +
					"</center>";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.setContent(content);
		m.setMail_to(u.getEmail());
		VMailManager.sendByHtml(m);
		return true;
	}
	
	public static boolean sendUserRegisterToAdmin(User u) {
		if(u == null) return false;
		
		VMail m = new VMail();
		m.setSubject("有新用户注册V&View(维视)");
		String content = "<center>" +
				"<h2>有新用户注册<a href=\""+Setting.getBasepath()+"index.jsp\">V&View(维视)</a></h2>" +
				"详细信息如下：<br/>" +
				"用户名：" + u.getUsername() + "<br/>" +
				"邮箱：" + u.getEmail() + "<br/>" +
				"</center>";
		m.setContent(content);
		m.setMail_to(u.getEmail());
		VMailManager.sendByHtml(m);
		return true;
	}
	
	public static boolean sendByHtml(VMail m){
		Properties props = Setting.getEmailProps();
        Session s = Session.getInstance(props);
	    // 为了查看运行时的信息
        s.setDebug(true);
        // 由邮件会话新建一个消息对象
        MimeMessage message = new MimeMessage(s);
        try {
	        // 发件人
	        InternetAddress from = new InternetAddress(Setting.getMailFrom());
			message.setFrom(from);
			// 收件人
            InternetAddress to = new InternetAddress(m.getMail_to());
            message.setRecipient(Message.RecipientType.TO, to);
            // 邮件标题
            message.setSubject(m.getSubject());
            // 邮件内容,也可以使纯文本"text/plain"
            message.setContent(m.getContent(), "text/html;charset=utf-8");
            message.saveChanges();
            
            Transport transport = s.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(props.getProperty("mail.smtp.host"), Setting.getMailUser(), Setting.getMailPassword());
            
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] a){
//		sendVerificationCode(UserManager.getUserByName("vell001"));
		User u = UserManager.getUserByName("我是谁");
		String content = null;
		try {
			content = "<center>" +
					"<h2>欢迎注册<a href=\""+Setting.getBasepath()+"index.jsp\">V&View(维视)</a></h2>" +
					"你的详细信息如下：<br/>" +
					"用户名：" + u.getUsername() + "<br/>" +
					"邮箱：" + u.getEmail() + "<br/>" +
					"如果信息无误，请点击：" +
					"<a href=\""+Setting.getBasepath()+"register.jsp?" +
							"verification_code="+u.getVer_code()+
							"&action=verification" +
							"&ver_name="+URLEncoder.encode(u.getUsername(),"gb2312")+
							"\" target=\"_blank\">确认注册</a>" +
					"</center>";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println(content);
			try {
				System.out.println(URLDecoder.decode("%CE%D2%CA%C7%CB%AD","gb2312"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	
	}
}
