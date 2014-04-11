package vell.bibi.vview.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import vell.bibi.vview.Setting;
import vell.bibi.vview.mail.VMailManager;
import vell.bibi.vview.util.DB;
import vell.bibi.vview.util.DesUtil;

public class User {
	private int id;
	private int status;
	private String nickname;
	private String username;
	private String password;
	private String email;
	private String user_url;
	private Timestamp rdate;
	private Timestamp ldate;
	private String ver_code;
	public String getVer_code() {
		return ver_code;
	}
	public void setVer_code(String ver_code) {
		this.ver_code = ver_code;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Timestamp getLdate() {
		return ldate;
	}
	public void setLdate(Timestamp ldate) {
		this.ldate = ldate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * Description 得到加密后的密码
	 * @return 加密后的密码
	 */
	public String getCryptPassword() {
		return password;
	}
	/**
	 * @return 解密后的密码
	 */
	public String getPassword() {
		String pwd = null;
		try {
			pwd = DesUtil.decrypt(password, Setting.getDesKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pwd;
	}
	/**
	 * @param password 原密码 
	 */
	public void setCryptPassword(String password) {
		try {
			this.password = DesUtil.encrypt(password, Setting.getDesKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param CryptPassword 加密密码
	 */
	public void setPassword(String CryptPassword) {
		this.password = CryptPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUser_url() {
		return user_url;
	}
	public void setUser_url(String user_url) {
		this.user_url = user_url;
	}
	public Timestamp getRdate() {
		return rdate;
	}
	public void setRdate(Timestamp rdate) {
		this.rdate = rdate;
	}
	/**
	 * 保存到数据库
	 */
	public void saveToDB() {
		Connection conn = DB.getConn();
		String sql = "insert into user values (null, ?, ?, ?, ?, ?," +
				" ?, ?, ?, ?)";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setString(1, nickname);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, email);
			pstmt.setString(5, user_url);
			pstmt.setInt(6, status);
			pstmt.setTimestamp(7, rdate);
			pstmt.setTimestamp(8, ldate);
			pstmt.setString(9, ver_code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
		
		/*  新建用户处理   */
		VMailManager.sendUserRegisterToAdmin(this);
	}
	
	public static void main(String[] args) {
		User u = new User();
		u.setCryptPassword("vell001");
		u.setEmail("vasdf");
		u.setRdate(new Timestamp(System.currentTimeMillis()));
		u.setStatus(1);
		u.setUser_url("www.");
		u.setUsername("vell001");
		u.saveToDB();
		System.out.println("ok");
	}
}
