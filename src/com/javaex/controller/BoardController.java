package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/board");

		String action = request.getParameter("action");

		if ("read".equals(action)) {
			System.out.println("board>read");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.read(no);

			boardDao.hit(no);

			request.setAttribute("readVo", boardVo);

			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");

		} else if ("delete".equals(action)) {
			System.out.println("board>delete");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao boardDao = new BoardDao();
			boardDao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board");

		} else if ("writeForm".equals(action)) {
			System.out.println("board>writeForm");

			HttpSession session = request.getSession();
			UserVo authUser = (UserVo) session.getAttribute("authUser");

			if (authUser != null) {
				System.out.println("로그인상태");
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			} else {
				System.out.println("로그아웃상태: 비정상적인 접근");
				WebUtil.redirect(request, response, "/mysite/main");
			}

		} else if ("write".equals(action)) {
			System.out.println("board>write");

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = Integer.parseInt(request.getParameter("userNo"));

			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setUserNo(userNo);

			BoardDao boardDao = new BoardDao();
			boardDao.write(boardVo);

			WebUtil.redirect(request, response, "/mysite/board");

		} else if ("modifyForm".equals(action)) {
			System.out.println("board>modifyForm");

			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.read(no);

			request.setAttribute("readVo", boardVo);

			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");

		} else if ("modify".equals(action)) {
			System.out.println("board>modify");

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("boardNo"));

			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setNo(no);

			BoardDao boardDao = new BoardDao();
			boardDao.update(boardVo);

			WebUtil.redirect(request, response, "/mysite/board");

		} else {
			System.out.println("board>list");

			BoardDao boardDao = new BoardDao();
			List<BoardVo> boardList = boardDao.boardList();

			request.setAttribute("boardList", boardList);

			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
