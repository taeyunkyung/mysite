<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Site</title>
<link href="/mysite/assets/css/mysite.css" rel="stylesheet"	type="text/css">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
</head>

<body>
	<div id="wrap">

		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>

		<div id="container" class="clearfix">
			<div id="aside">
				<h2>게시판</h2>
				<ul>
					<li><a href="">일반게시판</a></li>
					<li><a href="">댓글게시판</a></li>
				</ul>
			</div>
			<!-- //aside -->

			<div id="content">

				<div id="content-head">
					<h3>게시판</h3>
					<div id="location">
						<ul>
							<li>홈</li>
							<li>게시판</li>
							<li class="last">일반게시판</li>
						</ul>
					</div>
					<div class="clear"></div>
				</div>
				<!-- //content-head -->

				<div id="board">
					<div id="list">
						<form action="/mysite/board" method="get">
							<div class="form-group text-right">
								<input type="text">
								<button type="submit" id=btn_search>검색</button>
							</div>
						</form>

						<table>
							<thead>
								<tr>
									<th>번호</th>
									<th>제목</th>
									<th>글쓴이</th>
									<th>조회수</th>
									<th>작성일</th>
									<th>관리</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${requestScope.boardList}" var="boardVo">
									<tr>
										<td>${boardVo.no}</td>
										<td class="text-left"><a href="/mysite/board?action=read&no=${boardVo.no}">
												${boardVo.title}</a></td>
										<td>${boardVo.name}</td>
										<td>${boardVo.hit}</td>
										<td>${boardVo.regDate}</td>

										<c:choose>
											<c:when test="${boardVo.userNo == authUser.no}">
												<td><a
													href="/mysite/board?action=delete&no=${boardVo.no}">[삭제]</a></td>
											</c:when>
											<c:otherwise>
												<td>삭제불가</td>
											</c:otherwise>
										</c:choose>

									</tr>
								</c:forEach>
							</tbody>
						</table>

						<div id="paging">
							<ul>
								<li><a href="">◀</a></li>
								<li><a href="">1</a></li>
								<li><a href="">2</a></li>
								<li><a href="">3</a></li>
								<li><a href="">4</a></li>
								<li class="active"><a href="">5</a></li>
								<li><a href="">6</a></li>
								<li><a href="">7</a></li>
								<li><a href="">8</a></li>
								<li><a href="">9</a></li>
								<li><a href="">10</a></li>
								<li><a href="">▶</a></li>
							</ul>


							<div class="clear"></div>
						</div>

						<c:choose>
							<c:when test="${empty authUser}"></c:when>							
							<c:otherwise>
								<a id="btn_write" href="/mysite/board?action=writeForm">글쓰기</a>
							</c:otherwise>
						</c:choose>

					</div>
					<!-- //list -->
				</div>
				<!-- //board -->
			</div>
			<!-- //content  -->

		</div>
		<!-- //container  -->

		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>

	</div>
	<!-- //wrap -->

</body>
</html>