#서블릿 공부용 리포지토리

java8
tomcat8.0

##이 리포지토리에 담은 내용
1. 서블릿 web.xml과 Filter상속으로 doFilter구현 확인.
2. 어노테이션을 작성을 통해 Get리퀘스트의 URI를 분석하여 리퀘스트를 처리하도록 수정
3. 싱글턴(이 부분은 넷 참고)

###그외
-첫 커밋 기준으로 Get리퀘스트를 간단하게 처리하는 기능만 구현됨.
Tomcat을 실행하여 아래 예제로 확인 가능
→http://localhost:8080/servletTest/message?name=myname