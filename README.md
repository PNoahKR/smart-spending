# 💸 슬기로운 소비생활 어플리케이션
2025.03.06 ~ 현재 진행 중

## 💻 프로젝트 개요
프로젝트 명 : 개인 금융 관리 어플리케이션 프로젝트
목표 : 사용자가 자신의 수입과 지출을 관리하고, 예산을 설정해 금융 데이터를 분석할 수 있는 플랫폼

## 📱 핵심 기능
### 1차 도입 기능
**사용자 인증 및 권한**

- 회원가입, 로그인(OAuth2 / JWT 기반)
- 비밀번호 암호화 및 보안

**수입/지출(내역) 관리**

- 수입/지출 항목 추가, 수정, 삭제
- 카테고리별 관리

**예산 설정**

- 월별 예산 설정 및 초과 알림 기능

**통계 및 대시보드**

- 월별 지출/수입 차트
- 카테고리 비율 분석

**내역 검색 및 필터링**

- 날짜별, 카테고리별, 금액별 검색

### 도입 예정 기능(확장 가능성)

## 🧾 상세 요구 사항
사용자 인증 및 권한

- **회원가입**
    - 이메일, 비밀번호, 이름 필수 기입
    - 이메일 중복 체크
    - 비밀번호는 8자이상, 영문, 숫자, 특수문자 포함
    - 비밀번호 암호화
    - 이메일 인증 기능
- **로그인**
    - 이메일과 비밀번호 기반 로그인
    - 성공시 JWT 토큰 발급
    - 토큰 만료 기한: 3시간
- **JWT 인증**
    - 각 API 호출시 JWT 사용 인증
    - Refresh Token 구현
- **비밀번호 재설정**
    - 비밀번호 찾기 요청 시 이메일로 인증코드 발송
    - 새 비밀번호 설정 API

수입/지출(내역) 관리

- **수입/지출 항목 추가**:
    - 필수 입력: 금액, 날짜, 카테고리, 메모(선택).
    - 금액은 음수가 아닌 값으로 제한.
    - 날짜는 현재 날짜 기준으로 과거 미래 가능.
- **수입/지출 항목 수정**:
    - 수정 가능한 필드: 금액, 날짜, 카테고리, 메모.
- **수입/지출 항목 삭제**:
    - 사용자는 본인의 항목만 삭제 가능.
    - 삭제 후 데이터는 복구 불가로 처리.
- **카테고리별 관리**:
    - 기본 제공 카테고리: 식비, 교통비, 엔터테인먼트, 주거비, 의료/건강, 교육/자기개발, 쇼핑, 가족/인간관계, 저축, 급여, 기타
    - 사용자가 카테고리 추가 가능.
    - 카테고리 수정/삭제는 시스템 기본 카테고리는 불가.

예산 설정

- **월별 예산 설정**:
    - 사용자는 월 단위로 예산을 설정 가능.
    - 필수 입력: 예산 금액.
    - 월별 설정된 예산 금액은 수정 가능.
- **초과 알림**:
    - 사용자의 월별 지출 합계가 예산 금액을 초과하면 알림.
    - 알림 방법: 대시보드에 표시(추가적으로 이메일 알림 고려 가능).

통계 및 대시보드

- **월별 지출/수입 차트**:
    - 월 단위로 수입 및 지출을 막대 또는 파이 차트로 시각화.
    - 데이터 소스: 월별 합계 계산 API.
- **카테고리별 비율 분석**:
    - 지출의 카테고리별 비율을 원형 차트로 표시.
    - 특정 월을 기준으로 필터 가능.
- **대시보드 요약**:
    - 현재 월의 총수입, 총지출, 예산 대비 잔여 금액 표시.
    - 최근 5개의 수입/지출 항목 표시.

내역 검색 및 필터링

- **검색 조건**:
    - 날짜: 시작일 ~ 종료일 설정.
    - 카테고리: 선택된 카테고리만 필터링.
    - 금액 범위: 최소 금액 ~ 최대 금액 입력.
- **결과 정렬**:
    - 기본 정렬: 날짜 내림차순.
    - 사용자는 금액 기준 오름차순/내림차순 선택 가능.
- **검색 결과**:
    - 조건에 맞는 항목 목록 반환.
    - 페이지네이션 제공 (기본 10개씩 표시).

## 🛠️ 기술 스택
- **백엔드**: Spring Boot, JPA, Hibernate
- **데이터베이스**: MySQL 또는 PostgreSQL
- **프론트엔드**: React.js 또는 Thymeleaf
- **인증/보안**: Spring Security, OAuth2, JWT
- **캐싱**: Redis
- **배포**: Docker, Kubernetes, AWS (EC2, RDS)