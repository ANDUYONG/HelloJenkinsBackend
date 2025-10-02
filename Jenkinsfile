pipeline {
	agent any
	
	environment {
		DEPLOY_DIR = "/Users/duyong/프로젝트/HelloJenkins/deploy/backend"
		JAR_NAME = "backend-0.0.1-SNAPSHOT.jar"
		SCREEN_NAME="HelloJenkinsBackend"
		PLIST_FILE = "${env.HOME}/Library/LaunchAgents/com.duyong.hello-jenkins.plist"
		GITHUB_TOKEN = credentials('github-token')
		PORT = 8091
		// PORT = 8090
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
		        # 기존 애플리케이션 중지
	            launchctl unload $PLIST_FILE || true
	
	            # JAR 파일 복사 
	            mkdir -p $DEPLOY_DIR
	            cp build/libs/$JAR_NAME $DEPLOY_DIR/
	
	            # LaunchAgent 재시작
	            launchctl load $PLIST_FILE
	            launchctl start com.duyong.hello-jenkins || true
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