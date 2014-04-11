package vell.bibi.vview.label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import vell.bibi.vview.util.DB;
import vell.bibi.vview.util.StringShiftUtil;
import vell.bibi.vview.util.UID;

public class LabelManager {

	public static List<Label> getPublishLabels() {
		
		Connection conn = DB.getConn();
		String sql = "select * from label where status>0 order by id desc" ;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		List<Label> list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static Label getPublishLabelByName(String name) {
		if(name == null) return null;
		String shift_name = null;
		shift_name = StringShiftUtil.enShift(name);
		return getPublishLabelByShiftName(shift_name);
	}
	
	public static Label getPublishLabelByShiftName(String shift_name) {
		if(shift_name == null) return null;
		Label l = null;
		String sql = "select * from label where status>0 and name='"+shift_name+"'" ;
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn, sql);
		
		List<Label> list = resultSetToList(rs);
		if(!list.isEmpty()) l = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return l;
	}
	
	public static Label getPublishLabelByUid(String uid) {
		if(uid == null) return null;
		Label l = null;
		Connection conn = DB.getConn();
		String sql = "select * from label where status>0 and uid='"+uid+"'" ;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		List<Label> list = resultSetToList(rs);
		if(!list.isEmpty()) l = list.get(0);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return l;
	}
	
	public static List<Label> getPublishLabelsByPostUid(String post_uid) {
		Connection conn = DB.getConn();
		String sql = "select * from label where status>0 and uid in " +
				"(select label_uid from post_label where post_uid='"+post_uid+"') " +
				"order by id desc";
		
		ResultSet rs = DB.executeQuery(conn, sql);
		List<Label> list = resultSetToList(rs);

		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Label> getPublishLabelsByPostUid(int start, int num, String post_uid) {
		Connection conn = DB.getConn();
		String sql = "select * from label where status>0 and uid in " +
				"(select label_uid from post_label where post_uid='"+post_uid+"') " +
				"order by id desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		List<Label> list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Label> resultSetToList(ResultSet rs) {
		List<Label> list = new ArrayList<Label>();
		Label l = null;
		try {
			while (rs.next()) {
				l = new Label();
				l.setId(rs.getInt("id"));
				l.setInfo(rs.getString("info"));
				l.setShiftName(rs.getString("name"));
				l.setStatus(rs.getInt("status"));
				l.setUid(rs.getLong("uid"));
				list.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 自动保存labels字符串中的label
	 * labels如：程序猿 电脑 手机
	 * @param labels
	 */
	public static List<Label> getLabelsFormString(String labels){
		if(labels == null || labels.trim().equals("")) return null;
		List<Label> list = new ArrayList<Label>();
		Label l = null;
		
		String regEx="(\\s|&|\\+|-|#|\\?)+";
		Pattern pat = Pattern.compile(regEx);
		String[] rs = pat.split(labels.trim());
		
		for(int i=0; i<rs.length; i++){
			l = getPublishLabelByName(rs[i]);
			if(l == null){//如果是新的就保存到数据库
				l = new Label();
				l.setName(StringShiftUtil.wipeStr(rs[i]));
				l.setInfo(rs[i]);
				l.setUid(Long.valueOf(UID.next()));
				l.saveToDB();
			}
			list.add(l);
		}
		return list;
	}
	
	public static void saveLabelsFormString(String labels){
		if(labels == null || labels.trim().equals("")) return;
		Label l = null;
		
		String regEx="(\\s|&|\\+|-|#|\\?)+";
		Pattern pat = Pattern.compile(regEx);
		String[] rs = pat.split(labels.trim());
		
		for(int i=0; i<rs.length; i++){
			if(getPublishLabelByName(rs[i]) == null){
				l = new Label();
				l.setName(StringShiftUtil.wipeStr(rs[i]));
				l.setInfo(rs[i]);
				l.setUid(Long.valueOf(UID.next()));
				l.saveToDB();
			}
		}
	}
	
	public static void main(String[] a){
//		saveLabelsFormString("程序猿               电脑 手机  V&View");
		System.out.println(getLabelsFormString("程序猿        +aa?a+       电脑 手机  V&View").size());
//		String sql = "select * from label where status>0 and name='V&View'" ;
//		System.out.println(sql);
//		System.out.println(getPublishLabelByShiftName("V%26View").getUid());
//		try {
//			System.out.println(URLEncoder.encode("?&#", "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
