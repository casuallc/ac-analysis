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
	public static final String SQL_URL = "CREATE TABLE URL(URL VARCHAR(100), STATUS INT, INDEX INDEX_UR_URL(url))";
	public static final String SQL_VIDEO = "CREATE TABLE VIDEO(URL VARCHAR(100), UID VARCHAR(50), TITLE VARCHAR(200), PUBLIC_TIME VARCHAR(100), INTRO TEXT)";
	public static final String SQL_URL_REQUESTED = "CREATE TABLE URL_REQUESTED(URL VARCHAR(100), STATUS INT, INDEX INDEX_URL_REQUESTED_URL(url))";
	public static final String SQL_UP = "CREATE TABLE UP(URL VARCHAR(100), UID VARCHAR(15), STATUS INT, NICK VARCHAR(100), REGISTE_TIME VARCHAR(100), SEX VARCHAR(4), ADDRESS VARCHAR(100), QQ CHAR(14), LOVES VARCHAR(10), IMG VARCHAR(100), POSTS INT, FOLLOWERS INT, FOLLOWING INT, PRIMARY KEY KEY_UP_UID(uid))";
	
	public boolean createTable() {
		try {
			if(openConnection()) {
				Statement stmt = conn.createStatement();
				stmt.execute(SQL_URL);
				stmt.execute(SQL_VIDEO);
				stmt.execute(SQL_URL_REQUESTED);
				stmt.execute(SQL_UP);
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
	public static final String URL = "jdbc:mysql://120.55.181.102:3306/ac";
	public static final String USER = "qlooker";
	public static String PASSWORD = "DB#mysql315";
	
//	public static final String URL = "jdbc:mysql://localhost:3306/ac";
//	public static final String USER = "root";
//	public static String PASSWORD = "root";
	
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
	public void saveRequestedURL(Request req) throws Exception {
		if(openConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO URL_REQUESTED(url, status) VALUES(?, '-1')");
			pstmt.setString(1, req.getUrl());
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement("UPDATE URL SET STATUS = '1' WHERE URL = ?");
			pstmt.setString(1, req.getUrl());
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}
	}
	
	/**
	 * 保存分析出的video信息
	 * @param video
	 * @throws Exception
	 */
	public void saveVideo(Video video) throws Exception {
		if(openConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO VIDEO(URL, UID, TITLE, PUBLIC_TIME, INTRO) VALUES(?, ?, ?, ?, ?)");
			pstmt.setString(1, video.getUrl());
			pstmt.setString(2, video.getUid());
			pstmt.setString(3, video.getTitle());
			pstmt.setString(4, video.getPublishTime());
			pstmt.setString(5, video.getIntro());
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}
	}
	
	/**
	 * 保存UP信息
	 * @param up
	 * @throws Exception
	 */
	public void saveUP(UP up) throws Exception {
		if(openConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO UP(URL, UID, NICK, REGISTE_TIME, SEX, ADDRESS, QQ, POSTS, FOLLOWERS, FOLLOWING, LOVES, IMG) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
			conn.close();
		}
	}
	
	/**
	 * 保存分析出的url地址
	 * @param urlList
	 * @throws Exception
	 */
	public void saveURL(List<String> urlList) throws Exception {
		if(openConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO URL(url, status) VALUES(?, '-1')");
			for(String url : urlList) {
				pstmt.setString(1, url);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
			conn.close();
		}
	}
	
	/**
	 * 查询出出100条待请求的url
	 * @param requestQueue
	 * @throws Exception
	 */
	public void listUrl(Queue<Request> requestQueue) throws Exception {
		if(openConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT URL FROM URL WHERE STATUS = '-1' LIMIT 0, 100");
			while(rs.next()) {
				Request req = new Request(rs.getString("URL"));
				requestQueue.add(req);
			}
			rs.close();
			stmt.close();
			conn.close();
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
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(1) CNT FROM URL_REQUESTED WHERE URL = '"+req.getUrl()+"'");
			rs.next();
			int r = rs.getInt("CNT");
			rs.close();
			stmt.close();
			conn.close();
			return r;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
	
	public void deleteData() {
		try {
			if(openConnection()) {
				Statement stmt = conn.createStatement();
				stmt.execute("DELETE FROM URL");
				stmt.execute("DELETE FROM URL_REQUESTED");
				stmt.execute("DELETE FROM UP");
				stmt.execute("DELETE FROM VIDEO");
				stmt.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
