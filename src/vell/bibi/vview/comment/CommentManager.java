package vell.bibi.vview.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vell.bibi.vview.RightManager;
import vell.bibi.vview.post.Post;
import vell.bibi.vview.post.PostManager;
import vell.bibi.vview.user.User;
import vell.bibi.vview.util.DB;

public class CommentManager {

	public static Comment getCommentById(String id) {
		if(id == null || id == "") return null;
		Comment cm = null;
		String sql = "select * from comment where status>0 and id='"+id+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Comment> list = resultSetToList(rs);
		if(!list.isEmpty()) cm = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return cm;
	}
	
	public static Comment getCommentByUid(String uid) {
		if(uid == null || uid == "") return null;
		Comment cm = null;
		String sql = "select * from comment where status>0 and uid='"+uid+"'";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Comment> list = resultSetToList(rs);
		if(!list.isEmpty()) cm = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return cm;
	}
	
	public static List<Comment> getCommentsByStatus(String status) {
		if(status == null || status == "") return null;
		String sql = "select * from comment where status = '"+status+"' order by cdate";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Comment> list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Comment> getCommentsByParentId(String parent_id) {
		if(parent_id == null || parent_id == "") return null;
		String sql = "select * from comment where status>0 and parent_id='"+parent_id+"' order by cdate";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Comment> list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	/**
	 * @param parent_id
	 * @param post_id
	 * @return
	 */
	public static List<Comment> getCommentsByPostParentId(String parent_id, String post_id) {
		if(parent_id == null || parent_id == "") return null;
		if(post_id == null || post_id == "") return getCommentsByParentId(parent_id);
		String sql = "select * from comment where status>0 and parent_id="+parent_id+" and post_id="+post_id +" order by cdate";
		Connection conn = DB.getConn();
		
		ResultSet rs = DB.executeQuery(conn,sql);
		List<Comment> list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Comment> getCommentsByCdate(int start, int num) {
		List<Comment> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from comment where status>0 order by cdate desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static boolean deleteCommentById(String id){
		if(id == null) return false;
		//修改post评论数
		Comment cm = getCommentById(id);
		PostManager.subCommentCount(String.valueOf(cm.getPost_id()));
		
		Connection conn = DB.getConn();
		String sql = "update comment set " +
				"status=? " +
				"where id =?";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setInt(1, -1);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
		return false;
	}
	
	public static List<Comment> resultSetToList(ResultSet rs) {
		if(rs == null) return null;
		List<Comment> list = new ArrayList<Comment>();
		Comment cm = null;
		try {
			while (rs.next()) {
				cm = new Comment();
				cm.setId(rs.getLong("id"));
				cm.setAuthor(rs.getString("author"));
				cm.setAuthor_email(rs.getString("author_email"));
				cm.setAuthor_ip(rs.getString("author_ip"));
				cm.setAuthor_url(rs.getString("author_url"));
				cm.setCdate(rs.getTimestamp("cdate"));
				cm.setContent(rs.getString("content"));
				cm.setParent_id(rs.getLong("parent_id"));
				cm.setPost_id(rs.getLong("post_id"));
				cm.setStatus(rs.getInt("status"));
				cm.setUser_id(rs.getLong("user_id"));
				cm.setNotice(rs.getInt("notice"));
				cm.setComment_img(rs.getString("comment_img"));
				cm.setUid(rs.getLong("uid"));
				list.add(cm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static String printSonComment(String parent_id, Post p, String content, User u){
	    List<Comment> cmlist = CommentManager.getCommentsByPostParentId(parent_id, String.valueOf(p.getId()));
    	if(cmlist == null) return content;
    	String str = "";
    	Iterator<Comment> cmit = cmlist.iterator();
    	Comment cm = null;
    	while(cmit.hasNext()){
    		cm = (Comment)cmit.next();
    		String could_delete = RightManager.couldDeleteComment(u, p)?"<a href=\"delete_comment.jsp?cmid="+cm.getId()+"&pid="+p.getId()+"\">【删除】</a>":"";
    		content = content + 
    			"<div class=\"comment\" id=\"comment_"+cm.getId()+"\">" +
					"<a name=\"comment_"+cm.getId()+"\"></a>"+
    	    		"<div class=\"comment_title\">"+
    	    			"<img class=\"author_img\" src=\""+cm.getComment_img()+"\" />"+
    	    			"<div class=\"author_info\">"+
    	    				"<h3 class=\"author_name\">"+cm.getAuthor()+"</h3>"+
    	    				"<span class=\"create_time\">时间："+cm.getCdate()+could_delete+"</span>"+
    	    			"</div>" +
    	    		"</div>" +
    	    		"<hr>" +
    	    		"<div class=\"comment_main\">"+
	    				cm.getContent()+
	    				"<p class=\"resp\"><a href=\"#comment_"+cm.getId()+"\" onclick=\"move_form('"+cm.getId()+"')\">回复</a></p>"+
	    			"</div>"+
	    			"<div id=\""+cm.getId()+"\" ></div>" +
    	    		printSonComment(String.valueOf(cm.getId()), p ,str,u)+
    	    	"</div>";
    	}
    	return content;
    }
	
	public static void main(String[] a){
//		Comment cm = getCommentById(String.valueOf(1));
//		System.out.println(cm.getContent());
//		List<Comment> list = getCommentsByParentId(String.valueOf(1));
//		System.out.println(list.isEmpty());
//		String content = "";
//	    content = printSonComment("0", String.valueOf(25),content);
//	    System.out.print(content);
	}
}
