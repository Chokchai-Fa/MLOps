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

        stage('Pull Image'){
            steps{
                script{
                    sh 'docker pull chokchaifa/hellogo:latest'
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                       docker.withDockerServer() {
                        sh 'docker run -d -p 3000:8080 chokchaifa/hellogo:latest'
                    }
                }
                
            }
        }
  
    }

      post {
        always{
            cleanWs()
        }
    }


}