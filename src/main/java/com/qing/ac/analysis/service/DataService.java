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
	public static final String SQL_URL = "CREATE TABLE URL(URL VARCHAR(100), STATUS INT)";
	public static final String SQL_VIDEO = "CREATE TABLE VIDEO(URL VARCHAR(100), UID VARCHAR(50), TITLE VARCHAR(200), PUBLIC_TIME VARCHAR(100), INTRO TEXT)";
	public static final String SQL_URL_REQUESTED = "CREATE TABLE URL_REQUESTED(URL VARCHAR(100), STATUS INT)";
	
	boolean createTable() {
		try {
			if(openConnection()) {
				Statement stmt = conn.createStatement();
				stmt.execute(SQL_URL);
				stmt.execute(SQL_VIDEO);
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
	public static final String URL = "jdbc:mysql://localhost:3306/ac";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	
	public boolean openConnection() {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int isRequested(String url) throws Exception {
		return 0;
	}
	
	public void saveRequestedURL(Request req) throws Exception {
		if(openConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO URL_REQUESTED(url, status) VALUES(?, '-1')");
			pstmt.setString(1, req.getUrl());
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}
	}
	
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
	
	public void saveUP(UP up) {
		
	}
	
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
	
	public void listUrl(Queue<Request> requestQueue) throws Exception {
		if(openConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT URL FROM URL WHERE STATUS = '-1'");
			while(rs.next()) {
				Request req = new Request(rs.getString("URL"));
				requestQueue.add(req);
			}
			rs.close();
			stmt.close();
			conn.close();
		}
	}
	
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
}
