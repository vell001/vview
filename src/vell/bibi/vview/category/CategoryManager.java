package vell.bibi.vview.category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vell.bibi.vview.util.DB;

public class CategoryManager {

	public static List<Category> getAllCategory(){
		String sql = "select * from category where status>0 order by id";
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Category> list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	/**
	 * @param id
	 * @return
	 */
	public static String getCategoryNameById(String id){
		if(id == null || id == "") return null;
		String name = null;
		String sql = "select name from category where status>0 and id='"+id+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		
		try {
			while (rs.next()) {
				name = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return name;
	}
	
	public static Category getCategoryById(String id){
		if(id == null || id == "") return null;
		String sql = "select * from category where status>0 and id='"+id+"'";
		Connection conn = DB.getConn();
		Category c = null;
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Category> list = resultSetToList(rs);
		if(!list.isEmpty()) c = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return c;
	}
	
	public static Category getCategoryByName(String name){
		if(name == null || name == "") return null;
		String sql = "select * from category where status>0 and name='"+name+"'";
		Connection conn = DB.getConn();
		Category c = null;
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Category> list = resultSetToList(rs);
		if(!list.isEmpty()) c = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return c;
	}
	
	public static List<Category> resultSetToList(ResultSet rs) {
		if(rs == null) return null;
		List<Category> list = new ArrayList<Category>();
		Category c = null;
		try {
			while (rs.next()) {
				c = new Category();
				c.setId(rs.getInt("id"));
				c.setInfo(rs.getString("info"));
				c.setName(rs.getString("name"));
				c.setStatus(rs.getInt("status"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
