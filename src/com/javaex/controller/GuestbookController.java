package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;

@WebServlet("/guest")
public class GuestbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/guest");

		String action = request.getParameter("action");

		if ("add".equals(action)) {
			System.out.println("action=add");

			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");

			GuestbookDao dao = new GuestbookDao();
			GuestbookVo vo = new GuestbookVo(name, password, content);
			dao.insert(vo);
			
			WebUtil.redirect(request, response, "/mysite/guest");

		} else if ("deleteForm".equals(action)) {
			System.out.println("action=deleteform");

			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");

		} else if ("delete".equals(action)) {
			System.out.println("action=delete");

			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");

			GuestbookVo vo = new GuestbookVo();
			vo.setNo(no);
			vo.setPassword(password);
			
			GuestbookDao dao = new GuestbookDao();
			dao.delete(vo);
			
			WebUtil.redirect(request, response, "/mysite/guest");

		} else {
			GuestbookDao dao = new GuestbookDao();
			List<GuestbookVo> guestList = dao.getList();
			//System.out.println(guestList);
			
			request.setAttribute("gList", guestList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
