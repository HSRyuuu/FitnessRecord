# FitnessRecord
(운동(웨이트 트레이닝) 기록 서비스)

## Skills
- Java 11, Spring Boot 2.7.10, gradle 8.2.1
- Spring Data JPA, Spring Security
- MariaDB
- JUnit5
- IntelliJ Idea

### 라이브러리
- google GSON
- Gmail smtp 메일 발송
- Swagger `/swagger-ui.html`

# 요구사항 

## 회원 관리
### 공통
- 로그인 시 JWT Token이 발부된다.
- 이후 `Authorization` 헤더에 `Bearer {token}`을 추가하여 권한을 확인한다.

### 이메일 회원
- 이메일로 회원가입 할 수 있다.`UserType.BASIC`
- 회원가입 시 인증 메일이 발송되고, 메일 인증을 완료해야지 서비스를 이용할 수 있다.
- 이메일, 비밀 번호를 통해 로그인 할 수 있다.

### 소셜 회원
- `KAKAO`, `(추가 예정)`
- `/login-page`에서 소셜 로그인을 통해 회원가입, 로그인 할 수 있다.
  - REST API를 제공하는 애플리케이션이지만 인증 코드를 불러오는부분 까지만 thymeleaf form을 사용한다. 
- 처음 로그인 시에는 회원가입이 되고, 이후에는 로그인이 된다.

### 회원 BODY_INFO
- 회원은 키, 몸무게, 골격근량, 체지방량, 체지방률 등을 입력할 수 있다.
- 데이터 추가 시 마다 몸 상태 변화가 기록된다.
- 회원 Body 데이터 추가는 하루에 한번만 가능하다.

## 회원 운동 기록

### 회원 별 운동 기록 제공

### 회원 별 운동 통계 제공

### 기록 알림 기능

## 루틴 만들기


## 운동 종목



### 운동 커뮤니티 (가능하다면)

## ERD
ERD는 아직 완성본이 아닙니다.
![ERD.png](src%2Fmain%2Fresources%2Fstatic%2Fimage%2FERD.png)




