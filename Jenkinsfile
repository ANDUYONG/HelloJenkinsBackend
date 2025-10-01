pipeline {
	agent any
	
	environment {
		DEPLOY_DIR = "/Users/duyong/프로젝트/HelloJenkins/deploy/backend"
		JAR_NAME = "backend-0.0.1-SNAPSHOT.jar"
		PORT = 8090
	}
	
	stages {
		stage('Checkout') {
			steps {			
				git url: 'https://github.com/ANDUYONG/HelloJenkinsBackend.git', branch: 'main'
			}
		}
		
		stage('Build') {
			steps {
				sh './gradlew clean build'
			}
		}
		
		stage('Deploy') {
			steps {
				sh '''
		        # 포트 확인 후 기존 프로세스 종료
		        PID=\$(lsof -t -i:\$PORT || true)
		        if [ -n "\$PID" ]; then
		            kill \$PID || true
		            sleep 5
		            if ps -p \$PID > /dev/null; then
		                kill -9 \$PID || true
		            fi
		        fi
		
		        # 배포 디렉토리 생성
		        mkdir -p \$DEPLOY_DIR
		
		        # JAR 복사
		        cp build/libs/\$JAR_NAME \$DEPLOY_DIR/
		
		        # 백그라운드에서 안전하게 실행 (setsid 사용)
		        java -jar \$DEPLOY_DIR/\$JAR_NAME >> \$DEPLOY_DIR/app.log 2>&1 &
				'''
			}
		}
		
	}
	
	post {
		success {
			echo "배포 성공!"
		}
		
		failure {
			echo "배포 실패!"
		}
	}
}