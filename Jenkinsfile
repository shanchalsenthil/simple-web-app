@Library('my-shared-lib') _

pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        APP_NAME = "simple-web-app"
        VERSION = "1.0"
        DOCKER_IMAGE = "java-app"
        NEXUS_URL = "localhost:8081"
        NEXUS_DOCKER = "localhost:8083"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                script {
                    buildApp()
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh 'mvn clean verify sonar:sonar'
                }
            }
        }

        // ✅ BETTER APPROACH (FIXED QUALITY GATE)
        stage('Quality Gate') {
            steps {
                script {
                    def qg = waitForQualityGate()
                    echo "Quality Gate Status: ${qg.status}"

                    if (qg.status != 'OK') {
                        error "Pipeline failed due to Quality Gate: ${qg.status}"
                    }
                }
            }
        }

        stage('Deploy JAR to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUS_URL}",
                    credentialsId: 'nexus-cred',
                    groupId: 'com.example',
                    version: "${VERSION}",
                    repository: 'maven-releases',
                    artifacts: [[
                        artifactId: "${APP_NAME}",
                        classifier: '',
                        file: "target/${APP_NAME}-${VERSION}.jar",
                        type: 'jar'
                    ]]
                )
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Run Docker Container') {
            steps {
                sh """
                    docker rm -f ${DOCKER_IMAGE} || true
                    docker run -d -p 8085:8080 --name ${DOCKER_IMAGE} ${DOCKER_IMAGE}:latest
                """
            }
        }

        stage('Push Docker Image (Nexus)') {
            steps {
                sh """
                    docker login ${NEXUS_DOCKER} -u admin -p admin123
                    docker tag ${DOCKER_IMAGE}:latest ${NEXUS_DOCKER}/${DOCKER_IMAGE}:latest
                    docker push ${NEXUS_DOCKER}/${DOCKER_IMAGE}:latest
                """
            }
        }
    }

    post {
        success {
            echo "PIPELINE SUCCESS"
        }
        failure {
            echo "PIPELINE FAILED"
        }
    }
}
