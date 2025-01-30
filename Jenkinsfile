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
        sh 'docker build --memory=512m --memory-swap=1024m -t ${IMAGE_NAME} .'
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

                echo "Running backend application..."
                docker run -d --name ${APP_CONTAINER} --network ${NETWORK_NAME} \
                    -p 8080:8080 ${IMAGE_NAME}
                '''
            }
        }

        stage('Verify Application is Running') {
            steps {
                echo "Checking if the application is running..."
                sh 'docker ps'
            }
        }
    }

    post {
        success {
            script {
                def ec2IP = sh(script: "curl -s ifconfig.me", returnStdout: true).trim()
                echo "Pipeline executed successfully!"
                echo "API is available at: http://${ec2IP}:8080"
            }
        }
        failure {
            echo "Pipeline failed. Check logs for errors."
        }
    }
}
