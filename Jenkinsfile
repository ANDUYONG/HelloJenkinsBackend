pipeline {
	agent any
	
	environment {
		// Docker 이미지 및 컨테이너 관리용 변수
		DOCKER_IMAGE_NAME = "hello-jenkins-backend:${env.BUILD_ID}" // 빌드 번호를 태그로 사용
		CONTAINER_NAME = "hello-jenkins-backend-container"
		GITHUB_TOKEN = credentials('github-token')
		PORT = 8092 // 외부/내부 포트
	}
	
	stages {
		stage('Checkout') {
			steps {			
				// GitHub 저장소에서 소스 코드 가져오기
				git url: 'https://github.com/ANDUYONG/HelloJenkinsBackend.git', branch: 'main'
			}
		}
		
		stage('Build Application') {
			steps {
				// Gradle을 사용하여 애플리케이션 JAR 파일 생성
				sh './gradlew clean bootJar'
			}
		}

		stage('Build Docker Image') {
			steps {
				script {
					// Dockerfile과 빌드된 JAR 파일을 포함하여 Docker 이미지 빌드
					// -t: 태그 설정
					// .: 현재 디렉토리의 Dockerfile 사용 
					sh "docker build -t ${DOCKER_IMAGE_NAME} ."
					
					// (선택 사항) 최신 태그도 함께 지정
					sh "docker tag ${DOCKER_IMAGE_NAME} hello-jenkins-backend:latest"
				}
			}
		}

		stage('Deploy Container') {
			steps {
				sh '''
				echo "--- 기존 컨테이너 중지 및 제거 ---"
				# 기존 컨테이너를 중지합니다 (에러 발생 시 무시 || true)
				docker stop ${CONTAINER_NAME} || true
				# 중지된 컨테이너를 제거합니다 (에러 발생 시 무시 || true)
				docker rm ${CONTAINER_NAME} || true
				
				# 컨테이너가 완전히 시작될 시간을 잠시 기다립니다 (중요)
				sleep 5

				echo "--- 새 Docker 컨테이너 실행 ---"
				# 새로운 이미지로 컨테이너를 실행합니다.
				# -d: 백그라운드 실행
				# --name: 컨테이너 이름 지정
				# -p: 호스트 포트(왼쪽)와 컨테이너 포트(오른쪽) 매핑
				docker run -d \
				  --name ${CONTAINER_NAME} \
				  -e GITHUB_TOKEN=${GITHUB_TOKEN} \
				  -p ${PORT}:${PORT} \
				  ${DOCKER_IMAGE_NAME}
				
				echo "배포된 컨테이너 정보:"
				docker ps | grep ${CONTAINER_NAME}
				'''
			}
		}
	}
	
	post {
		success {
			echo "배포 성공! 이미지: ${DOCKER_IMAGE_NAME}, 컨테이너: ${CONTAINER_NAME}"
		}
		
		failure {
			echo "배포 실패! 빌드 또는 Docker 과정 확인 필요."
		}
	}
}
