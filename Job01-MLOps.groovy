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
                    sh 'sudo usermod -aG docker jenkins'
                    docker.image('chokchaifa/hellogo:latest').pull()
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                       docker.withDockerServer() {
                        def dockerContainer = docker.image('chokchaifa/hellogo:latest').run()
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