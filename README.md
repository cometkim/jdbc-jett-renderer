# jdbc-jett-renderer

[JETT](http://jett.sourceforge.net)는 [JEXL 표현식](http://commons.apache.org/proper/commons-jexl)을 사용하는 엑셀 스프레드시트 템플릿 엔진입니다.

DB 분석서를 한땀한땀 써내려가기엔 인생이 너무 짧으므로 Spring Boot, JDBC, JETT를 활용하여 자동화합니다.

## 목표

DB 지원
- MySQL / MariaDB
- 다른 DB는 아직 추가 지원 계획 없음...

기능
- [x] DB 메타데이터 시트에 렌더링
- [x] DB 커넥션 테스트
- [ ] DB 테이블 별 시트 생성
- [ ] CLI 모드
- [ ] Docker 컨테이너

## 타입 정보

### Bean

시트에 전달되는 컨텍스트 정보

인터페이스: `Map<String, Object>`

| 이름 | 타입 | 설명 |
| ---- | ---- | ---- |
| jdbc | [`net.sf.jett.jdbc.JDBCExecutor`](http://jett.sourceforge.net/misc/jdbc_executor.html) |  |
| tables | `List<*Object>` | 테이블 목록, *Object 구조는 TableInfo로 정의 |

### TableInfo

인터페이스: `Map<String, Object>`

| 이름 | 타입 | 설명 |
| ---- | ---- | ---- |
| name | `String` | 테이블 이름 |
| comment | `String` | 테이블 커멘트 |
| columns | `List<*Object>` | 컬럼 목록, *Object 구조는 ColumnInfo로 정의 |

### ColumnInfo

인터페이스: `Map<String, Object>`

| 이름 | 타입 | 설명 |
| ---- | ---- | ---- |
| name | `String` | 컬럼 이름 |
| type | `String` | 컬럼 타입 이름 |
| comment | `String` | 컬럼 커멘트 |
| isNullable | `Boolean` | 컬럼 NULLABLE 여부 |
| isAutoIncrement | `Boolean` | 컬럼 AUTO_INCREMENT 여부 |
