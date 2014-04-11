package vell.bibi.vview.label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vell.bibi.vview.util.DB;

public class LabelForPost {
	private long post_uid;
	private long label_uid;
	public LabelForPost(long post_uid, long label_uid){
		this.post_uid = post_uid;
		this.label_uid = label_uid;
	}
	public LabelForPost(){}
	
	public long getPost_uid() {
		return post_uid;
	}
	public void setPost_uid(long post_uid) {
		this.post_uid = post_uid;
	}
	public long getLabel_uid() {
		return label_uid;
	}
	public void setLabel_uid(long label_uid) {
		this.label_uid = label_uid;
	}
	/**
	 * 保存到数据库
	 */
	public void saveToDB() {
		Connection conn = DB.getConn();
		String sql = "insert into post_label values (?, ?)";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setLong(1, post_uid);
			pstmt.setLong(2, label_uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
	}
	public static void main(String[] args) {

	}

}
