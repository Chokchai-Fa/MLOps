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
                    docker.image('chokchaifa/hellogo:latest').pull()
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                       docker.image('chokchaifa/hellogo:latest').run('-d -p 3000:8080')
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