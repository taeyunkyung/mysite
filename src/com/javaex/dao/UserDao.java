package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

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

	// 가입
	public int insert(UserVo vo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "insert into users ";
			query += " values(seq_users_no.nextval, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());

			count = pstmt.executeUpdate();
			System.out.println(count + "건 등록되었습니다.(UserDao)");

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return count;
	}
	
	// 회원정보(1명)가져오기: 로그인
	public UserVo getUser(String id, String pw) {
		UserVo user = null;
		this.getConnection();

		try {
			String query = "";
			query += "select  no, ";
			query += "        name ";
			query += " from users ";
			query += " where id = ? ";
			query += " and password = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);

			rs = pstmt.executeQuery();
			while (rs.next() == true) {
				int uno = rs.getInt("no");
				String name = rs.getString("name");

				user = new UserVo();
				user.setNo(uno);
				user.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return user;
	}

	// 회원정보(1명)가져오기: 회원정보수정
	public UserVo getUser(int no) {
		UserVo user = null;
		this.getConnection();

		try {
			String query = "";
			query += "select  id, ";
			query += "        password, ";
			query += "        name, ";
			query += "        gender ";
			query += " from users ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();
			while (rs.next() == true) {
				String uid = rs.getString("id");
				String upw = rs.getString("password");
				String name = rs.getString("name");
				String gender = rs.getString("gender");

				user = new UserVo(no, uid, upw, name, gender);
			}

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return user;
	}

	// 수정
	public int update(UserVo vo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "update users ";
			query += " set password = ?, ";
			query += "     name = ?, ";
			query += "     gender = ? ";
			query += " where no = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, vo.getPassword());
			pstmt.setString(2, vo.getName());
			pstmt.setString(3, vo.getGender());
			pstmt.setInt(4, vo.getNo());

			count = pstmt.executeUpdate();
			System.out.println(count + "건 수정되었습니다.(UserDao)");

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		this.close();
		return count;
	}
}
