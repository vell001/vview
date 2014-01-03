package vell.bibi.vview.label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vell.bibi.vview.util.DB;
import vell.bibi.vview.util.StringShiftUtil;

public class Label {
	private long id;
	private String shift_name;
	private String info;
	private int status = 1;
	private long uid;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		String name = null;
		name = StringShiftUtil.deShift(shift_name);
		return name;
	}
	public String getShiftName() {
		return shift_name;
	}
	/**
	 * 用明文设置name
	 * @param name
	 */
	public void setShiftName(String shift_name) {
		this.shift_name = shift_name;
	}
	/**
	 * 用暗文设置name
	 * @param name
	 */
	public void setName(String name) {
		this.shift_name = StringShiftUtil.enShift(name);
		
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * 保存到数据库
	 */
	public void saveToDB() {
		Connection conn = DB.getConn();
		String sql = "insert into label values (null, ?, ?, ?, ?)";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setString(1, shift_name);
			pstmt.setString(2, info);
			pstmt.setInt(3, status);
			pstmt.setLong(4, uid);
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

	}
}
