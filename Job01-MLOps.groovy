pipeline {
    agent any

    stages{
        stage('Build') {
            steps{
                git 'https://github.com/Chokchai-Fa/banking-go'

            }
        }
    post {
        always{
            script {
                sh 'ls ./src'
            }
            
        }
    }
    }



}