# triple-app

> 트리플여행자 클럽 마일리지 서비스

## Assignment Summary

### 목표

사용자가 장소에 리뷰를 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 개인별 누적 포인트를 관리하는 애플리케이션 개발

### Specification

- 리뷰 작성 API

  ```json
  POST /events

  {
    "type": "REVIEW",
    "action": "ADD", /* "MOD", "DELETE" */
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667791",
    "content": "좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
    "userId": "c1b69c15-cf58-4697-8767-207054f58797",
    "placeId": "3ede0ef2-92b7-4817-a5f3-0c575361f745"
  }
  ```

- 리뷰 작성 보상 점수
  - 내용 점수
    ```
    1자 이상 텍스트 작성: 1점
    1장 이상 사진 첨부: 1점
    ```
  - 보너스 점수
    ```
    특정 장소에 첫 리뷰 작성: 1점
    ```

### Requirements

- SQL(MySQL >= 5.7) 설계
- 아래 API를 제공하는 서버 애플리케이션 작성
  - <kbd>POST /events</kbd>로 호출하는 포인트 적립 API
  - 포인트 조회 API

### Remark

- 포인트 증감 시 이력이 남아야 함.
- 사용자마다 현재 시점의 포인트 총점을 조회하거나 계산할 수 있어야 함.
- 포인트 부여 API 구현에 필요한 SQL 수행 시, 전체 테이블 스캔이 일어나지 않는 인덱스가 필요함.
- 리뷰를 작성했다가 삭제하면 해당 리뷰로 부여한 내용 점수와 보너스 점수를 회수함.

<br>

## About Project

### 기술 스택

- Java 11
- SpringBoot 2.7.1
- Spring Data JPA 2.7.1
- QueryDSL 1.0.10
- MySQL 8.0.21

### ERD

<img src="https://user-images.githubusercontent.com/52561963/177161493-53ae0608-8be4-4798-a7f8-36fad1f0edb0.png" width="450">

### API Spec

- POST /events

  ```json
  // Request
  {
    "type": "REVIEW",
    "action": "ADD", /* "MOD", "DELETE" */
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667791",
    "content": "좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
    "userId": "c1b69c15-cf58-4697-8767-207054f58797",
    "placeId": "3ede0ef2-92b7-4817-a5f3-0c575361f745"
  }

  // Response
  /* 200 OK */
  {
    "userId": "c1b69c15-cf58-4697-8767-207054f58797",
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667791"
  }
  ```

  - 리뷰를 작성, 수정 및 삭제할 수 있음.

- GET /user/point/{userId}

  ```json
  // Response
  /* 200 OK */
  {
    "id": "c1b69c15-cf58-4697-8767-207054f58797",
    "contentPoint": 3,
    "bonusPoint": 4,
    "totalPoint": 7
  }
  ```

  - 유저의 포인트를 조회할 수 있음.

- GET /history/point/{userId}/{pageId}

  ```json
  // Response
  /* 200 OK */
  {
    "page": 1,
    "totalPages": 1,
    "data": [
      {
        "userId": "c1b69c15-cf58-4697-8767-207054f58797",
        "contentPoint": 2,
        "bonusPoint": 3,
        "timestamp": "2022-07-04T12:44:53.432+00:00"
      },
      {
        "userId": "c1b69c15-cf58-4697-8767-207054f58797",
        "contentPoint": 0,
        "bonusPoint": 0,
        "timestamp": "2022-07-04T12:44:53.339+00:00"
      }
    ]
  }
  ```

- GET /history/point/all/{pageId}

  ```json
  // Response
  /* 200 OK */
  {
    "page": 1,
    "totalPages": 1,
    "data": [
      {
        "userId": "c1b69c15-cf58-4697-8767-207054f58797",
        "contentPoint": 2,
        "bonusPoint": 3,
        "timestamp": "2022-07-04T12:44:53.432+00:00"
      },
      {
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "contentPoint": 1,
        "bonusPoint": 5,
        "timestamp": "2022-07-04T12:44:53.421+00:00"
      }
    ]
  }
  ```

- GET /ping

  ```
  PLACE
  3ede0ef2-92b7-4817-a5f3-0c575361f745

  USER
  b888e83e-f154-4d5a-8d6c-cfd76db569d0
  c1b69c15-cf58-4697-8767-207054f58797
  68b07ea3-c5f1-475e-b85d-8cd9491e1dea
  ```

  - 유저나 장소 등록 API를 따로 제공하지 않기 때문에 **임시로** 생성한 API이며, 위 UUID 값에 해당하는 장소와 유저를 등록할 수 있음.
  - 유저와 장소가 등록되어 있어야 다른 API 요청이 정상적으로 처리되는지를 확인할 수 있음.

### Running the app

- DB Schema 생성

  ```sql
  CREATE SCHEMA `tt` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
  ```

- application.properties 파일 변경

  ```yml
  # src/main/resources/application.yml

  spring:
    jpa:
      show-sql: true
      hibernate:
        ddl-auto: create
    datasource:
      url: jdbc:mysql://localhost:3306/{ 생성한 Schema명 }?serverTimezone=Asia/Seoul
      username: { 사용자 ID }
      password: { 비밀번호 }
      driver-class-name: com.mysql.cj.jdbc.Driver
  ```

- 빌드 & 실행

```ps
git clone https://github.com/yanglet/Triple.git
cd Triple

./gradlew bootjar
./gradlew bootRun
```

### Feature

- UUID 값을 Binary(16)으로 최적화하여 저장
- Hibernate Envers를 사용한 유저 포인트 이력 관리
- Infra Layer와 Application Layer의 테스트 코드 작성
- 과제 범위인 review와 review_image에 soft delete 적용
