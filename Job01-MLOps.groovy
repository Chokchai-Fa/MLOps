pipeline {
     agent any

    stages{
        stage('Pull Code') {
            steps{
                dir('src'){
                     sh 'git clone https://github.com/Chokchai-Fa/go-hello'
                }
            }
        }
        stage('Build Image and Push Image to Regisrty'){
            steps{
                   dir('src/go-hello'){
                    script{
                        def dockerImage = docker.build('chokchaifa/go-hello', '.')
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
                    docker.image('chokchaifa/go-hello:latest').pull()
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                        sh 'docker stop hello-go'
                       docker.image('chokchaifa/go-hello:latest').run('--name hello-go -d -p 3000:8080')
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