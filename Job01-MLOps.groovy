pipeline {
    agent any

    stages{
        stage('Pull Code') {
            steps{
                sh 'pwd'
                dir('src'){
                     sh 'git clone https://github.com/Chokchai-Fa/banking-go'
                }
            }
        }
  
    }

      post {
        always{
            script {
                sh 'ls'
            }
            
        }
    }


}