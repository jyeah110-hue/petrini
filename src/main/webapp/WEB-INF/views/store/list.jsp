<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="pageId" value="store" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/ad-banner.jsp" %>
<style>
  .store-wrap { max-width:var(--inner-width); margin:32px auto 80px; padding:0 20px; display:flex; gap:28px; align-items:flex-start; }
  /* 사이드바 */
  .store-sidebar { width:220px; flex-shrink:0; }
  .store-sidebar-card { background:var(--bg-card); border:1px solid var(--border); border-radius:var(--radius-md); padding:20px; margin-bottom:16px; }
  .store-sidebar-title { font-size:14px; font-weight:800; color:var(--text-main); margin:0 0 14px; }
  .store-cat-list { list-style:none; padding:0; margin:0; display:flex; flex-direction:column; gap:2px; }
  .store-cat-list li a { display:flex; justify-content:space-between; padding:8px 10px; border-radius:var(--radius-sm); font-size:13px; color:var(--text-sub); text-decoration:none; transition:var(--transition); }
  .store-cat-list li a:hover { background:var(--primary-light); color:var(--primary-dark); }
  .store-cat-list li a.active { background:var(--primary-light); color:var(--primary-dark); font-weight:700; }
  .store-cat-list .cat-count { font-size:12px; color:var(--text-muted); }
  .store-cat-list li label { padding:8px 10px; border-radius:var(--radius-sm); transition:var(--transition); }
  .store-cat-list li label:hover { background:var(--primary-light); }
  .brand-item { list-style:none; }
  .price-range { display:flex; flex-direction:column; gap:8px; }
  .price-range input[type=range] { width:100%; accent-color:var(--primary); }
  .price-range-vals { display:flex; justify-content:space-between; font-size:12px; color:var(--text-muted); }
  /* 상품 영역 */
  .store-content { flex:1; min-width:0; }
  .store-toolbar { display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; }
  .store-result-count { font-size:14px; color:var(--text-sub); }
  .store-result-count strong { color:var(--text-main); font-weight:700; }
  .store-sort { display:flex; gap:8px; }
  .sort-btn { padding:6px 14px; border:1px solid var(--border); border-radius:50px; font-size:13px; color:var(--text-sub); background:#fff; cursor:pointer; transition:var(--transition); }
  .sort-btn:hover,.sort-btn.on { border-color:var(--primary); color:var(--primary); background:var(--primary-light); font-weight:600; }
  /* 상품 그리드 */
  .product-grid { display:grid; grid-template-columns:repeat(3,1fr); gap:20px; margin-bottom:32px; }
  .product-card { background:var(--bg-card); border:1px solid var(--border); border-radius:var(--radius-md); overflow:hidden; transition:var(--transition); cursor:pointer; }
  .product-card:hover { box-shadow:var(--shadow-md); transform:translateY(-3px); }
  .product-thumb-wrap { position:relative; }
  .product-thumb { width:100%; aspect-ratio:1/1; object-fit:cover; display:block; }
  .product-badge { position:absolute; top:10px; left:10px; background:var(--accent); color:#fff; font-size:11px; font-weight:700; padding:3px 8px; border-radius:20px; }
  .product-wish { position:absolute; top:10px; right:10px; width:32px; height:32px; border-radius:50%; background:rgba(255,255,255,.9); border:none; cursor:pointer; display:flex; align-items:center; justify-content:center; }
  .product-wish svg { width:16px; height:16px; stroke:var(--accent); fill:none; stroke-width:1.8; }
  .product-body { padding:14px; }
  .product-brand { font-size:11px; color:var(--text-muted); margin-bottom:4px; }
  .product-name { font-size:14px; font-weight:600; color:var(--text-main); margin-bottom:8px; line-height:1.4; display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; overflow:hidden; }
  .product-rating { display:flex; align-items:center; gap:4px; margin-bottom:8px; }
  .product-rating svg { width:13px; height:13px; fill:var(--yellow); stroke:none; }
  .product-rating span { font-size:12px; color:var(--text-muted); }
  .product-price { display:flex; align-items:baseline; gap:6px; }
  .price-sale { font-size:16px; font-weight:800; color:var(--text-main); }
  .price-rate { font-size:14px; font-weight:700; color:var(--accent); }
  .price-origin { font-size:12px; color:var(--text-muted); text-decoration:line-through; }
  .product-footer { padding:0 14px 14px; }
  .btn-cart { width:100%; padding:9px; border:none; border-radius:var(--radius-sm); background:var(--primary); color:#fff; font-size:13px; font-weight:700; cursor:pointer; transition:var(--transition); }
  .btn-cart:hover { background:var(--primary-dark); }
  /* 페이지네이션 */
  .pagination { display:flex; justify-content:center; gap:5px; }
  .page-btn { width:36px; height:36px; border-radius:var(--radius-sm); border:1px solid var(--border); background:#fff; font-size:13px; color:var(--text-sub); cursor:pointer; display:flex; align-items:center; justify-content:center; transition:var(--transition); }
  .page-btn:hover { border-color:var(--primary); color:var(--primary); }
  .page-btn.active { background:var(--primary); border-color:var(--primary); color:#fff; font-weight:700; }
  .page-btn svg { width:14px; height:14px; stroke:currentColor; fill:none; stroke-width:2; stroke-linecap:round; stroke-linejoin:round; }
  /* 검색창 */
  .store-search-box { display:flex; gap:8px; margin-bottom:16px; }
  .store-search-box input[type=text] { flex:1; padding:9px 14px; border:1px solid var(--border); border-radius:var(--radius-sm); font-size:14px; }
  .store-search-box button { padding:9px 20px; border:none; border-radius:var(--radius-sm); background:var(--primary); color:#fff; font-size:13px; font-weight:700; cursor:pointer; }
  .species-tabs { display:flex; gap:6px; margin-bottom:14px; }
  .species-tab { flex:1; text-align:center; padding:8px 0; border:1px solid var(--border); border-radius:var(--radius-sm); font-size:13px; color:var(--text-sub); text-decoration:none; }
  .species-tab.active { background:var(--primary); border-color:var(--primary); color:#fff; font-weight:700; }
  .age-filter { display:flex; gap:6px; margin-bottom:14px; }
  .age-chip { padding:6px 14px; border:1px solid var(--border); border-radius:20px; font-size:12px; color:var(--text-sub); text-decoration:none; }
  .age-chip.active { border-color:var(--primary); background:var(--primary-light); color:var(--primary-dark); font-weight:700; }
</style>

<form id="searchForm" method="get" action="${contextPath}/store">
  <input type="hidden" id="hSpecies" name="species" value="${search.species}">
  <input type="hidden" id="hAge" name="age" value="${search.age}">
  <input type="hidden" id="hType" name="type" value="${search.type}">
  <input type="hidden" id="hSort" name="sort" value="${search.sort}">
  <input type="hidden" id="hKeyword" name="keyword" value="${search.keyword}">
  <input type="hidden" id="hMaxPrice" name="maxPrice" value="${search.maxPrice}">
  <input type="hidden" id="hPage" name="curPage" value="${search.curPage}">
</form>

<div class="store-wrap">
  <%-- 사이드바 --%>
  <aside class="store-sidebar">
    <!--강아지/고양이-->
    <div class="species-tabs">
      <c:forEach var="category" items="${categoryList}">
        <c:if test="${category.depth == 2}">
          <a class="species-tab ${search.species == category.categoryId ? 'active' : ''}" 
             onclick="setFilter('species', '${category.categoryId}')">
              ${category.name}
          </a>
        </c:if>
      </c:forEach>
    </div>

    <!--상품종류-->
    <div class="store-sidebar-card">
      <div class="store-sidebar-title">카테고리</div>
      <ul class="store-cat-list">
        <li>
          <a class="${empty search.type?'active':''}" onclick="setFilter('type','')">전체
          </a>
        </li>
        <c:forEach var="category" items="${categoryList}">
          <c:if test="${category.depth == 3 && category.parentId == search.species}">
            <li>
              <a class="${search.type == category.categoryId ? 'active' : ''}"
                  onclick="setFilter('type', '${category.categoryId}')">
                ${category.name}
              </a>
            </li>
          </c:if>
        </c:forEach>
      </ul>
    </div>

    <%-- 나이 필터 --%>
    <c:set var="hasAgeOptions" value="false"/>
    <div class="store-sidebar-card">
      <c:forEach var="category" items="${categoryList}">
        <c:if test="${not empty search.type && category.parentId == search.type}">
          <c:set var="hasAgeOptions" value="true"/>
        </c:if>
      </c:forEach>
      <c:if test="${hasAgeOptions}">
        <div class="age-filter">
          <a class="age-chip ${empty search.age ? 'active' : ''}"
             onclick="setFilter('age', '')">전체
          </a>
          <c:forEach var="category" items="${categoryList}">
            <c:if test="${category.parentId == search.type}">
              <a class="age-chip ${search.age == category.categoryId ? 'active' : ''}"
                 onclick="setFilter('age', '${category.categoryId}')">
                 ${category.name}
              </a>
            </c:if>
          </c:forEach>
        </div>
      </c:if>
    </div>

    <!--브랜드-->
    <div class="store-sidebar-card">
      <div class="store-sidebar-title" style="display:flex;justify-content:space-between;align-items:center;">브랜드
        <c:if test="${fn:length(brandList) > 5}">
          <button type="button" onclick="openBrandModal()" style="font-size:12px;color:var(--text-muted);background:none;border:none;text-decoration:underline;cursor:pointer;">전체보기 &gt;</button>
        </c:if>
      </div>
      <ul class="store-cat-list" id="brandListAll">
        <c:forEach var="brand" items="${brandList}" varStatus="vs">
          <li class="brand-item${vs.index >= 5 ? ' brand-extra' : ''}" style="${vs.index >= 5 ? 'display:none;' : ''}">
            <label style="display:flex;align-items:center;gap:6px;cursor:pointer;">
              <input type="checkbox" name="brand" value="${brand.name}" 
                      ${not empty search.brand and search.brand.contains(brand.name) ? 'checked' : ''}
                      onchange="if (this.closest('#brandModalGrid')) { submitSearch(); }">
                      ${brand.name}
            </label>
          </li>
        </c:forEach>
      </ul>
      <c:if test="${fn:length(brandList) > 5}">
        <button type="button" id="brandMoreBtn" onclick="toggleBrandMore()" style="font-size:12px;color:var(--text-muted);background:none;border:none;cursor:pointer;margin-top:6px;">더보기</button>
      </c:if>        
    </div>

    <%-- 브랜드 전체보기 모달 --%>
    <div id="brandModalBg" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.4);z-index:1000;align-items:center;justify-content:center;">
      <div style="background:#fff;border-radius:12px;padding:24px;width:640px;max-height:80vh;overflow-y:auto;">

        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <strong style="font-size:16px;">브랜드 전체</strong>
          <button type="button" onclick="cancelBrandModal()" style="background:none;border:none;font-size:18px;cursor:pointer;">&times;</button>
        </div>
        <div id="brandModalGrid" style="display:grid;grid-template-columns:repeat(3,1fr);gap:10px 16px;"></div>
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:20px;padding-top:14px;border-top:1px solid var(--border,#eee);">
          <button type="button" onclick="clearBrandAll()" style="font-size:13px;color:var(--text-muted);background:none;border:none;text-decoration:underline;cursor:pointer;">선택한 필터 전체 삭제</button>
          <button type="button" onclick="applyBrandModal()" style="background:var(--primary,#2BAB82);color:#fff;border:none;border-radius:8px;padding:10px 24px;font-weight:700;cursor:pointer;">적용하기</button>
        </div>
      </div>
    </div>

    <%-- 가격대 슬라이더 --%>
    <div class="store-sidebar-card">
      <div class="store-sidebar-title">가격대</div>
      <div class="price-range">
        <input type="range" id="priceInput" min="0" max="150000" step="5000" value="${empty search.maxPrice ? 150000 : search.maxPrice}">
        <div class="price-range-vals">
          <span>0원</span>
          <span>100,000원 이하
            <c:choose>
              <c:when test="${empty search.maxPrice || search.maxPrice >= 150000}">전체</c:when>
              <c:otherwise><fmt:formatNumber value="${search.maxPrice}" pattern="#,###"/>원 이하</c:otherwise>
            </c:choose>          
          </span>
        </div>
      </div>
    </div>
  </aside>

  <%-- 상품 목록 --%>
  <div class="store-content">
      <%-- 검색 --%>
    <div class="store-search-box">
      <input type="text" id="keywordInput" value="${search.keyword}" placeholder="상품명 또는 브랜드로 검색">
      <button type="button" onclick="searchKeyword()">검색</button>
    </div>

    <%-- 상품 수 + 정렬 --%>
    <div class="store-toolbar">
      <div class="store-result-count">총 <strong>${fn:length(productList)}</strong>개 상품</div>
      <div class="store-sort">
        <a class="sort-btn ${search.sort == 'popular' ? 'on' : ''}" onclick="setFilter('sort', 'popular')">인기순</a>
        <a class="sort-btn ${search.sort == 'latest' ? 'on' : ''}" onclick="setFilter('sort', 'latest')">최신순</a>
        <a class="sort-btn ${search.sort == 'priceAsc' ? 'on' : ''}" onclick="setFilter('sort', 'priceAsc')">낮은가격</a>
        <a class="sort-btn ${search.sort == 'priceDesc' ? 'on' : ''}" onclick="setFilter('sort', 'priceDesc')">높은가격</a>
      </div>
    </div>

    <%-- 상품 그리드 --%>
    <div class="product-grid">
      <c:forEach var="product" items="${productList}">
        <div class="product-card" onclick="location.href='${contextPath}/store/detail?id=${product.productId}'">
          <div class="product-thumb-wrap">
            <img class="product-thumb" 
                 src="https://placehold.co/400x400/EAF7F2/2BAB82?text=상품" 
                 alt="${product.name}">
            <c:if test="${product.discountRate > 0}">
              <span class="product-badge">SALE</span>
            </c:if>
            <button type="button" class="product-wish" aria-label="찜하기">
              <svg viewBox="0 0 24 24">
                <path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78L12 21.23l8.84-8.84a5.5 5.5 0 000-7.78z"/>
              </svg>
            </button>
          </div>

          <div class="product-body">
            <div class="product-brand">${product.brand}</div>
            <div class="product-name">${product.name}</div>
            <div class="product-rating">
              <svg viewBox="0 0 24 24"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
              <span>4.8 (324)</span>
            </div>
            <div class="product-price">
              <c:choose>
                <c:when test="${product.discountRate > 0}">
                  <span class="price-rate">${product.discountRate}%</span>
                  <span class="price-sale"><fmt:formatNumber value="${product.sale}" pattern="#,###"/>원</span>
                  <span class="price-origin"><fmt:formatNumber value="${product.price}" pattern="#,###"/>원</span>
                </c:when>
                <c:otherwise>
                  <span class="price-sale"><fmt:formatNumber value="${product.sale}" pattern="#,###"/>원</span>
                </c:otherwise>
              </c:choose>             
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
    <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
    <span style="font-size:12px;color:#999;text-align:center;display:block;margin-top:8px">
      총 ${paging.totalCount}건
    </span>
  </div>
</div>

<script>
  document.querySelectorAll('.sort-btn').forEach(b => b.addEventListener('click', function(){
    document.querySelectorAll('.sort-btn').forEach(x => x.classList.remove('on'));
    this.classList.add('on');
  }));
  document.querySelectorAll('.btn-cart').forEach(b => b.addEventListener('click', e => {
    e.stopPropagation();
    alert('장바구니에 담았습니다!');
  }));
</script>

<script>
  function submitSearch() {
    const form = document.getElementById('searchForm');

    //기존 브랜드 hidden input 제거
    let oldBrands = form.querySelectorAll('input[name="brand"]');
    for(let i = 0; i < oldBrands.length; i++) {
      oldBrands[i].remove();
    }

    //체크된 브랜드를 hidden input으로 추가
    let newBrands = document.querySelectorAll('.brand-cb:checked');
    for (let i = 0; i < newBrands.length; i++) {
      let hBrand = document.createElement('input');
      hBrand.type = 'hidden';
      hBrand.name = 'brand';
      hBrand.value = newBrands[i].value;
      form.appendChild(hBrand);
    }

    let hiddens = form.querySelectorAll('input[type="hidden"]');
    for (let i = 0; i < hiddens.length; i++) {
      if (!hiddens[i].value) {
        hiddens[i].disabled = true;
      }
    }

    form.submit();
  }

  function setFilter(name, value) {
    // hidden input에 값 세팅
    let hMap = {
        species:  'hSpecies',
        category: 'hCategory',
        age:      'hAge',
        sort:     'hSort',
        page:     'hPage',
        maxPrice: 'hMaxPrice',
        keyword:  'hKeyword'
    };

    const el
  }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
