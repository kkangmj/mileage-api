# triple-mileage-api

> 트리플여행자 클럽 마일리지 서비스의 API 구현하기

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
  - 리뷰를 작성, 수정 및 삭제할 수 있음.
  - Request
    ```json
    {
      "type": "REVIEW",
      "action": "ADD", 
      "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667791",
      "content": "좋아요!",
      "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
      "userId": "c1b69c15-cf58-4697-8767-207054f58797",
      "placeId": "3ede0ef2-92b7-4817-a5f3-0c575361f745"
    }
    ```
  - Response
    ```json
    {
      "userId": "c1b69c15-cf58-4697-8767-207054f58797",
      "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667791"
    }
    ```
    
- GET /user/point/{userId}
  - 유저의 포인트를 조회할 수 있음.
  - Response
    ```json
    {
    "id": "c1b69c15-cf58-4697-8767-207054f58797",
    "contentPoint": 3,
    "bonusPoint": 4,
    "totalPoint": 7
    }
    ```

- GET /history/point/{userId}/{pageId}
  - 유저의 포인트 변경 이력을 조회할 수 있음.
  - Response
    ```json
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
  - 모든 유저의 포인트 변경 이력을 조회할 수 있음.
  - Response
    ```json
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
  - 유저나 장소 등록 API를 따로 제공하지 않기 때문에 **임시로** 생성한 API이며, 아래 UUID 값에 해당하는 장소와 유저를 등록할 수 있음.
  - 유저와 장소가 등록되어 있어야 다른 API 요청이 정상적으로 처리되는지를 확인할 수 있음.

    ```
    PLACE
    3ede0ef2-92b7-4817-a5f3-0c575361f745

    USER
    b888e83e-f154-4d5a-8d6c-cfd76db569d0
    c1b69c15-cf58-4697-8767-207054f58797
    68b07ea3-c5f1-475e-b85d-8cd9491e1dea
    ```

### Running the app

- Git Clone

  ```ps
  git clone https://github.com/kkangmj/triple-assignment
  ```

- DB Schema 생성

  ```sql
  CREATE SCHEMA `{ Schema명 }` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
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
  ```yml
  # src/test/resources/application.yml

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
  cd triple-assignment

  ./gradlew bootjar
  ./gradlew bootRun
  ```

### Feature

- UUID 값을 Binary(16)으로 최적화하여 저장
- Hibernate Envers를 사용한 유저 포인트 이력 관리
- Infra Layer와 Application Layer의 테스트 코드 작성
- 과제 범위인 review와 review_image에 soft delete 적용

### To-Do
- [ ] 인덱스 설정하여 풀 스캔이 일어나지 않도록 구현하기
- [ ] Controller Test Code 작성하기
- [ ] envers의 rev 값이 int 형이 아닌 bigint 형으로
- [ ] Hibernate OneToOne 이슈사항 정확한 원인 찾기 (User와 UserPoint의 연관관계가 OneToOne이고, user 엔티티를 통해 userPoint를 가져올 때 발생)
- [ ] envers의 property가 id인 경우 uuid를 가져올 수 없었던 원인 찾기 
- [ ] User 등록, Place 등록 api 구현하기
