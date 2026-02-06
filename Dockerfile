# 1. Java 21 베이스 이미지 (가볍고 빠른 Alpine 리눅스 기반)
FROM eclipse-temurin:21-jdk-alpine

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 4. 애플리케이션 실행
# -Dspring.profiles.active=prod : prod 프로필 강제 적용
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]