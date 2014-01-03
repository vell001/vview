package vell.bibi.vview;

import vell.bibi.vview.post.Post;
import vell.bibi.vview.post.PostManager;
import vell.bibi.vview.user.User;
import vell.bibi.vview.user.UserManager;

/**
 * @author VellBibi
 *权限管理
 */
public class RightManager {
	
	/**
	 * 用户能发布文章
	 * @param u
	 * @return
	 */
	public static boolean couldPublishPost(User u) {
		if(u == null) return false;
		if(u.getStatus() > 1) return true;
		return false;
	}
	public static boolean couldPublishPost(String username) {
		if(username == null) return false;
		return couldPublishPost(UserManager.getUserByName(username));
	}
	/**
	 * u能不能编辑p
	 * @param u
	 * @param p
	 * @return
	 */
	public static boolean couldEditPost(User u, Post p){
		if(u == null || p == null) return false;
		int u_status = u.getStatus();
		if(u_status <= 1) return false; //用户不能任何编辑
		if(p.getAuthor() == 0) return true; //p是空的
		if(u_status == 2 && p.getAuthor() == u.getId()) return true;
		if(u_status >= 3) return true;
		return false;
	}
	public static boolean couldEditPost(String username, Post p){
		if(username == null) return false;
		return couldEditPost(UserManager.getUserByName(username),p);
	}
	public static boolean couldEditPost(User u, String pid){
		if(u == null || pid == null) return false;
		return couldEditPost(u, PostManager.getPostById(pid));
	}
	public static boolean couldEditPost(String username, String pid){
		if(username == null || pid == null) return false;
		return couldEditPost(UserManager.getUserByName(username), PostManager.getPostById(pid));
	}
	
	/**
	 * 判断用户有无查看权限
	 * @param u
	 * @param p
	 * @return
	 */
	public static boolean couldViewPost(User u, Post p) {
		if(p == null) return false;
		if(p.getStatus()>=1) return true;
		if(u != null && couldEditPost(u,p)){//用户能编辑这篇文章
			return true;
		}
		return false;
	}
	
	/**
	 * 判断用户有无查看权限
	 * @param username
	 * @param p
	 * @return
	 */
	public static boolean couldViewPost(String username, Post p) {
		if(p == null) return false;
		if(p.getStatus()>=1) return true;
		if(username != null && couldEditPost(username,p)){//用户能编辑这篇文章
			return true;
		}
		return false;
	}
	
	public static boolean couldDeleteComment(User u, Post p){
		if(u == null || p == null) return false;
		if(u.getStatus()>=3) return true;
		if(u.getStatus() == 2 && u.getId() == p.getAuthor()) return true;
		return false;
	}
}
