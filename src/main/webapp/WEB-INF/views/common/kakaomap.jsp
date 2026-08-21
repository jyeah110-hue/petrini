<%--
  common/kakaomap.jsp — 카카오맵 공통 초기화 (compile-time <%@ include %> 전용)

  컨테이너 div :  id="kakao-map"  (고정)

  Model에서 읽는 값 (KakaoMapService로 세팅):
    kakaoJsApiKey  Kakao JS API 키
    mapLat         초기 중심 위도
    mapLng         초기 중심 경도
    markersJson    마커 JSON 배열
                   - 1개 → 상세페이지 모드 (인포윈도우 항상 열림)
                   - 2개 이상 → 리스트 모드 (인포윈도우 hover로 열림)
    markerLink     마커 클릭 시 이동할 URL prefix (예: '/hospital/detail?id=')
                   설정 안 하면 링크 없이 이름만 표시

  JSP에서 include 전에 설정 가능한 변수:
    skipAutoMarkers  "true" → 마커 자동 생성을 건너뜀
                     (페이지 JS에서 직접 마커를 관리할 때 사용)

  include 후 JS 전역:
    window.kakaoMap  – kakao.maps.Map
    window.kakaoPs   – kakao.maps.services.Places
--%>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoJsApiKey}&autoload=true&libraries=services"></script>

<script>
  kakao.maps.load(function () {
      var el = document.getElementById('kakao-map');
      if (!el) { 
        console.warn('[kakaomap] #kakao-map 컨테이너를 찾을 수 없습니다.'); 
        return; 
      }
  
      var lat = '${mapLat}' || '37.5665';
      var lng = '${mapLng}' || '126.9780';

      var coords = new kakao.maps.LatLng(parseFloat(lat), parseFloat(lng));
      var map    = new kakao.maps.Map(el, { center: coords, level: 6 });
  
      map.addControl(new kakao.maps.MapTypeControl(), kakao.maps.ControlPosition.TOPRIGHT);
      map.addControl(new kakao.maps.ZoomControl(),    kakao.maps.ControlPosition.RIGHT);
  
      /* 전역 노출 */
      window.kakaoMap = map;
      window.kakaoPs  = new kakao.maps.services.Places();
      
      /* ── skipAutoMarkers가 true이면 여기서 끝 ── */
      /* 페이지 JS에서 직접 마커를 관리하는 경우 사용 */
      var skip = '${skipAutoMarkers}';
      if (skip === 'true') {
          return;
      }

      /* ── markersJson으로 마커 생성 ── */
      var markersJson = '${markersJson}';
      if (!markersJson || markersJson === '[]') {
          return;
      }

      var places   = JSON.parse(markersJson);
      var linkBase = '${markerLink}';

      /* ────────────────────────────────────────────────
         마커 1개 → 상세페이지 모드
         인포윈도우가 항상 열려 있음
         ──────────────────────────────────────────────── */
      if (places.length === 1) {
          var h   = places[0];
          var pos = new kakao.maps.LatLng(h.lat, h.lng);
          var marker = new kakao.maps.Marker({ position: pos, map: map });

          new kakao.maps.InfoWindow({
              content: '<div style="padding:8px 14px;font-size:13px;font-weight:800;'
                     + 'color:#1A1A2E;white-space:nowrap;">' + h.name + '</div>'
          }).open(map, marker);
      }

      /* ────────────────────────────────────────────────
         마커 2개 이상 → 리스트 모드
         마우스 올리면 인포윈도우 열림, 떠나면 닫힘
         ──────────────────────────────────────────────── */
      else {
          var bounds = new kakao.maps.LatLngBounds();

          for (var i = 0; i < places.length; i++) {
              var h   = places[i];
              var pos = new kakao.maps.LatLng(h.lat, h.lng);
              var marker = new kakao.maps.Marker({ position: pos, map: map });
              bounds.extend(pos);

              /* 인포윈도우 내용: markerLink 있으면 링크 포함 */
              var contentHtml = '';
              if (linkBase) {
                  contentHtml = '<div style="padding:8px 12px;font-size:13px;font-weight:800;'
                     + 'color:#1A1A2E;white-space:nowrap;">'
                     + '<a href="' + linkBase + h.id + '" '
                     + 'style="color:inherit;text-decoration:none">' + h.name + '</a></div>';
              } else {
                  contentHtml = '<div style="padding:8px 12px;font-size:13px;font-weight:800;'
                     + 'color:#1A1A2E;white-space:nowrap;">' + h.name + '</div>';
              }

              var infowindow = new kakao.maps.InfoWindow({ content: contentHtml });

              /* 클로저 문제 해결용 즉시실행 함수 */
              (function(m, iw) {
                  kakao.maps.event.addListener(m, 'mouseover', function() {
                      iw.open(map, m);
                  });
                  kakao.maps.event.addListener(m, 'mouseout', function() {
                      iw.close();
                  });
              })(marker, infowindow);
          }

          /* 모든 마커가 보이도록 지도 범위 자동 조정 */
          map.setBounds(bounds);
      }
  });
</script>
