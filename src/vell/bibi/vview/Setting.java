package vell.bibi.vview;

import java.util.*;

public class Setting {
	private static final int post_max_num = 10;
	private static final int post_excerpt_num = 100;
	private static final String admin_email = "vell001@qq.com";
	private static final String des_key = "";
	private static final String v_keywords = "V&View(维视),V&View,维视,vview,vell001,VellBibi";
	private static final String v_description = "V&View(维视),一群屌丝的世界观。。。主要是IT方面的感想、分享";
	private static final String notice = 
			"<h1>V&View(维视)</h1>" + 
    		"<br/><h2>一群屌丝的《世界观》</h2>" +
    		"<br/>注明：本网站正处于公开测试阶段，暂不提供注册，没有启用邮箱提醒，更多功能敬请期待";
	private static final String basepath = "http://vview.vell001.ml/";
//	private static final String basepath = "http://localhost:8080/VView/";
	
	private static final String mail_from = "";
	private static final String mail_user = "";
	private static final String mail_password = "";
	
	public static Properties getEmailProps() {
		Properties email_props = System.getProperties();
		// 设置smtp服务器
		email_props.setProperty("mail.smtp.host", "smtp.qq.com");
        // 现在的大部分smpt都需要验证了
		email_props.put("mail.smtp.auth", "true");
		return email_props;
	}
	public static String getDefaultExcerptImgSrc() {
		return basepath+"/images/post_img/post_img ("+(int)(16*Math.random()+1)+").jpg";
	}
	public static String getDefaultUserImgSrc() {
		return basepath+"/images/head_img/head_img ("+(int)(19*Math.random()+1)+").jpg";
	}
	
	public static String getAdminEmail() {
		return admin_email;
	}
	public static int getPostMaxNum() {
		return post_max_num;
	}
	public static int getPostExcerptNum() {
		return post_excerpt_num;
	}
	public static String getBasepath() {
		return basepath;
	}
	public static String getMailFrom() {
		return mail_from;
	}
	public static String getMailUser() {
		return mail_user;
	}
	public static String getMailPassword() {
		return mail_password;
	}
	public static String getDesKey() {
		return des_key;
	}
	public static String getvKeywords() {
		return v_keywords;
	}
	public static String getvDescription() {
		return v_description;
	}
	public static String getNotice() {
		return notice;
	}
	
	public static void main(String[] a){
		System.out.println(Setting.getDefaultExcerptImgSrc());
		System.out.println(Setting.getDefaultUserImgSrc());
	}
}
