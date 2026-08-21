<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 관리자 전용 푸터 --%>
</div><%-- /adm-body --%>
</div><%-- /adm-page --%>
</body>

<script>
    /**
     * CSRF 토큰을 자동으로 포함하는 fetch 래퍼
     *
     * [동작]
     * 1. <meta name="_csrf"> 에서 토큰 값을 읽음
     * 2. 요청 headers 에 'X-CSRF-TOKEN' 을 자동 추가
     * 3. 원래 fetch 를 호출
     */
    window.csrfFetch = function(url, options) {
        options = options || {};
        options.headers = options.headers || {};

        var csrfMeta = document.querySelector('meta[name="_csrf"]');
        if (csrfMeta) {
            options.headers['X-CSRF-TOKEN'] = csrfMeta.getAttribute('content');
        }

        return fetch(url, options);
    };

    // 모든 $.ajax POST 요청에 CSRF 토큰 자동 포함
    if (typeof $ !== 'undefined' && $.ajaxSetup) {
        $.ajaxSetup({
            beforeSend: function(xhr) {
                var token = $('meta[name="_csrf"]').attr('content');
                if (token) {
                    xhr.setRequestHeader('X-CSRF-TOKEN', token);
                }
            }
        });
    }
</script>

</html>
