package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

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
			System.out.println("error:" + e);
		}
	}

	// 리스트 가져오기
	public List<BoardVo> boardList() {
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		this.getConnection();

		try {
			String query = "";
			query += "select  bo.no boardNo, ";
			query += "        title, ";
			query += "        hit, ";
			query += "        to_char(reg_date, 'YY-MM-DD HH24:MI') regDate, ";
			query += "        user_no userNo, ";
			query += "        us.name userName ";
			query += " from board bo, users us ";
			query += " where bo.user_no = us.no ";
			query += " order by regDate desc";

			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next() == true) {
				int boardNo = rs.getInt("boardNo");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				int userNo = rs.getInt("userNo");
				String name = rs.getString("userName");

				BoardVo vo = new BoardVo(boardNo, title, "", hit, regDate, userNo, name);
				boardList.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return boardList;
	}

	// 게시글 1건 가져오기(읽기용)
	public BoardVo read(int boardNo) {
		BoardVo boardVo = null;
		this.getConnection();

		try {
			String query = "";
			query += "select  us.name writer, ";
			query += "        us.no userNo, ";
			query += "        hit, ";
			query += "        to_char(reg_date, 'YY-MM-DD HH24:MI') regDate, ";
			query += "        title, ";
			query += "        content ";
			query += " from board bo, users us ";
			query += " where bo.user_no = us.no ";
			query += " and bo.no = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardNo);

			rs = pstmt.executeQuery();
			while (rs.next() == true) {
				String writer = rs.getString("writer");
				int userNo = rs.getInt("userNo");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				String title = rs.getString("title");
				String content = rs.getString("content");

				boardVo = new BoardVo(boardNo, title, content, hit, regDate, userNo, writer);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return boardVo;
	}

	// 조회수 업데이트
	public void hit(int boardNo) {	
		this.getConnection();

		try {
			String query = "";
			query += "update board ";
			query += " set hit = nvl(hit, 0) + 1";
			query += " where no = ?";

			pstmt = conn.prepareStatement(query);			
			pstmt.setInt(1, boardNo);

			int count = pstmt.executeUpdate();
			System.out.println("조회수+" + count);

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();		
	}

	// 글쓰기
	public int write(BoardVo boardVo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, 0, sysdate, ?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUserNo());

			count = pstmt.executeUpdate();
			System.out.println(count + "건 등록되었습니다.(BoardDao)");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}

	// 게시글 삭제
	public int delete(int boardNo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "delete from board ";
			query += " where no = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardNo);

			count = pstmt.executeUpdate();
			System.out.println(count + "건 삭제되었습니다.(BoardDao)");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}

	// 게시글 수정
	public int update(BoardVo boardVo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "update board ";
			query += " set title = ?, ";
			query += "     content = ? ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getNo());

			count = pstmt.executeUpdate();
			System.out.println(count + "건 수정되었습니다.(BoardDao)");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}
}
