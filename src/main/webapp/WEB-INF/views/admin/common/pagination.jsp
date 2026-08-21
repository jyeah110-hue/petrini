<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  관리자 전용 페이지네이션.
  사용법은 common/pagination.jsp와 동일.
  CSS 클래스만 admin 스타일(adm-pagination, adm-page-btn) 사용.
--%>
<c:if test="${pageInfo != null && pageInfo.totalPages > 1}">
<div class="adm-pagination" style="margin:0">

  <c:if test="${pageInfo.hasPrev}">
    <button class="adm-page-btn" onclick="goPage(${pageInfo.startPage - 1})">
      <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6"/></svg>
    </button>
  </c:if>

  <c:forEach var="p" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
    <button class="adm-page-btn ${p == pageInfo.page ? 'active' : ''}"
            onclick="goPage(${p})">${p}</button>
  </c:forEach>

  <c:if test="${pageInfo.hasNext}">
    <button class="adm-page-btn" onclick="goPage(${pageInfo.endPage + 1})">
      <svg viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
    </button>
  </c:if>

</div>

<script>
  function goPage(pageNo) {
    var url = new URL(window.location.href);
    url.searchParams.set('page', pageNo);
    window.location.href = url.toString();
  }
</script>
</c:if>
