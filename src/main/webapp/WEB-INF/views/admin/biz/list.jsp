<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="adminPage" value="biz-list" />
<%@ include file="/WEB-INF/views/admin/common/header.jsp" %>
<%@ include file="/WEB-INF/views/admin/common/sidebar.jsp" %>

<style>
    .biz-apply-card {
        background:#fff; border:1px solid #E4E6ED; border-radius:12px;
        margin-bottom:14px; overflow:hidden; transition:box-shadow .2s;
    }.biz-apply-card:hover { box-shadow:0 4px 16px rgba(0,0,0,.08); }
    .biz-apply-head {
        display:flex; align-items:center; gap:14px;
        padding:16px 20px; border-bottom:1px solid #E4E6ED; background:#FAFBFC;
    }.biz-apply-type {
        width:42px; height:42px; border-radius:10px;
        display:flex; align-items:center; justify-content:center; flex-shrink:0;
    }.biz-apply-type svg { width:22px; height:22px; fill:none; stroke-width:1.8; stroke-linecap:round; stroke-linejoin:round; }
    .biz-apply-type.hospital   { background:#E0F2FE; } .biz-apply-type.hospital svg   { stroke:#0284C7; }
    .biz-apply-type.stay       { background:#F3E8FF; } .biz-apply-type.stay svg       { stroke:#9333EA; }
    .biz-apply-type.grooming   { background:#FDF2F8; } .biz-apply-type.grooming svg   { stroke:#DB2777; }
    .biz-apply-name   { font-size:15px; font-weight:700; color:#1A1A2E; }
    .biz-apply-meta   { font-size:12px; color:#999; margin-top:2px; }
    .biz-apply-date   { margin-left:auto; font-size:12px; color:#999; }
    .biz-apply-body {
        display:grid; grid-template-columns:repeat(4,1fr);
        padding:0; border-bottom:1px solid #E4E6ED;
    }.biz-apply-field { padding:14px 20px; border-right:1px solid #E4E6ED; }
    .biz-apply-field:last-child { border-right:none; }
    .biz-apply-field label { font-size:11px; color:#999; font-weight:600; display:block; margin-bottom:4px; }
    .biz-apply-field span  { font-size:13px; color:#1A1A2E; }
    .biz-apply-foot {
        display:flex; justify-content:space-between; align-items:center;
        padding:12px 20px;
    }.biz-doc-link { font-size:12px; color:#3B5BDB; text-decoration:underline; cursor:pointer; }
    .biz-action-area { display:flex; gap:8px; align-items:center; }
    .biz-reject-input {
        border:1px solid #E4E6ED; border-radius:6px;
        padding:6px 12px; font-size:12px; color:#333;
        outline:none; width:220px; display:none;
    }
</style>

<main class="adm-main">
    <div class="adm-page-head">
        <div class="adm-page-head-left">
            <h1 class="adm-page-title">사업자 승인 관리</h1>
            <p class="adm-page-desc">사업자 등록 신청을 검토하고 승인·반려 처리하세요.</p>
        </div>
    </div>
    <%-- 탭 --%>
    <div style="display:flex;gap:0;border-bottom:2px solid #E4E6ED;margin-bottom:20px">
        <a href="${contextPath}/admin/biz/list?status=PENDING"
           style="padding:10px 20px;font-size:14px;font-weight:${status eq 'PENDING' ? '700' : '600'};color:${status eq 'PENDING' ? '#3B5BDB' : '#999'};text-decoration:none;border:none;background:none;border-bottom:2px solid ${status eq 'PENDING' ? '#3B5BDB' : 'transparent'};margin-bottom:-2px">
            대기 <span style="background:#EEF2FF;color:#3B5BDB;font-size:11px;padding:1px 7px;border-radius:20px;margin-left:4px">${counts.PENDING}</span>
        </a>
        <a href="${contextPath}/admin/biz/list?status=APPROVED"
           style="padding:10px 20px;font-size:14px;font-weight:${status eq 'APPROVED' ? '700' : '600'};color:${status eq 'APPROVED' ? '#3B5BDB' : '#999'};text-decoration:none;border:none;background:none;border-bottom:2px solid ${status eq 'APPROVED' ? '#3B5BDB' : 'transparent'};margin-bottom:-2px">
            승인완료 <span style="background:#F0FDF4;color:#16A34A;font-size:11px;padding:1px 7px;border-radius:20px;margin-left:4px">${counts.APPROVED}</span>
        </a>
        <a href="${contextPath}/admin/biz/list?status=REJECTED"
           style="padding:10px 20px;font-size:14px;font-weight:${status eq 'REJECTED' ? '700' : '600'};color:${status eq 'REJECTED' ? '#3B5BDB' : '#999'};text-decoration:none;border:none;background:none;border-bottom:2px solid ${status eq 'REJECTED' ? '#3B5BDB' : 'transparent'};margin-bottom:-2px">
            반려 <span style="background:#FEF2F2;color:#DC2626;font-size:11px;padding:1px 7px;border-radius:20px;margin-left:4px">${counts.REJECTED}</span>
        </a>
    </div>

    <c:choose>
        <c:when test="${empty applyList}">
            <p style="text-align:center;color:#999;padding:48px 0">해당 상태의 사업자 신청이 없습니다.</p>
        </c:when>
        <c:otherwise> 
            <c:forEach var='apply' items='${applyList}'>   
                <div class="biz-apply-card">
                    <div class="biz-apply-head">
                        <c:set var="typeClass" value="hospital"/>
                        <c:if test="${apply.type eq 'STAY'}"><c:set var="typeClass" value="stay"/></c:if>
                        <c:if test="${apply.type eq 'GROOMING'}"><c:set var="typeClass" value="grooming"/></c:if>
                        <c:if test="${apply.type eq 'STUDIO'}"><c:set var="typeClass" value="studio"/></c:if>
                        <div class="biz-apply-type ${typeClass}">
                            <svg viewBox="0 0 24 24"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><rect x="9" y="10" width="6" height="11" rx="1"/><line x1="12" y1="13" x2="12" y2="17"/><line x1="10" y1="15" x2="14" y2="15"/></svg>
                        </div>
                        <div>
                            <div class="biz-apply-name">${apply.name}</div>
                            <div class="biz-apply-meta">${apply.type} · 대표자: ${apply.ceo}</div>
                        </div>
                        <span class="adm-badge wait" style="margin-left:12px">승인 대기</span>
                        <span class="biz-apply-date">신청일: ${apply.created}</span>
                    </div>
                    <div class="biz-apply-body">
                        <div class="biz-apply-field"><label>사업자 등록번호</label><span>${apply.bizNo}</span></div>
                        <div class="biz-apply-field"><label>사업장 주소</label><span>${apply.addr1} ${apply.addr2}</span></div>
                        <div class="biz-apply-field"><label>연락처</label><span>${apply.phone}</span></div>
                        <div class="biz-apply-field"><label>신청 계정</label><span>${item.memberId}</span></div>
                    </div>
                    <div class="biz-apply-foot">
                        <div>
                            <span class="biz-doc-link" onclick="alert('서류 미리보기')">사업자등록증 보기</span>
                            <span class="biz-doc-link" style="margin-left:14px" onclick="alert('서류 미리보기')">영업신고증 보기</span>
                        </div>
                        <div class="biz-action-area">
                            <a href="${contextPath}/admin/biz/detail?memberId=${apply.memberId}" class="adm-btn blue">상세 검토</a>
                            <input type="text" class="biz-reject-input" id="reject1" placeholder="반려 사유를 입력하세요">
                            <c:if test="${apply.status eq 'PENDING'}">
                                <form method="post" action="${contextPath}/admin/biz/approve" style="display:inline" onsubmit="return confirm('${item.bizName} 신청을 승인하시겠습니까?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    <button type="button" class="adm-btn gray btn-reject" data-id="${apply.memberId}">반려</button>
                                    <button type="button" class="adm-btn green btn-approve" data-id="${apply.memberId}" data-name="${apply.name}">승인</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>

<script>
    document.querySelectorAll('.btn-approve').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var name = this.dataset.name;
            var memberId = this.dataset.id;

            if (!confirm(name + '을(를) 승인하시겠습니까?'))
                return;
            
            csrfFetch('${contextPath}/admin/biz/approve', { 
                method:'POST', 
                headers: { 'Content-Type':'application/x-www-form-urlencoded'},
                body: 'memberId=' + memberId + '&action=APPROVED'})
            .then(function(result) { return result.text(); })
            .then(function(data) {
                if (data === 'OK') {
                    alert("승인 처리되었습니다.");
                    location.reload();
                }
            })
            .catch(function() { alert("처리 중 오류가 발생했습니다.")});
        });
    });

    document.querySelectorAll('.btn-reject').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var input = this.closest('.biz-action-area').querySelector('.biz-reject-input');
            
            // 첫 클릭: 입력란 보여주기
            if (input.style.display === 'none' || !input.style.display) {
                input.style.display = 'block';
                input.focus();
                return;
            }

            // 두 번째 클릭: 사유 확인 후 전송
            var reason = input.value.trim();
            if (!reason) {
                alert('반려 사유를 입력해 주세요.');
                input.focus();
                return;
            }

            var memberId = this.dataset.id;
            csrfFetch('${contextPath}/admin/biz/approve', {
                method: 'POST',
                headers: { 'Content-Type':'application/x-www-form-urlencoded'},
                body: 'memberId=' + memberId
                        + '&action=REJECT' 
                        + '&reason=' + encodeURIComponent(reason)
            })
            .then(function(result) { return result.text(); })
            .then(function(data) {
                if (data === 'OK') {
                    alert('반려 처리되었습니다.');
                    location.reload();
                }                
            })
            .catch(function() { alert("처리 중 오류가 발생했습니다.")});
        });
    });
</script>

<%@ include file="/WEB-INF/views/admin/common/footer.jsp" %>
