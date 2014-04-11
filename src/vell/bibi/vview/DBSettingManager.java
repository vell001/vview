package vell.bibi.vview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vell.bibi.vview.util.DB;

public class DBSettingManager {
	public static String getNotice(){
		DBSetting dbs = null;
		String notice = null;
		String sql = "select * from setting where status>0 order by cdate desc" ;
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);

		List<DBSetting> list = resultSetToList(rs);
		if(!list.isEmpty()) {
			dbs = list.get(0);
			notice = dbs.getNotice();
		}
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return notice;
	}
	
	public static List<DBSetting> resultSetToList(ResultSet rs) {
		if(rs == null) return null;
		List<DBSetting> list = new ArrayList<DBSetting>();
		DBSetting dbs = null;
		try {
			while (rs.next()) {
				dbs = new DBSetting();
				dbs.setCdate(rs.getTimestamp("cdate"));
				dbs.setId(rs.getLong("id"));
				dbs.setNotice(rs.getString("notice"));
				dbs.setStatus(rs.getInt("status"));
				list.add(dbs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
