package vell.bibi.vview.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import vell.bibi.vview.util.DB;

public class UserManager {
	/**
	 * @return 所有用户List
	 */
	public static List<User> getUsers() {
		Connection conn = DB.getConn();
		ResultSet rs = null;
		String sql = "select * from user";
		rs = DB.executeQuery(conn, sql);
		
		List<User> list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	/**
	 * @param id 删除用户id
	 */
	public static void deleteUser(String id) {
		if(id == null || id == "") return;
		String sql = "delete from user where id=?";
		Connection conn = DB.getConn();
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
	}
	/**
	 * @param username 查找用户名
	 * @return
	 */
	public static User getUserByName(String username) {
		if(username == null || username == "") return null;
		User u = null;
		String sql = "select * from user where username='"+username+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<User> list = resultSetToList(rs);
		if(!list.isEmpty()) u = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return u;
	}
	/**
	 * @param id
	 * @return
	 */
	public static User getUserById(String id) {
		if(id == null || id == "") return null;
		User u = null;
		String sql = "select * from user where id='"+id+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<User> list = resultSetToList(rs);
		if(!list.isEmpty()) u = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return u;
	}
	
	public static User getUserByEmail(String email) {
		if(email == null || email == "") return null;
		User u = null;
		String sql = "select * from user where email='"+email+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<User> list = resultSetToList(rs);
		if(!list.isEmpty()) u = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return u;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static String getUserNameById(String id) {
		if(id == null || id == "") return null;
		String username = null;
		String sql = "select username from user where id='"+id+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		
		try {
			while (rs.next()) {
				username = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return username;
	}
	
	/**
	 * @param username 
	 * @param password 加密后的密码
	 * @return
	 */
	public static boolean isLogin(String username, String password) {
		if(username!=null && password!=null){
			User u = UserManager.getUserByName(username);
			if(u!=null && password.equals(u.getCryptPassword())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param cookies 传入Cookie[] 判断登陆
	 * @return
	 */
	public static boolean isLogin(Cookie[] cookies) {
		if(cookies == null) return false;
		HashMap<String,String> cookiesmap = new HashMap<String,String>();
		for(int i=0; i<cookies.length; i++) {
			cookiesmap.put(cookies[i].getName(), cookies[i].getValue());
		}
		String username = getCookiesValue(cookies,"username");
		String password = getCookiesValue(cookies,"password");
		
		return isLogin(username,password);
	}
	/**
	 * @param cookies
	 * @param key 主键
	 * @return
	 */
	public static String getCookiesValue(Cookie[] cookies,String key) {
		if(cookies == null || key == null) return null;
		HashMap<String,String> cookiesmap = new HashMap<String,String>();
		for(int i=0; i<cookies.length; i++) {
			cookiesmap.put(cookies[i].getName(), cookies[i].getValue());
		}
		return cookiesmap.get(key);
	}
	
	/**
	 * rs 转 List
	 * @param rs
	 * @return List<User>
	 * @throws SQLException
	 */
	public static List<User> resultSetToList(ResultSet rs) {
		if(rs == null) return null;
		List<User> list = new ArrayList<User>();
		User u = null;
		try {
			while (rs.next()) {
				u = new User();
				u.setId(rs.getInt("id"));
				u.setNickname(rs.getString("nickname"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				u.setUser_url(rs.getString("user_url"));
				u.setStatus(rs.getInt("status"));
				u.setRdate(rs.getTimestamp("rdate"));
				u.setLdate(rs.getTimestamp("ldate"));
				u.setVer_code(rs.getString("ver_code"));
				list.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据u修改密码
	 * @param conn
	 * @param u
	 */
	public static void updatePassword(User u) {
		Connection conn = DB.getConn();
		String sql = "update user set password='"+u.getCryptPassword()+"' where id='"+ u.getId()+"'";
		Statement stmt = DB.getStmt(conn);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
	}
	public static void updateStatus(User u) {
		Connection conn = DB.getConn();
		String sql = "update user set status='"+u.getStatus()+"' where id='"+ u.getId()+"'";
		Statement stmt = DB.getStmt(conn);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
	}
	
	public static void main(String[] a){
//		User u = UserManager.getUserByName("aaaa");
//		System.out.println(u.getVer_code().equals("O+av7eUNGgVfRJ1kjZdcqhmKDFUZfB0u"));
	}
}
