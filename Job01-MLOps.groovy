pipeline {
     agent any

    stages{
        stage('Pull Code') {
            steps{
                dir('src'){
                     sh 'https://github.com/Chokchai-Fa/MLOps-Project01o'
                }
            }
        }
        stage('Build Image and Push Image to Regisrty'){
            steps{
                   dir('src/MLOps-Project01'){
                    script{
                        def dockerImage = docker.build('chokchaifa/mlops-test', '.')
                        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                            dockerImage.push()
                        }
                    }
            }
            }
        }
         
        stage('Pull Image Stage'){
            steps{
                script{
                    docker.image('chokchaifa/mlops-test').pull()
                }
            }
        }

        stage('Deploy Stage'){
            steps{
                script{
                    def containerName = 'ml-ops01'
                    def existingContainerId = sh(returnStdout: true, script: "docker ps -aqf \"name=${containerName}\"").trim()
                    if (!existingContainerId.isEmpty()) {
                        sh "docker stop ${existingContainerId}"
                        sh "docker rm ${existingContainerId}"
                    }
                    docker.image('chokchaifa/mlops-test:latest').run('-dit --name mlops-test -d --gplus all ')
                }
            }
                
            }


        stage('PreProcessing Stage'){
            steps{
                script{
                  sh 'docker exec -it mlops-test  python3 preprocessing.py'
                }
            } 
            }

        

        stage('Train Stage'){
            steps{
                script{
                  sh 'docker exec -it mlops-test  python3 train.py'
                }
            } 
            }
            
        stage('Test Stage'){
        steps{
            script{
                sh 'docker exec -it mlops-test  python3 test.py'
                sh 'docker exec -it mlops-test  cat /results/train_metadata.json /results/test_metadata.json'
            }
        } 
        }
        
      post {
        always{
          cleanWs()
        }
    }

    }
}