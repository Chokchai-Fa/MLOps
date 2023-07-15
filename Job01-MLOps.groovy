pipeline {
     agent any

    stages{
        stage('Pull Code') {
            steps{
                dir('src'){
                     sh 'git clone https://github.com/Chokchai-Fa/banking-go'
                }
            }
        }
        stage('Build Image and Push Image'){
            steps{
                   dir('src/banking-go'){
                    script{
                        def dockerImage = docker.build('chokchaifa/banking-go', '.')
                        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                            dockerImage.push()
                        }
                    }
            }
            }
        }
         
        stage('Pull Image'){
            steps{
                script{
                    docker.image('chokchaifa/banking-go:latest').pull()
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                       docker.image('chokchaifa/banking-go:latest').run('-d -p 3000:8080')
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