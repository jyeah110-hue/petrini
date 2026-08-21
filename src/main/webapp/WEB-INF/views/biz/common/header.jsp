<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- 사업자센터 전용 헤더 — 각 JSP에서 bizTypeLabel, bizTypeKey 변수를 먼저 set 해야 함 --%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title>PetCare 사업자센터 — ${memberInfo.name}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${contextPath}/resources/css/biz.css">
</head>
<body>
<div class="biz-page">

<header class="biz-header">
    <%-- 로고 --%>
    <a href="${contextPath}/" class="biz-header-logo">
        <svg width="26" height="26" viewBox="0 0 32 32" fill="none">
            <ellipse cx="16" cy="20" rx="9" ry="8" fill="#2BAB82"/>
            <ellipse cx="8"  cy="12" rx="3.2" ry="3.8" fill="#2BAB82"/>
            <ellipse cx="13" cy="9.5" rx="3.2" ry="3.8" fill="#2BAB82"/>
            <ellipse cx="19" cy="9.5" rx="3.2" ry="3.8" fill="#2BAB82"/>
            <ellipse cx="24" cy="12" rx="3.2" ry="3.8" fill="#2BAB82"/>
        </svg>
        <span>PetCare</span>
        <span class="biz-tag">BIZ</span>
    </a>

    <div class="biz-header-divider"></div>

    <%-- 업체명 + 업종 --%>
    <span class="biz-header-bizname">${memberInfo.name}</span>
    <span class="biz-header-type">${bizTypeLabel}</span>

    <%-- 우측 메뉴 --%>
    <div class="biz-header-right">
        <a href="${contextPath}/mypage" class="back-btn">
            <svg viewBox="0 0 24 24"><path d="M19 12H5"/><polyline points="12 19 5 12 12 5"/></svg>
            마이페이지로 돌아가기
        </a>
        <div class="biz-header-divider"></div>
        <a href="${contextPath}/member/logout">로그아웃</a>
    </div>
</header>

<!-- 토스트 메시지 -->
<div id="toast" style="
    position:fixed; top:50%; left:50%;
    transform:translate(-50%, -50%) scale(0.95);
    background:rgba(255,255,255, 0.85);
    backdrop-filter:blur(16px);
    -webkit-backdrop-filter:blur(16px);
    color:#1A1A2E;
    padding:16px 32px;
    border-radius:14px;
    font-size:14px; font-weight:600;
    border:1px solid rgba(43,171,130, 0.2);
    box-shadow:0 8px 32px rgba(0,0,0,.08), 0 2px 8px rgba(0,0,0,.04);
    z-index:9999;
    display:none;
    opacity:0;
    transition:opacity .35s ease, transform .35s ease;">
</div>

<c:if test="${not empty msg}">
<script>
    (function() {
        var toast = document.getElementById('toast');
        toast.textContent = '✓  ${msg}';
        toast.style.display = 'block';

        setTimeout(function() {
            toast.style.opacity = '1';
            toast.style.transform = 'translate(-50%, -50%) scale(1)';
        }, 30);

        setTimeout(function() {
            toast.style.opacity = '0';
            toast.style.transform = 'translate(-50%, -50%) scale(0.95)';
            setTimeout(function() {
                toast.style.display = 'none';
            }, 350);
        }, 2500);
    })();
</script>
</c:if>

<div class="biz-body">
