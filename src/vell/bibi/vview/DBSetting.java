package vell.bibi.vview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import vell.bibi.vview.util.DB;

public class DBSetting {
	private long id;
	private Timestamp cdate;
	private int status;
	private String notice;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getCdate() {
		return cdate;
	}
	public void setCdate(Timestamp cdate) {
		this.cdate = cdate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	public void saveToDB(){
		Connection conn = DB.getConn();
		String sql = "insert into setting values (null, ?, ?, ?)";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setString(1, notice);
			pstmt.setTimestamp(2, cdate);
			pstmt.setLong(3, status);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
		
		/*相关改变处理*/
	}
	public static void main(String[] args) {
		DBSetting dbs = new DBSetting();
		dbs.setNotice("asdfadsfaf");
		dbs.setStatus(1);
		dbs.setCdate(new Timestamp(System.currentTimeMillis()));
		dbs.saveToDB();
	}

}
