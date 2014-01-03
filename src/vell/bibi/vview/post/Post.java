package vell.bibi.vview.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import vell.bibi.vview.util.DB;
import vell.bibi.vview.util.UID;

public class Post {
	private long id;
	private long author;
	private Timestamp pdate;
	private String content;
	private String title;
	private String excerpt; //摘要
	private String excerpt_img;
	private int status;
	private int comment_status;
	private Timestamp mdate;
	private int category_id;
	private long parent;
	private long comment_count;
	private long click_count;
	private long score;
	private long uid;
	
	public Post(){
		this.uid = UID.next();
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getUid() {
		return uid;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public String getExcerpt_img() {
		return excerpt_img;
	}
	public void setExcerpt_img(String excerpt_img) {
		this.excerpt_img = excerpt_img;
	}
	public long getClick_count() {
		return click_count;
	}
	public void setClick_count(long click_count) {
		this.click_count = click_count;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAuthor() {
		return author;
	}
	public void setAuthor(long author) {
		this.author = author;
	}
	public Timestamp getPdate() {
		return pdate;
	}
	public void setPdate(Timestamp pdate) {
		this.pdate = pdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getComment_status() {
		return comment_status;
	}
	public void setComment_status(int comment_status) {
		this.comment_status = comment_status;
	}
	public Timestamp getMdate() {
		return mdate;
	}
	public void setMdate(Timestamp mdate) {
		this.mdate = mdate;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public long getParent() {
		return parent;
	}
	public void setParent(long parent) {
		this.parent = parent;
	}
	public long getComment_count() {
		return comment_count;
	}
	public void setComment_count(long comment_count) {
		this.comment_count = comment_count;
	}
	
	/**
	 * 保存到数据库
	 */
	public void saveToDB() {
		Connection conn = DB.getConn();
		String sql = "insert into post values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setLong(1, author);
			pstmt.setTimestamp(2, pdate);
			pstmt.setString(3, content);
			pstmt.setString(4, title);
			pstmt.setString(5, excerpt);
			pstmt.setInt(6, status);
			pstmt.setInt(7, comment_status);
			pstmt.setTimestamp(8, mdate);
			pstmt.setInt(9, category_id);
			pstmt.setLong(10, parent);
			pstmt.setLong(11, comment_count);
			pstmt.setLong(12, click_count);
			pstmt.setString(13, excerpt_img);
			pstmt.setLong(14, score);
			pstmt.setLong(15, uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
	}
	
	public static void main(String[] args) {
//		Post p = new Post();
//		p.setAuthor(1);
//		p.setCategory_id(1);
//		p.setComment_count(0);
//		p.setComment_status(1);
//		p.setContent("由于某些需要需要来回切换IP，之前找了几个修改IP的软件，总有些bug. 其实一想，还不如用批处理来的快捷方便。下面提供2个批处理用来切换、修改IP。1.这个批处理可以在两个或多个IP、掩码、网关之间选择切换，没有...");
//		p.setExcerpt("由于某些需要需要来回切换IP，之前找了几个修改IP的软件，总有些bug. 其实一想，还不如用批处理来的快捷方便。下面提供2个批处理用来切换、修改IP。1.这个批处理可以在两个或多个IP、掩码、网关之间选择切换，没有...");
//		p.setMdate(new Timestamp(System.currentTimeMillis()));
//		p.setPdate(new Timestamp(System.currentTimeMillis()));
//		p.setStatus(1);
//		p.setTitle("好久没来");
//		p.setExcerpt_img("images/post_img_5.jpg");
//		p.setClick_count(0);
//		p.saveToDB();
//		System.out.println("post_save_ok");
	}
}
