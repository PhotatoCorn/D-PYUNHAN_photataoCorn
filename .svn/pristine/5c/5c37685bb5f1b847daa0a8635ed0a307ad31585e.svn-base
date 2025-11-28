<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="stylesheet" href="/css/filter.css">

<style>
/* 공통 카드별 상단 영역 시작 */
body { /* 그냥 폰트 */
	font-family: 'Noto Sans KR', 'Source Sans Pro', sans-serif;
}

.card {
	border-radius: 1.5rem !important;
	overflow: hidden !important;
	box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15) !important;
}

.card-header .card-title {
	font-size: 1.2rem !important; /* 글자 크기 */
	font-weight: 700 !important;
	/* 글자 두께 살짝 굵게  400이 기본, 700이 bord정도 입니다. */
	padding-top: 4px !important; /* 상단패딩*/
	padding-bottom: 4px !important;
}
/* 공통 카드별 상단 영역 끝 */
.card-body {
	padding: 1rem !important;
}

.card-footer {
	padding: 1rem !important;
}
/* 공통 테이블 간격을 줄 간격 맞추기. table 생성시 꼭 사용하세요 시작 */
.tight-table, .tight-table th, .tight-table td {
	padding: 3px 5px !important; /* 테이블들 행간격 */
	overflow: hidden !important; /* 삐져나가는거 잡아줘요 */
	text-overflow: ellipsis; /* 길어진 글은 ...처리 */
	white-space: nowrap; /* 줄바꿈 X */
	text-align: center;
}
/* 줄무늬 효과(odd는 홀수, even은 짝수) */
.tight-table tbody tr:nth-child(odd) {
	background-color: #ffffff; /* 흰색 */
}

.tight-table tbody tr:nth-child(even) {
	background-color: #f1f4fa; /* 연한 회색(AdminLTE 느낌) */
}
/* 공통 테이블 간격을 줄 간격 맞추기. table 생성시 꼭 사용하세요 끝 */
/* 호버이벤트 시작 */
/* tight-table호버이벤트시작 */
.table tbody tr {
	transition: background-color 0.25s ease, box-shadow 0.25s ease,
		transform 0.2s ease;
}

.table tbody tr:hover {
	background-color: rgba(100, 140, 164, 0.12) !important; /* 은은한 파스텔 */
	box-shadow: 0 3px 12px rgba(0, 0, 0, 0.15); /* 은은한 그림자 */
	transform: translateY(-1px); /* 살짝 띄우기 */
	cursor: pointer;
}
/* 호버이벤트 끝 */

</style>


	<%@ include file="../include/header.jsp"%>

<section class="content" style="margin: 20px">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-12">

        <!-- 카드 시작 -->
        <div class="card bg-light">
            <!-- 필터 폼 시작 -->
              <form action="/" method="get" class="filter-form">
                <div class="filter-card">
                  <div class="filter-card-header">
                    <h3 class="filter-card-title">검색 필터</h3>
                    <button type="button" class="filter-btn-toggle" onclick="toggleFilter()">
                      <span class="filter-toggle-text">설정</span>
                      <i class="fas fa-chevron-down"></i>
                    </button>
                  </div>

                  <div class="filter-card-body collapsed" id="filterBody">
                    <div class="filter-row">

                      <div class="filter-cell">
                        <div class="filter-cell-label">검색 필드</div>
                        <div class="filter-cell-content">
                          <div class="filter-control-inline">
                            <label class="filter-checkbox">
                              <input type="checkbox" name="searchField" value="noticeSj" /> <span>순번</span>
                            </label>
                            <label class="filter-checkbox">
                              <input type="checkbox" name="searchField" value="noticeCn" /> <span>제목</span>
                            </label>
                            <label class="filter-checkbox">
                              <input type="checkbox" name="searchField" value="nm" /> <span>내용</span>
                            </label>
                            <label class="filter-checkbox">
                              <input type="checkbox" name="searchField" value="nm" /> <span>작성자</span>
                            </label>
                          </div>
                        </div>
                      </div>

                      <div class="filter-cell">
                        <div class="filter-cell-label">정렬</div>
                        <div class="filter-cell-content">
                          <div class="filter-control-inline">
                            <label class="filter-radio">
                              <input type="radio" name="sortOrder" value="desc" checked /> <span>최신순</span>
                            </label>
                            <label class="filter-radio">
                              <input type="radio" name="sortOrder" value="asc" /> <span>오래된순</span>
                            </label>
                          </div>
                        </div>
                      </div>

                      <div class="filter-cell">
                        <div class="filter-cell-label">상태</div>
                        <div class="filter-cell-content">
                          <select name="status" class="filter-control">
                            <option value="">전체</option>
                            <option value="1">활성</option>
                            <option value="0">비활성</option>
                          </select>
                        </div>
                      </div>

                      <div class="filter-cell">
                        <div class="filter-cell-label">날짜 범위</div>
                        <div class="filter-cell-content">
                          <div class="filter-input-group">
                            <input type="date" name="startDate" class="filter-control" value="${param.startDate}" />
                            <span class="filter-input-group-text">~</span>
                            <input type="date" name="endDate" class="filter-control" value="${param.endDate}" />
                          </div>
                        </div>
                      </div>

                    </div>
                  </div>

                  <div class="filter-search-footer">
                    <div class="filter-search-wrapper">
                      <input type="text" name="keyword" class="filter-input" placeholder="검색어..." value="${keyword}" />
                    </div>
                    <button type="submit" class="filter-btn filter-btn-primary">검색</button>
                    <a href="/" class="filter-btn filter-btn-secondary">초기화</a>
                  </div>
                </div>
              </form>
          
          <!-- 카드 헤더 시작 -->
          <div class="card-header d-flex justify-content-between align-items-center">
            <!-- 좌 제목 -->
            <h5 class="card-title mb-0">관리자 공지사항</h5>
            
            <!-- 우 검색 -->
            <div class="search-area ml-auto d-flex">
          
            </div>
          </div>
          <!-- 카드 헤더 끝 -->

          <!-- 테이블 시작 -->
          <table class="table tight-table">
            <thead>
              <tr>
                <th>순번</th>
                <th>제목</th>
                <th>작성일</th>
                <th>작성자</th>
                <th>파일</th>
                <th>구분</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="noticeVO" items="${articlePage.content}" varStatus="status">
                <tr style="cursor: pointer;" onclick="location.href='/notice/detail/${noticeVO.noticeSn}'" class="align-middle">
                  <td>${(articlePage.currentPage-1) * 10 + status.count}</td>
                  <td>
                    <a href="/notice/detail/${noticeVO.noticeSn}" class="text-decoration-none text-dark">${noticeVO.noticeSj}</a>
                  </td>
                  <td class="text-center text-muted">
                    <fmt:formatDate value="${noticeVO.noticeWritngDt}" pattern="yyyy-MM-dd" />
                  </td>
                  <td class="text-center">
                    <span class="badge bg-info text-dark">${noticeVO.nm}</span>
                  </td>
                  <td class="text-center">
                    <c:choose>
                      <c:when test="${noticeVO.fileGroupSn > 0}">
                        <i class="far fa-file text-primary"></i>
                      </c:when>
                      <c:otherwise>
                       <i class="far fa-file" style="color: #d0d0d0;"></i>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td class="text-center">
                    <c:choose>
                      <c:when test="${noticeVO.wnmpyNotice == 1}">
                        <span class="badge bg-warning text-dark">사내공지</span>
                      </c:when>
                      <c:otherwise>
                        <span class="badge bg-secondary">입주민공지</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <!-- 테이블 끝 -->

          <!-- 카드 푸터 시작 -->
          <div class="card-footer d-flex justify-content-between align-items-center">
            <!-- 중앙 페이징 -->
            <div class="flex-grow-1 text-center">
              ${articlePage.pagingArea}
            </div>
            <!-- 오른쪽 등록 버튼 -->
            <div>
              <a type="button" class="btn btn-info btn-sm mr-3" onclick="location.href='/notice/register'">공지등록</a>
            </div>
          </div>
          <!-- 카드 푸터 끝 -->

        </div>
        <!-- 카드 끝 -->

      </div>
    </div>
  </div>
</section>


<script>
function toggleFilter() {
  const filterBody = document.getElementById('filterBody');
  const toggleBtn = document.querySelector('.filter-btn-toggle i');
  
  filterBody.classList.toggle('collapsed');
  toggleBtn.classList.toggle('rotated');
}
</script>
	<%@ include file="../include/footer.jsp"%>
