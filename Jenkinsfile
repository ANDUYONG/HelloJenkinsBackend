pipeline {
	agent any
	
	environment {
		DEPLOY_DIR = "/Users/duyong/deploy/backend"
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
				PID=$(lsof -t -i:$PORT || true)  # lsof 실패해도 무시
				if [ -n "$PID" ]; then
				    kill $PID || true
				    sleep 5
				    if ps -p $PID > /dev/null; then
				        kill -9 $PID || true
				    fi
				fi
				mkdir -p $DEPLOY_DIR
				cp build/libs/$JAR_NAME $DEPLOY_DIR/
				nohup java -jar $DEPLOY_DIR/$JAR_NAME >> $DEPLOY_DIR/app.log 2>&1 &
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