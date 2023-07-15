pipeline {
    agent{
         docker {
            image 'jenkins/jenkins'
            args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
        }
    } 

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