# MSA Toy Project

## 프로젝트 개요

* Spring cloud를 이용하여 MSA 애플리케이션을 개발하며, Spring Cloud의 사용법과 구성을 학습

## 주요 기능 

* [JWT를 이용한 로그인, 회원가입 구현](https://minutemaid.tistory.com/187)
* [JUnit5, Mockito Framework를 이용하여 테스트](https://minutemaid.tistory.com/189)
* [Open Feign을 이용한 인터페이스 형식의 HTTP 통신 구현 ]()
* [Spring cloud Config를 통한 환경별 구성정보 분리](https://minutemaid.tistory.com/195)
* [SAGA Pattern와 Kafka를 이용한 이벤트 기반 아키텍처](https://minutemaid.tistory.com/198)
  
## Architecture

|Service|Description|
|-----|-------------|
|```config-service```|- Configuration service</br> - 환경별로 구성정보 분리|
|```discoveryservice```|- Service discovery|
|```apigateway-service```|- Spring Cloud Gateway</br> - 판매자, 일반 유저를 구분하여 권한 부여|
|```member-service```|- JWT를 이용한 로그인, 회원가입|
|```inventory-service```|- 상품 등록 및 재고 확인|
|```order-service```|- 상품 주문|
|```payment-service```|- 결제 |

#### config-service

<img width="732" alt="스크린샷 2023-09-15 오후 6 16 36" src="https://github.com/yaezzin/spring-msa-market/assets/97823928/208a9fd6-052e-42b0-b3b6-77a95104787d">
