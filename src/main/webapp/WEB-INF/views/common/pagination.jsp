<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
  ═══════════════════════════════════════════════════════════════
  공통 페이지네이션 컴포넌트

  [사용 조건]
  컨트롤러에서 model에 "paging" (PageVO 객체)를 담아야 한다.
    PageVO paging = new PageVO(page, pageSize, totalCount);
    model.addAttribute("paging", paging);

  [사용법]
  각 JSP에서 include만 하면 된다:
    <%@ include file="/WEB-INF/views/common/pagination.jsp" %>

  [페이지 이동 방식]
  현재 URL의 "page" 파라미터만 바꿔서 이동 (다른 파라미터는 유지)
  예) /store?keyword=사료&sort=latest&page=1
    → 3페이지 클릭 시: /store?keyword=사료&sort=latest&page=3
  ═══════════════════════════════════════════════════════════════
--%>

<c:if test="${paging != null && paging.totalPages > 1}">
<div class="pagination">
  <%-- ◀ 이전 블록 --%>
  <c:if test="${paging.hasPrev}">
    <a class="page-btn" href="javascript:goPage(${paging.startPage - 1})">
      <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6"/></svg>
    </a>
  </c:if>

  <%-- 페이지 번호 --%>
  <c:forEach var="p" begin="${paging.startPage}" end="${paging.endPage}">
    <a class="page-btn ${p == paging.page ? 'active' : ''}"
       href="javascript:goPage(${p})">${p}</a>
  </c:forEach>

  <%-- ▶ 다음 블록 --%>
  <c:if test="${paging.hasNext}">
    <a class="page-btn" href="javascript:goPage(${paging.endPage + 1})">
      <svg viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
    </a>
  </c:if>
</div>
</c:if>

<script>
  /*
   * 현재 URL에서 page 파라미터만 바꿔서 이동.
   * 다른 검색 조건(keyword, sort 등)은 그대로 유지된다.
   */
  function goPage(pageNo) {
    var url = new URL(window.location.href);
    url.searchParams.set('page', pageNo);
    window.location.href = url.toString();
  }
</script>