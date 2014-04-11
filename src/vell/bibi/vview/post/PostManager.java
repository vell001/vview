package vell.bibi.vview.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vell.bibi.vview.Setting;
import vell.bibi.vview.util.DB;

public class PostManager {

	/**
	 * 返回所有文章
	 * @return List<Post>
	 */
	public static List<Post> getAllPosts() {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post";
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);

		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	/**
	 * @param start 开始行数
	 * @param num 返回行数
	 * @return List<Post>
	 */
	public static List<Post> getPublishPosts(int start, int num) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 order by mdate desc limit " + start + "," + num;

		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);

		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	/**
	 * 按字段field降序
	 * @param start 开始行数
	 * @param num 返回行数
	 * @param field 排名字段
	 * @return List<Post>
	 */
	public static List<Post> getPublishPosts(int start, int num, String field) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 order by "+field+" desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	/**
	 * 按分数降序
	 * @param start
	 * @param num
	 * @return List<Post>
	 */
	public static List<Post> getPublishPostsByScore(int start, int num) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 order by score desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Post> getPublishCategoryPosts(int start, int num, String category_id) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 and category_id="+category_id+" order by mdate desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	public static List<Post> getPublishLabelPosts(int start, int num, String label_uid) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where uid in (select post_uid from post_label where label_uid = '"+label_uid+"') limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Post> getPublishCategoryPostsByScore(int start, int num, String category_id) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 and category_id="+category_id+" order by score desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	public static List<Post> getPublishLabelPostsByScore(int start, int num, String label_uid) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where uid in (select post_uid from post_label where label_uid = '"+label_uid+"') order by score desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	public static List<Post> getPublishAuthorPosts(int start, int num, String author_id) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 and author="+author_id+" order by mdate desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	public static List<Post> getPublishAuthorPostsByScore(int start, int num, String author_id) {
		List<Post> list = null;
		Connection conn = DB.getConn();
		String sql = "select * from post where status>0 and author="+author_id+" order by score desc limit " + start + "," + num;
		
		ResultSet rs = DB.executeQuery(conn, sql);
		list = resultSetToList(rs);
		DB.closeRs(rs);
		DB.closeConn(conn);
		return list;
	}
	
	/**
	 * 获取具体文章
	 * @param pid 文章id
	 * @return Post
	 */
	public static Post getPostById(String pid) {
		if(pid == null || pid == "") return null;
		Post p = null;
		String sql = "select * from post where id='"+pid+"'";
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		
		List<Post> list = resultSetToList(rs);
		if(!list.isEmpty()) p = list.get(0);
		
		updateScore(conn,p);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return p;
	}
	
	public static Post getPostByUid(String uid) {
		if(uid == null || uid .equals("")) return null;
		Post p = null;
		String sql = "select * from post where uid='"+uid+"'";
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		
		List<Post> list = resultSetToList(rs);
		if(!list.isEmpty()) p = list.get(0);
		
		DB.closeRs(rs);
		DB.closeConn(conn);
		return p;
	}
	
	/**
	 * 更新click_count
	 * @param conn 数据库连接
	 * @param p 要更新的post实例
	 */
	public static void addClickCount(Post p) {
		String sql = "update post set click_count="+(p.getClick_count()+1)+" where id="+ p.getId();
		p.setClick_count(p.getClick_count()+1);
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
	
	/**
	 * 更新score
	 * @param conn 数据库连接
	 * @param p 要更新的post实例
	 */
	private static void updateScore(Connection conn, Post p) {
		long score = 1400 + p.getClick_count() + p.getComment_count()*2;
		p.setScore(score);
		String sql = "update post set score="+score+" where id="+ p.getId();
		Statement stmt = DB.getStmt(conn);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addCommentCount(String pid) {
		Post p = PostManager.getPostById(pid);
		Connection conn = DB.getConn();
		p.setComment_count(p.getComment_count()+1);
		updateScore(conn, p);
		String sql = "update post set comment_count="+p.getComment_count()+" where id="+ p.getId();
		Statement stmt = DB.getStmt(conn);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
	}
	
	public static void subCommentCount(String pid) {
		Post p = PostManager.getPostById(pid);
		Connection conn = DB.getConn();
		p.setComment_count(p.getComment_count()-1);
		updateScore(conn, p);
		String sql = "update post set comment_count="+p.getComment_count()+" where id="+ p.getId();
		Statement stmt = DB.getStmt(conn);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
	}
	
	/**
	 * 获取总文章数
	 * @return int
	 */
	public static int getPublishPostNum() {
		String sql = "select count(*) from post where status>0 ";
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		try {
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeConn(conn);
		}
		return 0;
	}
	public static int getCategoryPostNum(String category_id) {
		String sql = "select count(*) from post where status>0 and category_id="+category_id;
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		try {
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeConn(conn);
		}
		return 0;
	}
	public static int getLabelPostNumByUid(String label_uid) {
		String sql = "select count(*) from post_label where label_uid='"+label_uid+"'";
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		try {
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeConn(conn);
		}
		return 0;
	}
	
	public static int getAuthorPostNum(String author_id) {
		String sql = "select count(*) from post where status>0 and author="+author_id;
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		try {
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeConn(conn);
		}
		return 0;
	}
	/**
	 * 获取所有用户文章数
	 * @param author_id
	 * @return
	 */
	public static int getAllAuthorPostNum(String author_id) {
		String sql = "select count(*) from post where author="+author_id;
		Connection conn = DB.getConn();
		ResultSet rs = DB.executeQuery(conn,sql);
		try {
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeConn(conn);
		}
		return 0;
	}
	
	/**
	 * rs 转 List
	 * @param rs
	 * @return List<Post>
	 * @throws SQLException
	 */
	public static List<Post> resultSetToList(ResultSet rs) {
		List<Post> list = new ArrayList<Post>();
		Post p = null;
		try {
			while (rs.next()) {
				p = new Post();
				p.setId(rs.getInt("id"));
				p.setAuthor(rs.getLong("author"));
				p.setTitle(rs.getString("title"));
				p.setPdate(rs.getTimestamp("pdate"));
				p.setCategory_id(rs.getInt("category_id"));
				p.setComment_count(rs.getLong("comment_count"));
				p.setComment_status(rs.getInt("comment_status"));
				p.setContent(rs.getString("content"));
				p.setExcerpt(rs.getString("excerpt"));
				p.setMdate(rs.getTimestamp("mdate"));
				p.setParent(rs.getLong("parent"));
				p.setStatus(rs.getInt("status"));
				p.setExcerpt_img(rs.getString("excerpt_img"));
				p.setClick_count(rs.getLong("click_count"));
				p.setScore(rs.getLong("score"));
				p.setUid(rs.getLong("uid"));
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 保存p
	 * @param p
	 * @return
	 */
	public static Post savePost(Post p){
		if(p == null) return null;
		
		String excerpt_img = getExcerptImg(p.getContent());
		if(excerpt_img == null){
			excerpt_img=Setting.getDefaultExcerptImgSrc();
		}
		p.setExcerpt_img(excerpt_img);
		
		p.setExcerpt(getExcerpt(p.getContent()));
		p.setMdate(new Timestamp(System.currentTimeMillis()));
		Post db_p = getPostByUid(Long.toString(p.getUid()));
		if(p.getContent() == "" && p.getTitle() == "") return p;//不用保存
		if(db_p == null){//创建新post
			p.setPdate(new Timestamp(System.currentTimeMillis()));
			p.saveToDB();
		}else{
			p.setPdate(db_p.getPdate());
			updatePostToDB(p);
		}
		return p;
	}
	
	/**
	 * 更新数据库中的p
	 * @param p
	 */
	private static void updatePostToDB(Post p){
		if(p == null) return;
		Connection conn = DB.getConn();
		String sql = "update post set " +
				"author=?," +
				"pdate=?," +
				"content=?," +
				"title=?," +
				"excerpt=?," +
				"status=?," +
				"comment_status=?," +
				"mdate=?," +
				"category_id=?, " +
				"parent=?," +
				"comment_count=?, " +
				"click_count=?," +
				"excerpt_img =?," +
				"score=? " +
				"where uid =?";
		PreparedStatement pstmt = DB.getPStmt(conn, sql);
		try {
			pstmt.setLong(1, p.getAuthor());
			pstmt.setTimestamp(2, p.getPdate());
			pstmt.setString(3, p.getContent());
			pstmt.setString(4, p.getTitle());
			pstmt.setString(5, p.getExcerpt());
			pstmt.setInt(6, p.getStatus());
			pstmt.setInt(7, p.getComment_status());
			pstmt.setTimestamp(8, p.getMdate());
			pstmt.setInt(9, p.getCategory_id());
			pstmt.setLong(10, p.getParent());
			pstmt.setLong(11, p.getComment_count());
			pstmt.setLong(12, p.getClick_count());
			pstmt.setString(13, p.getExcerpt_img());
			pstmt.setLong(14, p.getScore());
			pstmt.setLong(15, p.getUid());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
	}
	
	/**
	 * 找到文章中的第一张图片
	 * @param content
	 * @return
	 */
	public static String getExcerptImg(final String content){
		String imgSrc = null;
		String regEx = "<img\\s+.*src\\s*=\\s*.*/>";
		String s = content;
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		if(mat.find()){//找到img标签
			s = mat.group(mat.groupCount());
			regEx = "\\s+src\\s*=\\s*\".*\\.\\w{1,6}\\s*\"\\s";
			pat = Pattern.compile(regEx);
			mat = pat.matcher(s);
			if(mat.find()){//找到img标签中的src
				s = mat.group(0);
				regEx = "\".+\"";
				pat = Pattern.compile(regEx);
				mat = pat.matcher(s);
				if(mat.find()){//找到src中的地址
					s = mat.group(0);
					imgSrc = s.substring(1,s.length()-1);
				}
			}
		}
		return imgSrc;
	}
	
	/**
	 * 获取简介
	 * @param content
	 * @return
	 */
	public static String getExcerpt(final String content){
		String excerpt = null;
		excerpt = getTextFromHtml(content);
        if(excerpt.length() > 150) excerpt = excerpt.substring(0, 150)+"...";
		return excerpt;
	}
	
	public static String getTextFromHtml(final String Html){
		String text = null;
		// 配置html标记。  
        Pattern p = Pattern.compile("<(\\S*?)[^>]*>.*?| <.*? />");  
        Matcher m = p.matcher(Html);
        text = Html;
        // 找出所有html标记。  
        while (m.find()) {  
            // 删除html标记。  
        	text = text.replace(m.group(), "");
        }
        return text;
	}
	
	public static boolean deletePostById(String id){
		if(id == null) return false;
		Connection conn = DB.getConn();
		String sql = "update post set " +
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
	
	public static void main(String[] args) {
//		List<Post> list = getAllPosts();
//		System.out.println(list.isEmpty());
//		System.out.println(getPostNum());
//		getExcerpt("<h1>asdfasdfa</h1><img src=\"adsfasdfadfafd\" /><td><a target=\"_blank\" href=\"http://www.baidu.com/baidu?cl=3&tn=baidutop10&fr=top1000&wd=%A1%B6%CC%C6%C9%BD%B4%F3%B5%D8%D5%F0%A1%B7%B9%AB%D3%B3\">《唐山大地震》公映</a></td>");
	}
}
