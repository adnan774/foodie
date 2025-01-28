pipeline {
    agent any
    environment {
        IMAGE_NAME = "springboot-foodie-app"
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
        stage('Build Docker Image') {
            steps {
                echo "Building Docker image"
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }
        stage('Run Docker Container') {
            steps {
                echo "Running Docker container"
                sh 'docker run -d -p 8100:8080 ${IMAGE_NAME}'
            }
        }
    }
}
