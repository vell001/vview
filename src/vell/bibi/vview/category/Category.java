package vell.bibi.vview.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vell.bibi.vview.util.DB;

public class Category {

	private int id;
	private String name;
	private String info;
	private int status;
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		String sql = "insert into category values (null, ?, ?, ?)";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setString(1, name);
			pstmt.setString(2, info);
			pstmt.setInt(3, status);
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
		Category c = new Category();
		c.setInfo("hao");
		c.setName("test");
		c.setStatus(1);
		c.saveToDB();
	}

	public void setId(int id) {
		this.id = id;
	}

}
