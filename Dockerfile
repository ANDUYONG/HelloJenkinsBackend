# --- 1. 빌더 스테이지 (Builder Stage) ---
# 기존 alpine 기반 gradle 이미지는 호환성 문제 가능성이 있어, 
# 더 범용적인 Eclipse Temurin JDK 이미지를 사용합니다.
FROM eclipse-temurin:17-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper, 설정 파일 복사
COPY gradlew .
COPY gradle gradle

# 프로젝트 소스 복사
COPY src src
COPY build.gradle settings.gradle .

# 빌드 실행
# --no-daemon: 빌드 속도 향상 (CI 환경)
RUN ./gradlew clean bootJar --no-daemon

# --- 2. 런타임 스테이지 (Runtime Stage) ---
# 애플리케이션 실행 환경 (경량 JDK 17 JRE)
FROM eclipse-temurin:17-jre

# 환경 변수 설정
ENV PORT 8092
ENV JAR_FILE app.jar
# 프로젝트의 build/libs 디렉토리에 생성되는 실제 JAR 파일 이름으로 변경해야 합니다.
# 예: backend-0.0.1-SNAPSHOT.jar 대신, 배포에 사용할 이름을 지정합니다.

# 빌더 스테이지에서 생성된 JAR 파일 복사
# build/libs 폴더 아래의 파일 이름을 확인하고 정확히 매칭되도록 수정하세요. 
COPY --from=builder /app/build/libs/*.jar $JAR_FILE

# 포트 노출 (Docker 컨테이너 내부 포트)
EXPOSE $PORT

# 컨테이너 실행 시 동작할 명령어
ENTRYPOINT ["java", "-jar", "/hello-jenkins-backend.jar", "--server.port=${PORT}"]
# ENTRYPOINT에서 실행 시 포트도 환경 변수로 설정되도록 추가했습니다.