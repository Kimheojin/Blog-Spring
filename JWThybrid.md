## 금방 할듯

### 남겨야 할 부분
- cookie 관련 설정 부분


### 제거해야 하는 부분
- filter 기반 로그인 방식 -> 엔드포인트로 빼기
- 

### 수정해야 하는 부분 (좀많음)
- AuthController 엔드포인트 부분

### 새로 만들어야 하는 부분
- JWT Refresh Token 생성 + Access Token 생성 관련 로직


---
- Refresh Token 관련 추가 테이블 구성하기
- 필터는 정확히 요청에 포함된 토큰을 검증하고 인증 처리만 하는 걸로 구현하기


- principal 객체에 id, email, role 정도만 채우기
- 이거 NPE 조심하기
---
- 이용자 들이 착하게 로그아웃 버튼을 누르나
  - 난 누르긴 하는데

--- 
# 구현 순서 정하고 밀죠
- refreshToken 테이블 설정
  - token String 형태로
- Token Provider 구현 초안은 하기
- filter 구현
- controller, service 관련 엔드포인트 수정