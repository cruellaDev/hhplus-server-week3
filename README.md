# hhplus-server-week3
---
## Chapter3 서버 구축
### 시나리오 : - 콘서트 예약 서비스
---
## 프로젝트 milestone
- [milestone in github](https://github.com/cruellaDev/hhplus-server-week3/issues)
---
## 요구사항분석

1. 유저 토큰 발급 / 조회 API
- 유저를 식별할 토큰 발급
- 폴링으로 본인 대기열 정보 확인 (대기열 순번 혹은 잔여 시간)

2. 예약 가능 날짜 / 좌석 API
- 콘서트의 예약 가능한 날짜 목록 조회
- 날짜정보를 입력받아 콘서트의 예약 가능한 좌석 정보 조회
- 좌석 정보는 1~50까지의 좌석번호로 관리됨

3. 좌석 예약 요청 API
- 날짜와 좌석정보 입력받아 좌석 예약 처리
- 좌석 예약과 동시에 유저에게 일정 시간(5분) 동안 임시 배정
- 배정 시간 내 결제 완료되지 않을 경우 임시 배정 해제
- 배정 시간 내 다른 사용자는 해당 좌석을 예약할 수 없다. (이선좌)
- 유의사항
  * 사용자가 유효한 고객인지 검증 필요
  * 포인트 잔액이 충분한지 검증 필요
  * 유저 간 대기열을 요청 순서대로 정확하게 제공하야 함
  * 대기열 테이블을 이용하여 대기열 시스템 구현
  * 동시에 여러 사용자가 예약 요청했을 때 좌석이 중복으로 예약되지 않도록 해야함 (동시성 이슈 고려)

4. 포인트 잔액 충전 / 조회 API
- 토큰과 충전 금액 받아 해당 사용자의 포인트 잔액 충전
- 토큰을 통해 해당 사용자 포인트 잔액 조회

5. 결제 API
- 결제 처리하고 결제 내역 생성
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정, 대기열 토큰 만료시킴

---
## 프로젝트 설계
### 시퀀스다이어그램
1. 유저토큰 발급 조회 API
![createUserToken](./images/createUserToken.png)
![checkUserToken](./images/checkToken.png)
2. 예약 가능 날짜 / 좌석 API
![getAvailableConcert](./images/getConcertOptions.png)
![getAvailableConcertSeat](./images/getConcertOptionsSeat.png)
3. 좌석 예약 요청 API
![reservation](./images/reservation.png)
4. 포인트 잔액 충전 / 조회 API
![chargePoint](./images/chargePoint.png)
![getPoint](./images/getPoint.png)
5. 결제 API
![payment](./images/payment.png)
### ERD
![ERD](./images/ConcertERD.png)
### API 명세
---
## 작업과정
3주차
- ~ 2024.06.30
  - 요구사항 분석
  - ERD 설계
- ~ 2024.07.02
  - 프로젝트 명세 문서 작성
  - 프로젝트 세팅
  - 시퀀스 다이어그램 작성

