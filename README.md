# developers-member
Developers 프로젝트의 사용자 관련 서비스 Backend 저장소입니다.

## Project packaging

```
com.developers.member
 ┣ 📂config
 ┣ 📂constant       
 ┣ 📂problem
 ┃ ┣ 📂entity
 ┃ ┣ ┣ 📃Member
 ┃ ┣ 📂dto
 ┃ ┣ 📂service
 ┃ ┣ 📂repository
 ┃ ┣ ┣ 📃MemberRepository
 ┃ ┗ 📂controller
```

## 개발환경 포트
- [Member] 사용자 서비스: 9000
- [Solve] 문제 풀이 서비스: 9001
- [Live] 화상 채팅 서비스: 9002
- [LiveSession] 화상 채팅 시그널링 서비스: 9003
- [Community] 커뮤니티 서비스: 9004
- MariaDB: 3306
- Redis: 6379

## 협업 전략
1. [Git Fork](https://jooneys-portfolio.notion.site/GIt-0f7a34fbaf584deaa0e561de46f3542d) 전략을 통해 업스트림 저장소에 PR을 올린다.
2. PR 리뷰어들은 PR을 리뷰하고 PR을 승인한다.
3. 리뷰어들의 승인을 받으면 업스트림 저장소에 올린 PR이 Merge되는 방식으로 진행한다.
4. 코드 리뷰를 성심성의껏 진행한다.
5. !
