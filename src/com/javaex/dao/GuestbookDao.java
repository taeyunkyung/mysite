package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestbookVo;

public class GuestbookDao {

	// 필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	// 메소드 일반
	private void getConnection() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	private void close() {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error." + e);
		}
	}

	// 등록
	public int insert(GuestbookVo vo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "insert into guestbook ";
			query += " values(seq_guestbook_no.nextval, ?, ?, ?, sysdate)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());

			count = pstmt.executeUpdate();
			System.out.println(count + "건 등록되었습니다.(GuestDao)");

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return count;
	}

	// 삭제
	public int delete(GuestbookVo vo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "delete from guestbook ";
			query += " where no = ?";	
			query += " and password = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());
			
			count = pstmt.executeUpdate();
			System.out.println(count + "건 삭제되었습니다.(GuestDao)");
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return count;
	}

	// 리스트 가져오기
	public List<GuestbookVo> getList() {
		List<GuestbookVo> gList = new ArrayList<GuestbookVo>();
		this.getConnection();

		try {
			String query = "";
			query += "select  no, ";
			query += "        name, ";
			query += "        content,";
			query += "        to_char(reg_date, 'YYYY-MM-DD HH24:MI:SS') reg_date ";
			query += " from guestbook ";
			query += " order by reg_date desc";

			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next() == true) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");

				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setContent(content);
				vo.setRegDate(regDate);
				
				gList.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return gList;
	}
}
