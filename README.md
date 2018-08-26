# jdbc-jett-renderer

[JETT](http://jett.sourceforge.net)은 [JEXL 표현식](http://commons.apache.org/proper/commons-jexl)을 지원하는 엑셀 스프레드시트 템플릿 엔진입니다.

DB 분석서를 한땀한땀 써내려가기엔 인생이 너무 짧으므로 Spring Boot, JDBC, JETT를 활용하여 자동화합니다.

## 컨텍스트 정보
| 이름 | 타입 |
| ---- | ---- |
| jdbc | [net.sf.jett.jdbc.JDBCExecutor](http://jett.sourceforge.net/misc/jdbc_executor.html) |
| tables | List<Map<String, Object\*>> |

TableInfo 오브젝트\*
- name: 테이블 명
- columns: List<Map<String, Object\*>>

ColumnInfo 오브젝트\*
- name: 컬럼 명
