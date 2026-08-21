package com.petcare.petcare.common.vo;

import lombok.Getter;

/**
 * 페이지네이션 계산 결과를 담는 VO.
 * 
 * [사용법]
 * 컨트롤러에서:
 *   int totalCount = service.getCount(search);
 *   PageInfo pageInfo = new PageInfo(search.getPage(), search.getPageSize(), totalCount);
 *   model.addAttribute("pageInfo", pageInfo);
 * 
 * JSP에서:
 *   <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
 *   → pageInfo를 자동으로 읽어서 페이지 버튼을 렌더링
 * 
 * [예시] page=3, pageSize=12, totalCount=100, blockSize=5
 *   totalPages = 9 (100/12 올림)
 *   startPage  = 1 (현재 블록의 첫 페이지)
 *   endPage    = 5 (현재 블록의 마지막 페이지)
 *   hasPrev    = false (이전 블록 없음)
 *   hasNext    = true  (다음 블록 있음: 6~9)
 */

@Getter
public class PageVO {
    private final int page;        // 현재 페이지 (1부터)
    private final int pageSize;    // 한 페이지당 항목 수
    private final int totalCount;  // 전체 데이터 건수
    private final int totalPages;  // 전체 페이지 수
    private final int startPage;   // 페이지 블록의 시작 번호
    private final int endPage;     // 페이지 블록의 끝 번호
    private final boolean hasPrev; // 이전 블록 존재 여부
    private final boolean hasNext; // 다음 블록 존재 여부    

    public PageVO(int page, int pageSize, int totalCount, int blockSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalCount = totalCount;

        // 전체 페이지 수 (올림 계산)
        this.totalPages = (totalCount == 0) ? 1 : (int) Math.ceil((double) totalCount / pageSize);

        // 현재 블록 계산 (1-based)
        //   page=1~5 → block=1, page=6~10 → block=2
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        this.startPage = (currentBlock - 1) * blockSize + 1;
        this.endPage = Math.min(currentBlock * blockSize, this.totalPages);

        this.hasPrev = startPage > 1;
        this.hasNext = endPage < this.totalPages;
    }

    /** blockSize 기본값 5 */
    public PageVO(int page, int pageSize, int totalCount) {
        this(page, pageSize, totalCount, 5);
    }
}
