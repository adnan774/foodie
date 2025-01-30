pipeline {
    agent any
    environment {
        IMAGE_NAME = "springboot-foodie-app"
        DB_IMAGE = "mysql:latest"
        DB_CONTAINER = "mysql-container"
        NETWORK_NAME = "foodie-network"
    }
    stages {
        stage('Get Code') {
            steps {
                echo "Cloning GitHub repository"
                git branch: 'master', url: 'https://github.com/adnan774/foodie.git'
            }
        }
        stage('Ensure Maven is Runnable') {
            steps {
                echo "Making mvnw executable"
                sh 'chmod a+x mvnw'
            }
        }
        stage('Build with Maven') {
            steps {
                echo "Building the project"
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Setup Docker Network') {
            steps {
                echo "Creating Docker network if not exists"
                sh '''
                if [ ! "$(docker network ls | grep ${NETWORK_NAME})" ]; then
                    docker network create ${NETWORK_NAME}
                fi
                '''
            }
        }
        stage('Start MySQL Container') {
            steps {
                echo "Starting MySQL Container"
                sh '''
                if [ ! "$(docker ps -q -f name=${DB_CONTAINER})" ]; then
                    docker run -d --name ${DB_CONTAINER} --network ${NETWORK_NAME} \
                        -e MYSQL_ROOT_PASSWORD=OrangeGrape123! \
                        -e MYSQL_DATABASE=foodie_db \
                        -p 3307:3306 ${DB_IMAGE}
                fi
                '''
            }
        }
        stage('Build Docker Image') {
            steps {
                echo "Building Docker image"
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }
        stage('Remove Existing Container') {
            steps {
                echo "Stopping and removing existing container"
                sh '''
                if [ "$(docker ps -q -f name=foodie-container)" ]; then
                    docker stop foodie-container
                    docker rm foodie-container
                fi
                '''
            }
        }
        stage('Run Docker Container') {
            steps {
                echo "Running Docker container"
                sh '''
                docker run -d --name foodie-container --network ${NETWORK_NAME} \
                    -p 8100:8080 ${IMAGE_NAME}
                '''
            }
        }
    }
}
