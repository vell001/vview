package vell.bibi.vview.label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vell.bibi.vview.util.DB;

public class LabelForPostManager {
	public static LabelForPost getLFPByLabelsUid(String label_uid) {
		LabelForPost lfp = null;
		Connection conn = DB.getConn();
		String sql = "select * from post_label where label_uid='"+label_uid+"'" ;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		List<LabelForPost> list = resultSetToList(rs);
		if(!list.isEmpty()) lfp = list.get(0);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return lfp;
	}
	
	public static List<LabelForPost> resultSetToList(ResultSet rs) {
		List<LabelForPost> list = new ArrayList<LabelForPost>();
		LabelForPost lfp = null;
		try {
			while (rs.next()) {
				lfp = new LabelForPost();
				lfp.setLabel_uid(rs.getLong("label_uid"));
				lfp.setPost_uid(rs.getLong("post_uid"));
				list.add(lfp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 删除post_uid的所以lfp关系
	 * @param post_uid
	 */
	private static void deleteAllLFPByPostUid(long post_uid){
		String sql = "delete from post_label where post_uid='"+post_uid+"'";
		Connection conn = DB.getConn();
		Statement stmt = DB.getStmt(conn);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
	}
	
	public static void updateLabelsForPost(String labels_string, long post_uid){
		if(labels_string == null || post_uid <= 0) return;
		deleteAllLFPByPostUid(post_uid);//删除之前的lfp关系
		List<Label> labels = LabelManager.getLabelsFormString(labels_string);
		updateLabelsForPost(labels,post_uid);
	}
	
	public static void updateLabelsForPost(List<Label> labels, long post_uid){
		if(labels == null || post_uid <= 0) return;
		LabelForPost l_p = new LabelForPost();
		Label l = null;
		Iterator<Label> l_it = labels.iterator();
		while(l_it.hasNext()){
			l = l_it.next();
			l_p.setLabel_uid(l.getUid());
			l_p.setPost_uid(post_uid);
			l_p.saveToDB();
		}
	}
	
	public static boolean belongToPostByUid(String label_uid, String post_uid){
		if(label_uid == null || post_uid == null) return false;
		LabelForPost l = getLFPByLabelsUid(label_uid);
		if(l != null && String.valueOf(l.getPost_uid()).equals(post_uid)){
			return true;
		}
		return false;
	}
}
