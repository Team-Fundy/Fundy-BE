# Fundy-BE
Fundy의 스프링 서버입니다.
### 목차
[1. 개발 유의사항 ](#개발-유의사항)   
[2. 쉽게 서버 실행하기 ](#쉽게-서버-실행하기)

### 1. 개발 유의사항
Fundy-BE 개발 시 유의사항입니다.
#### 서버 설정 파일
`application.yml`과 같은 설정 파일은 보안 때문에 노션에서 따로 관리합니다.  
외부 인원들과 공유를 금합니다.   
노션 문서에서 `서버 설정 파일`을 참조합니다.
#### Mysql DB 컨테이너 연동하기
개발 시 사용할 DB를 도커를 활용해 구축하였습니다.
1. `application.yml`파일
   1. 노션에 있는 `application.yml - 개발`을 참조합니다.
   2. `/src/main/resources` 폴더에 `application.yml`을 생성하고 적힌 내용을 붙여놓습니다.
2. `Docker Desktop` 설치 및 실행
   1. https://www.docker.com/products/docker-desktop/ 에서 `Docker Desktop`을 설치합니다.
   2. `Docker Desktop`을 실행합니다. (Docker Desktop이 실행 중일 때만 서버를 실행할 수 있습니다.)
   - 만약 Docker만 설치한다면 서버 실행 명령어의 차이가 있을 수 있습니다.
3. 도커 컴포즈 명령어 실행
   1. 프로젝트 최상위 폴더에서 터미널을 켜주세요.
   2. `Docker Desktop`이 켜진 상태에서 터미널에 `docker compose up -d --build database` 명령어를 실행합니다.
   - 만약 IntelliJ를 사용중이면 쉽게 DB 컨테이너를 실행할 수 있습니다.


### 2. 쉽게 서버 실행하기 
서버 개발자가 아닌 사람들을 위한 가이드라인입니다.    
서버 개발자에는 빌드 후 서버를 실행하는 Fundy-Server 컨테이너는 불필요합니다. Fundy-DB만 실행해주세요.
1. `application.yml`파일
   1. 노션 문서에서 `서버 설정 파일`의 `application.yml - 개발`을 참조합니다.
   2. `/src/main/resources` 폴더에 `application.yml`을 생성하고 적힌 내용을 붙여놓습니다.
2. `Docker Desktop` 설치 및 실행
   1. https://www.docker.com/products/docker-desktop/ 에서 `Docker Desktop`을 설치합니다.
   2. `Docker Desktop`을 실행합니다. (Docker Desktop이 실행 중일 때만 서버를 실행할 수 있습니다.)
   - 만약 Docker만 설치한다면 서버 실행 명령어의 차이가 있을 수 있습니다.
3. 도커 컴포즈 명령어 실행
   1. 프로젝트 최상위 폴더에서 터미널을 켜주세요.
   2. `Docker Desktop`이 켜진 상태에서 터미널에 `docker compose up -d --build` 명령어를 실행합니다. (VCS의 도커 관련 익스텐션을 써도 괜찮습니다.)
   3. 서버가 빌드하고 실행하기 까지 시간이 걸리기 때문에 조금 기달려야합니다(1~3분: 실행환경에 따라 상₩)
      - 정확히 실행되었는지 확인하고 싶으면 `Docker Desktop`에서 모든 컨테이너가 실행 중인지와 `Fundy-Server` 컨테이너의 `Log`를 확인해주세요
      - `Fundy-Server` 컨테이너에서 `Log`에 `Started FundyBeApplication in 2.056 seconds (process running for 2.375)` 같은 것이 찍혀있으면 정상적으로 실행된 것입니다
4. 서버 실행 확인
    1. `localhost:3333`를 통해서 서버가 실행되었는지 확인해주세요. (Whitelabel Error Page도 괜찮습니다.)