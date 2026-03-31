# 베이스 이미지 지정
FROM amazoncorretto:21-alpine

# 컨테이너 안의 작업 디렉토리 설정
WORKDIR /app

# jar 복사
COPY core-api/build/libs/core-api-0.0.1-SNAPSHOT.jar app.jar

# 타임존 설정
ENV TZ=Asia/Seoul

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]