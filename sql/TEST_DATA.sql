INSERT INTO MEMBER (ID, PASSWORD, NAME, NICKNAME, EMAIL, PHONE, ZIPCODE, ADDR1, ADDR2, POINT, ROLE, CREATED)
VALUES ('user1', '$2a$10$qIkpgyhoGUjLySmgmn8O6OldY5gulqDQBB0I1CFE7Rfn3raFElZNa', '����1', '����1', 'user1@petcare.com', '010-1111-1111', null, null, null, 3000, 'USER', SYSDATE);
INSERT INTO MEMBER (ID, PASSWORD, NAME, NICKNAME, EMAIL, PHONE, ZIPCODE, ADDR1, ADDR2, POINT, ROLE, CREATED)
VALUES ('user2', '$2a$10$qIkpgyhoGUjLySmgmn8O6OldY5gulqDQBB0I1CFE7Rfn3raFElZNa', '����2', '����2', 'user1@petcare.com', '010-2222-2222', null, null, null, 3000, 'USER', SYSDATE);
INSERT INTO MEMBER (ID, PASSWORD, NAME, NICKNAME, EMAIL, PHONE, ZIPCODE, ADDR1, ADDR2, POINT, ROLE, CREATED)
VALUES ('user3', '$2a$10$qIkpgyhoGUjLySmgmn8O6OldY5gulqDQBB0I1CFE7Rfn3raFElZNa', '����3', '����3', 'user1@petcare.com', '010-3333-3333', null, null, null, 3000, 'USER', SYSDATE);
INSERT INTO MEMBER (ID, PASSWORD, NAME, NICKNAME, EMAIL, PHONE, ZIPCODE, ADDR1, ADDR2, POINT, ROLE, CREATED)
VALUES ('user4', '$2a$10$qIkpgyhoGUjLySmgmn8O6OldY5gulqDQBB0I1CFE7Rfn3raFElZNa', '����4', '����4', 'user1@petcare.com', '010-4444-4444', null, null, null, 3000, 'USER', SYSDATE);
INSERT INTO MEMBER (ID, PASSWORD, NAME, NICKNAME, EMAIL, PHONE, ZIPCODE, ADDR1, ADDR2, POINT, ROLE, CREATED)
VALUES ('user5', '$2a$10$qIkpgyhoGUjLySmgmn8O6OldY5gulqDQBB0I1CFE7Rfn3raFElZNa', '����5', '����5', 'user1@petcare.com', '010-5555-5555', null, null, null, 3000, 'USER', SYSDATE);
INSERT INTO MEMBER (ID, PASSWORD, NAME, EMAIL, PHONE, ZIPCODE, ADDR1, ADDR2, POINT, ROLE, AGREESERVICE, AGREEPRIVACY, AGREELOCATION, AGREEMARKETING, CREATED)
VALUES ('admin', '$2a$10$qIkpgyhoGUjLySmgmn8O6OldY5gulqDQBB0I1CFE7Rfn3raFElZNa', '������', 'admin@petcare.com', '010-1234-5678', null, null, null, 3000, 'ADMIN', 'Y', 'Y', 'Y', 'Y', SYSDATE);


-- ═══════════════════════════════════════════════════════════
--  PetCare Store 더미 데이터 (Oracle)
--  페이지네이션 테스트용 — 카테고리 + 상품 50개
-- ═══════════════════════════════════════════════════════════

-- ── 1. 카테고리 (PRODUCT_CATEGORY) ─────────────────────────
-- depth 1: 전체
-- depth 2: 강아지 / 고양이
-- depth 3: 사료, 간식, 용품, 영양제
-- depth 4: 퍼피, 어덜트, 시니어

-- 기존 데이터 삭제 (필요 시)
-- DELETE FROM PRODUCT;
-- DELETE FROM PRODUCT_CATEGORY;

-- depth 1 (루트)
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (0, '전체', 1, 1);

-- depth 2 (동물 종류)
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (1, '강아지', 2, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (1, '고양이', 2, 1);

-- depth 3 (상품 종류 - 강아지)
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (10, '사료', 3, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (10, '간식', 3, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (10, '용품', 3, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (10, '영양제', 3, 1);

-- depth 3 (상품 종류 - 고양이)
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (20, '사료', 3, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (20, '간식', 3, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (20, '용품', 3, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (20, '영양제', 3, 1);

-- depth 4 (나이 - 강아지 사료)
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (101, '퍼피', 4, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (101, '어덜트', 4, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (101, '시니어', 4, 1);

-- depth 4 (나이 - 고양이 사료)
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (201, '키튼', 4, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (201, '어덜트', 4, 1);
INSERT INTO PRODUCT_CATEGORY (PARENT_ID, NAME, DEPTH, STATUS) VALUES (201, '시니어', 4, 1);


-- ── 2. 상품 (PRODUCT) 50개 ─────────────────────────────────
-- 컬럼: ID, CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE

-- 강아지 사료 (category_id = 101) — 12개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-001', '로얄캐닌 미니 인도어 어덜트 3kg',        '로얄캐닌',   101, 45000, 39900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-002', '오리젠 오리지널 독 2kg',                  '오리젠',     101, 52000, 47800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-003', '아카나 프리런 덕 2kg',                    '아카나',     101, 48000, 43500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-004', '뉴트리나 울트라 건강백서 어덜트 2kg',     '뉴트리나',   101, 28000, 24900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-005', '내추럴발란스 L.I.D 오리&감자 2.04kg',     '내추럴발란스', 101, 35000, 31500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-006', '하림 더리얼 그레인프리 오븐베이크드 1.6kg', '하림',     101, 32000, 28900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-007', '나우프레시 스몰브리드 어덜트 2.72kg',      '나우프레시',  101, 42000, 38500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-008', '지위픽 에어드라이드 독 비프 1kg',           '지위픽',     101, 65000, 59900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-009', '웰츠 독 오리지널 2.5kg',                  '웰츠',       101, 30000, 27000);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-010', '네이처스버라이어티 인스팅트 오리지널 2kg',  '네이처스버라이어티', 101, 55000, 49900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-011', '카나간 프리런 치킨 2kg',                   '카나간',     101, 47000, 42900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-F-012', '브이플래닛 비건 도그 사료 2.27kg',          '브이플래닛',  101, 38000, 34500);

-- 강아지 간식 (category_id = 102) — 8개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-001', '오리젠 프리즈드라이 트릿 오리지널 42.5g',  '오리젠',     102, 18000, 16200);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-002', '하림 펫푸드 더리얼 치킨 져키 100g',        '하림',       102, 8500,  7650);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-003', '뽀시래기 오리가슴살 져키 300g',            '뽀시래기',   102, 15000, 12900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-004', '지위픽 굿독 리워드 비프 85g',              '지위픽',     102, 12000, 10800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-005', '내추럴코어 유기농 덴탈껌 12p',             '내추럴코어',  102, 9500,  8550);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-006', '웰츠 독 트릿 연어 100g',                   '웰츠',       102, 7000,  6300);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-007', '뉴트리나 건강백서 덴탈간식 200g',          '뉴트리나',   102, 11000, 9900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-S-008', '나우프레시 소프트 츄 연어 170g',            '나우프레시',  102, 13000, 11700);

-- 강아지 용품 (category_id = 103) — 5개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-G-001', '펫츠빌 자동급식기 6L',                     '펫츠빌',     103, 89000, 79900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-G-002', '레드퍼피 반려견 이동가방 M',               '레드퍼피',   103, 45000, 39900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-G-003', '도그씨 스텐 식기 M 2구세트',               '도그씨',     103, 25000, 22500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-G-004', '릭스 리트랙터블 리드줄 5m',                '릭스',       103, 18000, 15900);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-G-005', '펫츠빌 쿨매트 L',                          '펫츠빌',     103, 32000, 28900);

-- 강아지 영양제 (category_id = 104) — 5개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-N-001', '뉴트리벳 멀티비타민 120정',               '뉴트리벳',   104, 28000, 25200);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-N-002', '펫시모 관절영양제 90정',                   '펫시모',     104, 35000, 31500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-N-003', '닥터바이 유산균 30포',                     '닥터바이',   104, 22000, 19800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-N-004', '아이포뮬러 피부모질 오메가3 90캡슐',       '아이포뮬러', 104, 32000, 28800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('DOG-N-005', '뉴트리벳 면역강화 60정',                   '뉴트리벳',   104, 25000, 22500);

-- 고양이 사료 (category_id = 201) — 8개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-001', '로얄캐닌 인도어 4kg',                     '로얄캐닌',   201, 52000, 46800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-002', '오리젠 캣&키튼 1.8kg',                    '오리젠',     201, 48000, 43200);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-003', '아카나 와일드 프레리 캣 1.8kg',            '아카나',     201, 45000, 40500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-004', '나우프레시 어덜트 캣 3.63kg',              '나우프레시',  201, 49000, 44100);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-005', '하림 더리얼 캣 그레인프리 1.5kg',          '하림',       201, 29000, 26100);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-006', '지위픽 에어드라이드 캣 치킨 1kg',          '지위픽',     201, 68000, 61200);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-007', '웰츠 캣 인도어 2.5kg',                    '웰츠',       201, 33000, 29700);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-F-008', '카나간 프리런 치킨 캣 1.5kg',              '카나간',     201, 44000, 39600);

-- 고양이 간식 (category_id = 202) — 5개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-S-001', '이나바 챠오츄르 참치 14g x 20개',         '이나바',     202, 12000, 10800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-S-002', '템테이션 클래식 닭고기 85g',              '템테이션',   202, 5500,  4950);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-S-003', '츄르 참치맛 버라이어티 40개입',           '이나바',     202, 22000, 19800);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-S-004', '지위픽 굿캣 리워드 치킨 85g',             '지위픽',     202, 13000, 11700);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-S-005', '웰츠 캣 트릿 참치 100g',                  '웰츠',       202, 7500,  6750);

-- 고양이 용품 (category_id = 203) — 4개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-G-001', '캣타워 대형 원목 150cm',                  '펫츠빌',     203, 120000, 108000);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-G-002', '고양이 화장실 후드형 XL',                 '유니참',     203, 35000, 31500);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-G-003', '에버클린 고양이 모래 10L',                '에버클린',   203, 28000, 25200);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-G-004', '펫츠빌 자동 급수기 2.5L',                 '펫츠빌',     203, 42000, 37800);

-- 고양이 영양제 (category_id = 204) — 3개
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-N-001', '뉴트리벳 캣 멀티비타민 90정',             '뉴트리벳',   204, 26000, 23400);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-N-002', '닥터바이 캣 유산균 30포',                 '닥터바이',   204, 20000, 18000);
INSERT INTO PRODUCT (CODE, NAME, BRAND, CATEGORY_ID, PRICE, SALE) VALUES ('CAT-N-003', '펫시모 캣 헤어볼 영양제 60정',            '펫시모',     204, 24000, 21600);

COMMIT;

-- ═══════════════════════════════════════════════════════════
--  확인 쿼리
-- ═══════════════════════════════════════════════════════════
-- SELECT COUNT(*) FROM PRODUCT;           -- 50
-- SELECT COUNT(*) FROM PRODUCT_CATEGORY;  -- 18
-- SELECT * FROM PRODUCT_CATEGORY ORDER BY ID;
-- SELECT * FROM PRODUCT ORDER BY ID;
