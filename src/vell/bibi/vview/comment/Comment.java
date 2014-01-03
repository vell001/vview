package vell.bibi.vview.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import vell.bibi.vview.mail.VMailManager;
import vell.bibi.vview.post.PostManager;
import vell.bibi.vview.util.DB;
import vell.bibi.vview.util.UID;

public class Comment {
	private long id;
	private long post_id;
	private String author;
	private String author_email;
	private String author_url;
	private String author_ip;
	private Timestamp cdate;
	private String content;
	private int status;
	private long parent_id;
	private long user_id;
	private int notice;
	private String comment_img;
	private long uid;
	public Comment(){
		this.uid = UID.next();
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getComment_img() {
		return comment_img;
	}
	public void setComment_img(String comment_img) {
		this.comment_img = comment_img;
	}
	public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPost_id() {
		return post_id;
	}
	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor_email() {
		return author_email;
	}
	public void setAuthor_email(String author_email) {
		this.author_email = author_email;
	}
	public String getAuthor_url() {
		return author_url;
	}
	public void setAuthor_url(String author_url) {
		this.author_url = author_url;
	}
	public String getAuthor_ip() {
		return author_ip;
	}
	public void setAuthor_ip(String author_ip) {
		this.author_ip = author_ip;
	}
	public Timestamp getCdate() {
		return cdate;
	}
	public void setCdate(Timestamp cdate) {
		this.cdate = cdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	public void saveToDB(){
		Connection conn = DB.getConn();
		String sql = "insert into comment values (null, ?, ?, ?, ?, ?," +
				"?, ?, ?, ?, ?," +
				"?, ?, ? )";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setLong(1, post_id);
			pstmt.setString(2, author);
			pstmt.setString(3, author_email);
			pstmt.setString(4, author_url);
			pstmt.setString(5, author_ip);
			pstmt.setTimestamp(6, cdate);
			pstmt.setString(7, content);
			pstmt.setInt(8, status);
			pstmt.setLong(9, parent_id);
			pstmt.setLong(10, user_id);
			pstmt.setInt(11, notice);
			pstmt.setString(12, comment_img);
			pstmt.setLong(13, uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
		
		/*相关改变处理*/
		PostManager.addCommentCount(String.valueOf(post_id));
		VMailManager.sendCommentRemindToParent(this);
		VMailManager.sendCommentRemindToPostAuthor(this);
	}
	
	public static void main(String[] a){
//		Comment cm = new Comment();
//		cm.setAuthor("vell");
//		cm.setAuthor_email("vell001@qq.com");
//		cm.setAuthor_ip("192.168.1.1");
//		cm.setAuthor_url("www.vell001.ml");
//		cm.setCdate(new Timestamp(System.currentTimeMillis()));
//		cm.setContent("hello...");
//		cm.setParent_id(0);
//		cm.setPost_id(20);
//		cm.setStatus(1);
//		cm.setUser_id(1);
//		cm.saveToDB();
	}
}
