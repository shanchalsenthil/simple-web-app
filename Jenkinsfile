@Library('my-shared-lib') _

pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                script {
                    buildApp()
                }
            }
        }
    }
}
