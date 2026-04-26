@Library('my-shared-lib') _

pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build (Shared Library)') {
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

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                nexusArtifactUploader(
                    artifacts: [[
                        artifactId: 'simple-web-app',
                        classifier: '',
                        file: 'target/simple-web-app-1.0.jar',
                        type: 'jar'
                    ]],
                    credentialsId: 'nexus-cred',
                    groupId: 'com.example',
                    nexusUrl: 'localhost:8081',
                    nexusVersion: 'nexus3',
                    repository: 'maven-releases',
                    version: '1.0'
                )
            }
        }

        //  NEW: Docker Build stage
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t java-app:latest .'
                }
            }
        }

        //  NEW: Run Docker container
        stage('Run Docker Container') {
            steps {
                script {
                    sh 'docker rm -f java-app || true'
                    sh 'docker run -d -p 8085:8080 --name java-app java-app:latest'
                }
            }
        }
    }
}
