pipeline {
    agent any

    environment {
        IMAGE_NAME = "springboot-foodie-app"
        DB_IMAGE = "mysql:latest"
        DB_CONTAINER = "mysql-container"
        APP_CONTAINER = "foodie-container"
        NETWORK_NAME = "foodie-network"
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo "Cloning GitHub repository..."
                git branch: 'master', url: 'https://github.com/adnan774/foodie.git'
            }
        }

        stage('Build with Maven') {
            steps {
                echo "Making mvnw executable..."
                sh 'chmod +x mvnw'

                echo "Building the project with Maven..."
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Setup Docker Network') {
            steps {
                echo "Ensuring Docker network exists..."
                sh '''
                if [ ! "$(docker network ls | grep ${NETWORK_NAME})" ]; then
                    docker network create ${NETWORK_NAME}
                fi
                '''
            }
        }

        stage('Start MySQL Container') {
            steps {
                echo "Starting MySQL container..."
                sh '''
                if [ "$(docker ps -aq -f name=${DB_CONTAINER})" ]; then
                    echo "Stopping and removing existing MySQL container..."
                    docker stop ${DB_CONTAINER} || true
                    docker rm ${DB_CONTAINER} || true
                fi

                echo "Launching new MySQL container..."
                docker run -d --name ${DB_CONTAINER} --network ${NETWORK_NAME} \
                    -e MYSQL_ROOT_PASSWORD=OrangeGrape123! \
                    -e MYSQL_DATABASE=foodie_db \
                    -p 3306:3306 \
                    --health-cmd="mysqladmin ping --silent" \
                    --health-interval=10s --health-timeout=5s --health-retries=5 \
                    ${DB_IMAGE}
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image for backend..."
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Run Backend Application') {
            steps {
                echo "Waiting for MySQL to be ready..."
                sh '''
                until docker exec ${DB_CONTAINER} mysqladmin ping --silent; do
                    echo "MySQL is not ready yet. Retrying in 5 seconds..."
                    sleep 5
                done

                echo "Stopping and removing existing backend container..."
                if [ "$(docker ps -aq -f name=${APP_CONTAINER})" ]; then
                    docker stop ${APP_CONTAINER} || true
                    docker rm ${APP_CONTAINER} || true
                fi

                echo "Running backend application..."
                docker run -d --name ${APP_CONTAINER} --network ${NETWORK_NAME} \
                    -p 8100:8080 ${IMAGE_NAME}

                sleep 5

                if [ "$(docker ps -aq -f name=${APP_CONTAINER})" ]; then
                    echo "Backend container is running."
                else
                    echo "Backend container failed! Printing logs..."
                    docker logs ${APP_CONTAINER} || echo "No logs found."
                    exit 1
                fi
                '''
            }
        }

        stage('Verify Application is Running') {
            steps {
                echo "Checking if the application is running..."
                sh '''
                sleep 10
                if curl -s http://localhost:8100/actuator/health | grep "UP"; then
                    echo "Application is running successfully!"
                else
                    echo "Application failed to start. Printing logs..."
                    docker logs ${APP_CONTAINER}
                    exit 1
                fi
                '''
            }
        }
    }

    post {
    success {
        script {
            def backendIP = sh(script: '''hostname -I | awk '{print $1}' ''', returnStdout: true).trim()
            echo "Pipeline executed successfully!"
            echo "API is available at: http://${backendIP}:8100"
        }
    }
    failure {
        echo "Pipeline failed. Check logs for errors."
    }
}


}
