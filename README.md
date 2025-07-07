# 블로그 백엔드 API
Spring Boot 백엔드 REST API 서버 코드
## 링크
- **배포**: [배포 링크](https://heojinblog.vercel.app/)
- **API 문서**: [Blog API 문서](https://heojin1109.github.io/2025.07.04.htm)
## 배포 환경
- **배포 방식**: 온프레미스 서버 + GitHub Actions CD + Docker Compose
- **서버 환경**: UbuntuServer 22.04 + Docker Container
- **인프라**: 자체 구축 및 관리
## 프로젝트 도식도

![블로그 도식도](https://res.cloudinary.com/dtrxriyea/image/upload/v1751883877/markdonw/sqi0fzzq6e5ln3eako52.png)

## 주요 기능
- Exception Handler를 통한 통합 공통 에러 처리
- Spring Rest Docs 기반 API 문서 자동 생성
- 쿠키 + 세션 기반 인증/인가

## 스택

### Backend
- **Java 17**, **Spring Boot 3.4.4**
- **Spring Data JPA** , **Spring Security** , **QueryDSL**

### Database
- **MySQL**

### API 문서화
- **Spring REST Docs** , **AsciiDoctor**

### 기타
- **Spring AOP**, **Cloudinary SDK**


