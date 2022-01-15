package com.javaex.dao;

import java.util.List;

import com.javaex.vo.BoardVo;

public class TestDao {

	public static void main(String[] args) {
		
		BoardDao boardDao = new BoardDao();
		List<BoardVo> boardList = boardDao.boardList();
		System.out.println(boardList);

	}

}
