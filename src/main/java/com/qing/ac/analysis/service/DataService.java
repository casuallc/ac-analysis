package com.qing.ac.analysis.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Queue;

import com.qing.ac.analysis.model.UP;
import com.qing.ac.analysis.model.Video;

public class DataService {
	public static final String SQL_URL = "CREATE TABLE URL(URL VARCHAR(255), STATUS INT, INDEX INDEX_UR_URL(url))";
	public static final String SQL_URL_OLD = "CREATE TABLE URL_OLD(URL VARCHAR(255), STATUS INT)";
	public static final String SQL_URL_REQUESTED = "CREATE TABLE URL_REQUESTED(URL VARCHAR(255), STATUS INT, INDEX INDEX_URL_REQUESTED_URL(url))";
	
	// A站
	public static final String SQL_VIDEO = "CREATE TABLE VIDEO(URL VARCHAR(255), UID VARCHAR(50), TITLE VARCHAR(200), PUBLIC_TIME VARCHAR(100), INTRO TEXT)";
	public static final String SQL_UP = "CREATE TABLE UP(URL VARCHAR(255), UID VARCHAR(15), STATUS INT, NICK VARCHAR(100), REGISTE_TIME VARCHAR(100), SEX VARCHAR(4), ADDRESS VARCHAR(100), QQ CHAR(14), LOVES VARCHAR(10), IMG VARCHAR(100), POSTS INT, FOLLOWERS INT, FOLLOWING INT, PRIMARY KEY KEY_UP_UID(uid))";
	
	// B站
	public static final String SQL_B_UP = "CREATE TABLE B_UP (URL VARCHAR(255), UID VARCHAR(15), STATUS INT, NICK VARCHAR(100), REGISTE_TIME VARCHAR(100), SEX VARCHAR(4), ADDRESS VARCHAR(100), IMG VARCHAR(100), POSTS INT, FOLLOWERS INT, FOLLOWING INT, INTRO TEXT, PRIMARY KEY KEY_UP_UID(uid))";
	public static final String SQL_B_VIDEO = "CREATE TABLE B_VIDEO (URL VARCHAR(255), TITLE VARCHAR(200), PUBLISH_TIME VARCHAR(100), UID VARCHAR(50), VIEWS INT, DANMU INT, YB INT, COLLECTIONS INT, INTRO TEXT)";
	public boolean createTable() {
		try {
			if(openConnection()) {
				Statement stmt = conn.createStatement();
				stmt.execute(SQL_URL);
				stmt.execute(SQL_VIDEO);
				stmt.execute(SQL_URL_REQUESTED);
				stmt.execute(SQL_UP);
				stmt.execute(SQL_B_UP);
				stmt.execute(SQL_B_VIDEO);
				stmt.close();
				conn.close();
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	
	
	private Connection conn = null;
	public static final String DRIVER = "com.mysql.jdbc.Driver";
//	public static final String URL = "jdbc:mysql://120.55.181.102:3306/ac";
//	public static final String USER = "qlooker";
//	public static String PASSWORD = "DB#mysql315";
	
	public static final String URL = "jdbc:mysql://localhost:3306/ac";
	public static final String USER = "root";
	public static String PASSWORD = "root";
	
	public boolean openConnection() {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 更新处理过的url信息
	 * @param req
	 * @throws Exception
	 */
	public void saveRequestedURL(Request req, boolean hasRequested) throws Exception {
		if(openConnection()) {
			try {
				conn.setAutoCommit(false);
				PreparedStatement pstmt = null;
				if(!hasRequested) {
					pstmt = conn.prepareStatement("INSERT INTO URL_REQUESTED(url, status) VALUES(?, '-1')");
					pstmt.setString(1, req.getUrl());
					pstmt.executeUpdate();
					pstmt.close();
				}
				
				pstmt = conn.prepareStatement("INSERT INTO URL_OLD (URL, STATUS) SELECT URL, '1' FROM URL WHERE URL = ?");
				pstmt.setString(1, req.getUrl());
				pstmt.executeUpdate();
				pstmt.close();
				conn.commit();
			} catch (Exception e) {
				System.out.println("saveRequestedURL error " + req.getUrl());
				conn.rollback();
			} finally {
				conn.close();
			}
			
		}
	}
	
	/**
	 * 保存分析出的video信息
	 * @param video
	 * @throws Exception
	 */
	public void saveVideo(Video video) throws Exception {
		if(openConnection()) {
			try {
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO VIDEO(URL, UID, TITLE, PUBLIC_TIME, INTRO) VALUES(?, ?, ?, ?, ?)");
				pstmt.setString(1, video.getUrl());
				pstmt.setString(2, video.getUid());
				pstmt.setString(3, video.getTitle());
				pstmt.setString(4, video.getPublishTime());
				pstmt.setString(5, video.getIntro());
				pstmt.executeUpdate();
				pstmt.close();
			} finally {
				conn.close();
			}
		}
	}
	
	/**
	 * 保存UP信息
	 * @param up
	 * @throws Exception
	 */
	public void saveUP(UP up) throws Exception {
		if(openConnection()) {
			try {
				PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO UP(URL, UID, NICK, REGISTE_TIME, SEX, ADDRESS, QQ, POSTS, FOLLOWERS, FOLLOWING, LOVES, IMG) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setString(1, up.getUrl());
				pstmt.setString(2, up.getUid());
				pstmt.setString(3, up.getNick());
				pstmt.setString(4, up.getRegistTime());
				pstmt.setString(5, up.getSex());
				pstmt.setString(6, up.getAddress());
				pstmt.setString(7, up.getQQ());
				pstmt.setLong(8, up.getPosts());
				pstmt.setLong(9, up.getFollowers());
				pstmt.setLong(10, up.getFollowing());
				pstmt.setString(11, up.getLoves());
				pstmt.setString(12, up.getImg());
				pstmt.executeUpdate();
				pstmt.close();
			} finally {
				conn.close();
			}
		}
	}
	
	/**
	 * 保存分析出的video信息
	 * @param video
	 * @throws Exception
	 */
	public void saveBVideo(Video video) throws Exception {
		if(openConnection()) {
			try {
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO B_VIDEO(URL, UID, TITLE, PUBLIC_TIME, INTRO, VIEWS, DANMU, YB, COLLECTIONS) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setString(1, video.getUrl());
				pstmt.setString(2, video.getUid());
				pstmt.setString(3, video.getTitle());
				pstmt.setString(4, video.getPublishTime());
				pstmt.setString(5, video.getIntro());
				pstmt.setLong(6, video.getViews());
				pstmt.setLong(7, video.getDanmu());
				pstmt.setLong(8, video.getYb());
				pstmt.setLong(9, video.getCollections());
				pstmt.executeUpdate();
				pstmt.close();
			} finally {
				conn.close();
			}
		}
	}
	
	/**
	 * 保存UP信息
	 * @param up
	 * @throws Exception
	 */
	public void saveBUP(UP up) throws Exception {
		if(openConnection()) {
			try {
				PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO UP(URL, UID, NICK, REGISTE_TIME, SEX, ADDRESS, POSTS, FOLLOWERS, FOLLOWING, IMG) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setString(1, up.getUrl());
				pstmt.setString(2, up.getUid());
				pstmt.setString(3, up.getNick());
				pstmt.setString(4, up.getRegistTime());
				pstmt.setString(5, up.getSex());
				pstmt.setString(6, up.getAddress());
				pstmt.setLong(7, up.getPosts());
				pstmt.setLong(8, up.getFollowers());
				pstmt.setLong(9, up.getFollowing());
				pstmt.setString(10, up.getImg());
				pstmt.executeUpdate();
				pstmt.close();
			} finally {
				conn.close();
			}
		}
	}
	
	/**
	 * 保存分析出的url地址
	 * @param urlList
	 * @throws Exception
	 */
	public void saveURL(List<String> urlList) throws Exception {
		if(openConnection()) {
			try {
				conn.setAutoCommit(false);
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO URL(url, status) VALUES(?, '-1')");
				for(String url : urlList) {
					pstmt.setString(1, url);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
				pstmt.close();
				conn.commit();
			} finally {
				conn.close();
			}
		}
	}
	
	/**
	 * 查询出出100条待请求的url
	 * @param requestQueue
	 * @throws Exception
	 */
	public void listUrl(Queue<Request> requestQueue) throws Exception {
		if(openConnection()) {
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT URL FROM URL WHERE STATUS = '-1' LIMIT 0, 100");
				while(rs.next()) {
					Request req = new Request(rs.getString("URL"));
					requestQueue.add(req);
				}
				rs.close();
				stmt.close();
			} finally {
				conn.close();
			}
		}
	}
	
	/**
	 * 查询该url是否已处理过
	 * @param req
	 * @return
	 */
	public int checkRequested(Request req) {
		try {
			if(!openConnection()) {
				return -1;
			}
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT COUNT(1) CNT FROM URL_REQUESTED WHERE URL = '"+req.getUrl()+"'");
				rs.next();
				int r = rs.getInt("CNT");
				rs.close();
				stmt.close();
				return r;
			} finally {
				conn.close();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 分析数据完成
	 * @param hasTemplate
	 * @param requset
	 */
	public void updateRequestedUrl(boolean hasTemplate, Request request) {
		try {
			if(openConnection()) {
				try {
					PreparedStatement pstmt = null;
					int status = 1;
					if(hasTemplate) {
						status = 10; // 有正确的模板
					}
					pstmt = conn.prepareStatement("UPDATE URL_REQUESTED SET STATUS = ? WHERE URL = ?");
					pstmt.setLong(1, status);
					pstmt.setString(2, request.getUrl());
					pstmt.executeUpdate();
					pstmt.close();
				} finally {
					conn.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public void deleteData() {
		try {
			if(openConnection()) {
				try {
					Statement stmt = conn.createStatement();
					stmt.execute("DELETE FROM URL");
					stmt.execute("DELETE FROM URL_REQUESTED");
					stmt.execute("DELETE FROM UP");
					stmt.execute("DELETE FROM VIDEO");
					stmt.close();
				} finally {
					conn.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
