package vell.bibi.vview.mail;


public class VMail {
//	private String mail_from;
	private String mail_to;
	private String content;//内容
	private String subject;//主题
//	private String filename;//附件
	public String getMail_to() {
		return mail_to;
	}
	public void setMail_to(String mail_to) {
		this.mail_to = mail_to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
