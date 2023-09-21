
select * from MEMBER;

-- 회원 테이블 member 생성
CREATE TABLE MEMBER(
    id varchar2(12) not null primary key,
    pass varchar2(12) not null,
    name varchar2(20) not null,
    reg_date DATE not null
);

alter table member 
add age number;

alter table member
add gender varchar2(5);

alter table member
add address varchar2(1000);

alter table member
add email varchar2(100); -- 추가함

alter table member
add tel varchar2(100); -- 추가함

alter table member
add hp varchar2(100); -- 추가함

-- 만들어진 member 테이블 구조 보기
desc member;

commit; -- 영구 반영
-- ---------------------------------------------------

-- 답변글을 달수 있고 페이징 처리가 가능한 게시판 board테이블 생성
CREATE TABLE board(
    b_idx number  PRIMARY KEY, -- 게시판의 글의 순서값(글번호)
    b_id  varchar2(20) not null, -- 글을 작성한 사람의 아이디
    b_pw  varchar2(10), -- 작성하는 글의 비밀번호 
    b_name varchar2(20), -- 글을 작성한 사람의 이름
    b_email varchar2(50), -- 글을 작성한 사람의 이메일
    b_title varchar2(100), -- 작성하는 글의 제목
    b_content varchar2(4000), -- 작성하는 글의 내용
    b_group number, -- 주글 과 답변글 그룹으로 묶어줄수 있는 그룹번호
    b_level number, -- 작성한 답변글의 들여쓰기 정도 레벨 값
    b_date Date, -- 글을 작성한 날짜
    b_cnt number, -- 글 조회수 
       -- id 컬럼을 회원테이블 member의  id컬럼에 대해 외래키로 지정합니다.
    CONSTRAINT FK_BOARD_b_ID FOREIGN KEY(b_id)
    REFERENCES member(id) ON DELETE CASCADE
);

--
--FOREIGN KEY
--외래키 역시 PK와 마찬가지로 매우 중요한 제약조건이다.
--
--외부키, 외래키, 참조키, 외부 식별자 등으로 불리며 흔히 FK라고도 한다.
--FK가 정의된 테이블을 자식 테이블이라고 칭한다.
--참조되는 테이블 즉, PK가 있는 테이블을 부모 테이블이라 한다.
--부모 테이블의 PK 컬럼에 존재하는 데이터만 자식 테이블에 입력할 수 있다.
--부모 테이블은 자식의 데이터나 테이블이 삭제된다고 영향을 받지 않는다.
--참조하는 데이터 컬럼과 데이터 타입이 반드시 일치해야 한다.
--참조할 수 있는 컬럼은 기본키(PK)나 UNIQUE만 가능하다.(보통 PK랑 엮는다.)
--제약조건을 추가할 때 사용되는 구문은 다음과 같다.
--
-- 
--
--▷ 구문
--
--CONSTRAINT [제약조건 명] FOREIGN KEY([컬럼명])
--    REFERENCES [참조할 테이블 이름]([참조할 컬럼])
--   [ON DELETE CASCADE | ON DELETE SET NULL]
--
--
--실제 테이블을 선언하면서 사용해보면 다음과 같이 사용할 수 있다.
--
--CREATE TABLE parentTable(
--    parentPk NUMBER PRIMARY KEY
--);
--
--
--CREATE TABLE consTest(
--    parentPk NUMBER,
--    pkCol1 CHAR(8),
--    
--    CONSTRAINT fk_code FOREIGN KEY(parentPk)
--    REFERENCES parentTable(parentPk) ON DELETE CASCADE
--);
--참조할 컬럼과 같은 컬럼이 자식 테이블에 존재해야만 한다.
--
--(굳이 같은 이름을 가진 컬럼일 필요는 없지만 일반적으로는 같은 이름을 가진 컬럼을 선언한다.)
--
--그리고 참조되는 컬럼에 데이터가 먼저 있어야 하기 때문에 자식 테이블에 값을 먼저 넣을 순 없다.
--
-- 
--
--그리고 특이하게도 FK를 선언할 때 사용되는 두 가지 옵션이 존재한다.
--
--ON DELETE CASCADE
--참조되는 부모 테이블 행에 대한 DELETE를 허용한다.
--
--즉, 참조되는 부모 테이블 값이 삭제되면 연쇄적으로 자식 테이블 값 역시 삭제된다는 의미이다.
--
--ON DELETE SET NULL
--참조되는 부모 테이블 행에 대한 DELETE를 허용한다.
--
--이건 CASCADE와는 다른데, 부모 테이블의 값이 삭제되면 해당 참조하는 자식 테이블의 값들이 NULL로 설정되는 옵션이다.
--
-- 
--
--일반적으로 ON DELETE CASCADE 옵션을 많이 사용한다.
--
--해당 옵션을 사용하지 않으면 엮여있는 모든 자식 테이블의 값을 먼저 다 지워줘야 하기 때문이다.

-- https://mine-it-record.tistory.com/45

-- 시퀀스 생성
create sequence border_b_idx
       increment BY 1
       start with 1
       minvalue 1
       maxvalue 9999
       nocycle
       nocache
       noorder;
-- ---------------------

-- 렌트카 회사에 모든 차량 정보가 저장되는 carlist테이블 생성
create table carlist(   
    carno NUMBER NOT NULL PRIMARY KEY, -- 차 번호 
    carname VARCHAR2(50) NOT NULL, -- 차 이름
    carcompany VARCHAR2(50) NOT NULL, -- 차 제조사 명 
    carprice NUMBER NOT NULL, -- 차 한대당 렌트 가격
    carusepeople NUMBER NOT NULL, -- 인승 정보 
    carinfo VARCHAR2(500) NOT NULL, -- 차량 상세 정보 
    carimg VARCHAR2(50) NOT NULL, -- 차량 이미지 명
    carcategory VARCHAR2(10) NOT NULL -- 차 유형( 소형 or 중형 or  대형)
); 

-- carlist테이블에 차량 정보들 추가
INSERT INTO carlist VALUES(1,'아반테','현대',80000,4,'이차량은 소형차량으로 잘나갑니다.','avante.jpg','Small');
INSERT INTO carlist VALUES(2, '카렌스', '현대', 90000, 4, '이차량은 소형차량으로 잘나갑니다.', 'carens.jpg', 'Small' );
INSERT INTO carlist VALUES(3, '카니발', '기아', 100000, 9, '이차량은 소형차량으로 잘나갑니다.', 'canival.jpg', 'Small' );
INSERT INTO carlist VALUES(4, '코란도', '쌍용', 110000, 4, '이차량은 소형차량으로 잘나갑니다.', 'co.jpg', 'Small' );
INSERT INTO carlist VALUES(101, '에쿠스', '현대', 120000, 5, '이차량은 중형차량으로 잘나갑니다.', 'equus.jpg', 'Mid' );
INSERT INTO carlist VALUES(102, '그렌져', '현대', 130000, 5, '이차량은 중형차량으로 잘나갑니다.', 'grandeur.jpg', 'Mid' );
INSERT INTO carlist VALUES(103, 'k3', '기아', 140000, 4, '이차량은 중형차량으로 잘나갑니다.', 'k3.jpg', 'Mid' );
INSERT INTO carlist VALUES(104, 'k5', '기아', 150000, 4, '이차량은 중형차량으로 잘나갑니다.', 'k5.jpg', 'Mid' );
INSERT INTO carlist VALUES(201, 'k7', '기아', 160000, 4, '이차량은 대형차량으로 잘나갑니다.', 'k7.jpg', 'Big' );
INSERT INTO carlist VALUES(202, 'k9', '기아', 170000, 4, '이차량은 대형차량으로 잘나갑니다.', 'k9.jpg', 'Big' );
INSERT INTO carlist VALUES(203, '말리부', 'GM', 180000, 4, '이차량은 대형차량으로 잘나갑니다.', 'malibu.jpg', 'Big' );
INSERT INTO carlist VALUES(204, 'bmw528i', 'bmw', 190000, 5, '이차량은 대형차량으로 잘나갑니다.', 'bmw.jpg', 'Big' );

COMMIT;





-- 비 회원 으로 렌트(대여)주문 현황을 DB에 저장 하기 위한 
-- non_carorder 테이블 만들기 

-- non_orderid; //자동차 렌트(대여) 주문id 저장
-- carno; //렌트할 차량 차번호 저장
-- carqty;//렌트 차량 대여 수량 저장
--  carreserveday;//대여기간 저장
--  carbegindate;//자동차를 렌트(대여)할 시작날짜 저장.
--  carins;//렌트시 보험적용 일수 저장
--  carwifi;//렌트시 무선wifi적용 일수 저장
--  carnave;//렌트시 네비게이션 적용여부 저장  (무료로 적용하면1, 미적용이면0)
--  carbabyseat;//렌트시 베이비시트적용 일수 저장 
--  memberphone;// 비회원으로 렌트할 사람 폰번호 저장
--  memberpass;// 비회원으로 렌트시 주문 패스워드 저장

Create Table non_carorder(
 non_orderid number PRIMARY KEY,
 carno number not null,
 carqty number not null,
 carreserveday number not null,
 carbegindate varchar2(50) not null,
 carins number not null,
 carwifi number not null,
 carnave number not null,
 carbabyseat number not null,
 memberphone varchar2(50) not null,
 memberpass varchar2(50) not null
);

create sequence non_carorder_non_orderid
       increment BY 1
       start with 1
       minvalue 1
       maxvalue 9999
       nocycle
       nocache
       noorder;
 
 
-- 회원 으로 렌트(대여)주문 현황을 DB에 저장 하기 위한 
-- carorder 테이블 만들기 

-- orderid; //자동차 렌트(대여) 주문id 저장
-- id; //렌트한 사람 ID 저장 <- member테이블에 저장된  id가 들어가야함
-- carno; //렌트할 차량 차번호 저장
-- carqty;//렌트 차량 대여 수량 저장
--  carreserveday;//대여기간 저장
--  carbegindate;//자동차를 렌트(대여)할 시작날짜 저장.
--  carins;//렌트시 보험적용 일수 저장
--  carwifi;//렌트시 무선wifi적용 일수 저장
--  carnave;//렌트시 네비게이션 적용여부 저장  (무료로 적용하면1, 미적용이면0)
--  carbabyseat;//렌트시 베이비시트적용 일수 저장 
--  memberphone;// 비회원으로 렌트할 사람 폰번호 저장
--  memberpass;// 비회원으로 렌트시 주문 패스워드 저장       
Create Table carorder(
 orderid number PRIMARY KEY,
 id VARCHAR2(12) not null,
 carno number not null,
 carqty number not null,
 carreserveday number not null,
 carbegindate varchar2(50) not null,
 carins number not null,
 carwifi number not null,
 carnave number not null,
 carbabyseat number not null,
 memberphone varchar2(50) not null,
 memberpass varchar2(50) not null,
 -- id 컬럼을 회원테이블 member의  id컬럼에 대해 외래키로 지정합니다.
CONSTRAINT FK_carorder_id FOREIGN KEY(id)
REFERENCES member(id) ON DELETE CASCADE
);       
       
       
delete from board;       
       
       commit;
       
-- 답변글을 달수 있고 페이징 처리가 가능하고 파일첨부 및 다운로드 가능한
-- 게시판 FileBoard테이블 생성
CREATE TABLE FileBoard(
    b_idx number  PRIMARY KEY, -- 게시판의 글의 순서값(글번호)
    b_id  varchar2(20) not null, -- 글을 작성한 사람의 아이디
    b_pw  varchar2(10), -- 작성하는 글의 비밀번호 
    b_name varchar2(20), -- 글을 작성한 사람의 이름
    b_email varchar2(50), -- 글을 작성한 사람의 이메일
    b_title varchar2(100), -- 작성하는 글의 제목
    b_content varchar2(4000), -- 작성하는 글의 내용
    b_group number, -- 주글 과 답변글 그룹으로 묶어줄수 있는 그룹번호
    b_level number, -- 작성한 답변글의 들여쓰기 정도 레벨 값
    b_date Date, -- 글을 작성한 날짜
    b_cnt number, -- 글 조회수
    ofile   varchar2(200),  -- 첨부(업로드)한 원본파일 명 
    sfile   varchar2(30),   -- 첨부(업로드)한 실제 파일명 (저장된 파일명)
    downcount number(5) default 0 not null, -- 다운로드한 횟수 
    
       -- id 컬럼을 회원테이블 member의  id컬럼에 대해 외래키로 지정합니다.
    CONSTRAINT FK_FileBoard_b_ID FOREIGN KEY(b_id)
    REFERENCES member(id) ON DELETE CASCADE
);       
       
-- sfile   varchar2(30) 다운로하는 파일명이 길 경우 에러가 나기때문에
--  sfile varchar2(1000) 으로 수정 
ALTER TABLE FileBoard MODIFY(sfile varchar2(1000));       
       
commit;      
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       