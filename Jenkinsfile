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
                echo "Checking MySQL Container"
                sh '''
                if [ "$(docker ps -aq -f name=${DB_CONTAINER})" ]; then
                    echo "Stopping and removing existing MySQL container..."
                    docker stop ${DB_CONTAINER} || true
                    docker rm ${DB_CONTAINER} || true
                fi

                echo "Starting a fresh MySQL container..."
                docker run -d --name ${DB_CONTAINER} --network ${NETWORK_NAME} \
                    -e MYSQL_ROOT_PASSWORD=OrangeGrape123! \
                    -e MYSQL_DATABASE=foodie_db \
                    -p 3306:3306 \
                    --health-cmd="mysqladmin ping --silent" \
                    --health-interval=10s --health-timeout=5s --health-retries=3 \
                    ${DB_IMAGE}
                '''
            }
        }
        stage('Build Docker Image') {
            steps {
                echo "Building Docker image"
                sh 'docker build -t ${IMAGE_NAME} --no-cache .'
            }
        }
        stage('Run Backend') {
            steps {
                echo "Waiting for MySQL..."
                sh '''
                until docker exec ${DB_CONTAINER} mysqladmin ping --silent; do
                    echo "MySQL is not ready yet. Retrying in 5 seconds..."
                    sleep 5
                done
                
                echo "Running backend container..."
                docker run -d --name foodie-container --network ${NETWORK_NAME} -p 8100:8080 ${IMAGE_NAME}
                '''
            }
        }
    }
}
