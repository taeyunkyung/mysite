package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/user");

		String action = request.getParameter("action");

		if ("joinForm".equals(action)) {
			System.out.println("user>joinForm");

			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");

		} else if ("join".equals(action)) {
			System.out.println("user>join");

			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");

			UserVo userVo = new UserVo(id, password, name, gender);
			UserDao userDao = new UserDao();
			userDao.insert(userVo);

			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");

		} else if ("loginForm".equals(action)) {
			System.out.println("user>loginForm");

			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");

		} else if ("login".equals(action)) {
			System.out.println("user>login");

			String id = request.getParameter("id");
			String pw = request.getParameter("password");

			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, pw);
			// System.out.println(authVo);

			if (authVo == null) {
				System.out.println("로그인 실패");

				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");

			} else {
				System.out.println("로그인 성공");

				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);

				WebUtil.redirect(request, response, "/mysite/main");			
			}
			
		} else if ("logout".equals(action)) {
			System.out.println("user>logout");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite/main");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
