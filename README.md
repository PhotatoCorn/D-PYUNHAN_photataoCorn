# D-PYUNHAN_photataoCorn

D-PYUNHAN (아파트 종합 관리 시스템)

Spring Boot + MyBatis + Oracle 기반의 아파트 통합 관리 플랫폼
단순 CRUD가 아닌 “실제 아파트 관리사무소에서 운영 가능한 수준”을 목표로
기획 → DB 설계 → 개발 → UI 커스터마이징까지 전 과정에 참여하며 완성한 프로젝트입니다.

이 프로젝트는
내가 직접 배워가면서 코드를 작성했고,
문제를 해결하면서 성장했던 기록이 담긴
성장형 포트폴리오 프로젝트입니다.

개발하며 익힌 핵심 역량 & 공부 과정
 MVC 구조 → "Controller는 요청 분배, Service는 로직, Mapper는 DB"

초반에는 역할 구분이 헷갈렸지만,
프로젝트를 하면서 레이어드 아키텍처 원리를 제대로 이해했습니다.
지금은 Controller에 비즈니스 로직을 넣지 않고
Service에 로직을 두는 구조를 자연스럽게 사용합니다.

 MyBatis 심화 기능 활용 능력

단순 SELECT가 아니라 실제 서비스에 필요한 기능들을 실전으로 익혔습니다.

동적SQL(IF / WHERE / CHOOSE)

페이징 처리용 공통 SQL 템플릿 사용

복잡한 JOIN + resultMap 기반 1:N 매핑

VO 구조를 활용한 연관 데이터 세팅

관리비 · 투표 · 캘린더 등 모듈별 SQL 최적화

특히 투표 모듈은
투표글 / 항목 / 대상자 / 결과
총 4개 테이블 이상이 얽혀 있어
MyBatis 구조를 많이 다루며 실력이 크게 늘었습니다.
✔ Oracle DB — 직접 ERD 설계 및 관계 설정

세대/입주민, 투표, 전자결재 등
40개 가까운 테이블을 직접 설계하고 관계를 구성했습니다.

정규화

PK/FK 설정

문자 타입 vs DATE 타입 차이

무결성 문제 해결

ON DELETE 처리

DB 초기 데이터 세팅

이 과정을 통해 DB 설계 감각이 크게 향상되었습니다.

 UI/UX 커스터마이징 능력 향상

AdminLTE3 + Bootstrap 템플릿을 그대로 쓰지 않고
필요한 화면을 직접 커스터마이징했습니다.

모바일 프레임 UI

투표 상세 모달

Plotly를 사용한 투표 결과 차트

FullCalendar 일정 관리

카드/테이블 레이아웃 재배치

커스텀 스크롤바 및 hover 스타일

jQuery + Fetch를 이용한 실시간 동적 업데이트

디자인 감각을 키우기 위해 작은 부분(버튼 간격, 테이블 정렬, 색상톤 등)도
태그 내부style, 전역css , bootstrap을 사용하여 다듬었습니다.

session과 principal 값 차이 때문에 발생한 인증 문제

하나하나 해결하면서
문제의 원인을 추적하는 능력이 크게 향상되었습니다.

🚀 주요 기능 소개

세대/입주민 관리

세대별 입주민 조회

상세 정보 확인

입주민 특이사항 관리

1:N 매핑

모바일에서도 보기 좋은 반응형 테이블 구성

🗳 온라인 투표 시스템

투표 생성 / 수정 / 삭제

투표 항목 및 대상 세대 설정

투표 참여 여부 체크

중복 투표 방지

모달 기반의 상세 보기

Plotly 기반의 투표 결과 시각화

AJAX 비동기 투표 처리

시설 예약 

일정 관리 FullCalendar 기반 일정 표시

모달로 상세 정보 확인

예약 시간 중복 방지

직원/입주민별 예약 관리

실시간 알림 시스템

WebSocket(SockJS + STOMP) 기반 푸시 알림

특정 부서/직원 대상 알림 전송

알림 읽음 여부 처리

📊 이상 사용량 분석 기능

이동평균 + 표준편차 기반 이상치 감지

단일 세대 단위 분석 기능 구현

기술 스택
Backend

Java 11

Spring Boot

Spring Security

MyBatis

Oracle Database

Frontend

JSP / Thymeleaf 혼합 환경

jQuery / Vanilla JS

Bootstrap 4/5

AdminLTE3 커스터마이징

FullCalendar

Plotly, Chart.js

기타

Git / GitHub

STS (Spring Tool Suite)

Maven

🧩 프로젝트 디렉토리 구조
src/
 ├── main/
 │   ├── java/kr/or/ddit/
 │   │   ├── controller/
 │   │   ├── service/
 │   │   ├── serviceImpl/
 │   │   ├── mapper/
 │   │   ├── vo/
 │   │   └── util/
 │   ├── resources/
 │   │   ├── mapper XML(MyBatis)
 │   │   ├── static/js
 │   │   ├── static/css
 │   │   └── templates / WEB-INF/views
 └── test/

🔍 개발할 때 중요하게 생각한 점
📌 1. 구조적인 개발

각 기능을 기능별로 나누고
Controller → Service → Mapper
구조를 정확히 유지하도록 했습니다.

📌 2. 이해하고 작성한 코드

단순히 따라 쓰는 것이 아니라
왜 이 기술을 사용하는지,
이 코드가 어떤 역할을 하는지 이해하고 작성했습니다.

📌 3. 실제 업무 시스템처럼 만들기

관리비, 투표, 예약, 알림 등
아파트 관리사무소의 실제 업무 흐름을 상상하며 만들었습니다.

마무리

이 프로젝트는 단순한 과제나 연습이 아니라,
개발자로 성장하기 위해 내가 직접 고민하고 작성한 코드들의 기록입니다.

면접에서
“어떻게 공부했고, 어떻게 문제를 해결했는가?”
이 프로젝트를 기반으로 자신 있게 설명할 수 있습니다.
