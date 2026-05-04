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
        NEXUS_URL = "172.16.101.201:8081"
        NEXUS_DOCKER = "172.16.101.201:8083"
        SCANNER_HOME = tool 'sonar'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh """
                        ${SCANNER_HOME}/bin/sonar-scanner \
                        -Dsonar.projectKey=simple-web \
                        -Dsonar.projectName=simple-web \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.java.binaries=target
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 60, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Quality Gate Failed: ${qg.status}"
                        }
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

        stage('Tag Docker Image') {
            steps {
                sh """
                    docker tag ${DOCKER_IMAGE}:latest ${NEXUS_DOCKER}/${DOCKER_IMAGE}:latest
                """
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-docker-cred',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        docker login ${NEXUS_DOCKER} -u $DOCKER_USER -p $DOCKER_PASS
                        docker push ${NEXUS_DOCKER}/${DOCKER_IMAGE}:latest
                    """
                }
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
