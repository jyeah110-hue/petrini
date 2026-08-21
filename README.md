# 🐾 PetCare - 반려동물 종합 케어 플랫폼

Spring Boot 기반의 반려동물 종합 케어 서비스입니다.

## 주요 기능

- **병원/미용/스튜디오/펫스테이** - 업체 검색, 상세 정보, 예약
- **펫 스토어** - 반려동물 용품 쇼핑, 장바구니, 주문/결제
- **커뮤니티** - 게시글 작성, 조회
- **나눔** - 유기동물 공고(공공API), 실종 신고, 재능 나눔
- **마이페이지** - 회원 정보, 반려동물 관리, 예약/주문 내역
- **사업자 페이지** - 업체별 대시보드, 예약 관리, 업체 정보 수정
- **관리자 페이지** - 회원/업체/상품/커뮤니티 관리
- **펫 지도** - 카카오맵 기반 주변 반려동물 시설 검색

## 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot 3.x, MyBatis |
| Frontend | JSP, HTML/CSS, JavaScript |
| Database | Oracle XE |
| API | 카카오맵, 공공데이터포털(유기동물), 토스페이먼츠 |
| Build | Maven |

## 로컬 실행 방법

### 1. 사전 준비
- JDK 17+
- Oracle XE (포트 1521)
- Maven

### 2. DB 세팅
```sql
-- sql/TEST_TABLE.sql 실행 (테이블 생성)
-- sql/TEST_DATA.sql 실행 (테스트 데이터 삽입)
```

### 3. 설정 파일 생성
`src/main/resources/application-local.properties.example`을 복사하여
`application-local.properties`로 이름 변경 후, 본인의 DB 정보와 API 키를 입력합니다.

```bash
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
```

### 4. 실행
```bash
./mvnw spring-boot:run
```
http://localhost:8080 접속

## 프로젝트 구조

```
src/main/java/com/petcare/petcare/
├── admin/          # 관리자
├── biz/            # 사업자 공통
├── common/         # API, 예외처리, 유틸
├── community/      # 커뮤니티
├── event/          # 이벤트/쿠폰
├── file/           # 파일 업로드
├── give/           # 나눔 (유기동물, 실종, 재능)
├── grooming/       # 미용
├── hospital/       # 동물병원
├── main/           # 메인 페이지
├── member/         # 회원
├── mypage/         # 마이페이지
├── pet/            # 반려동물
├── petmap/         # 펫 지도
├── reserve/        # 예약
├── stay/           # 펫스테이
├── store/          # 스토어
└── studio/         # 펫 스튜디오
```
