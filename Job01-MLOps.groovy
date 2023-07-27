pipeline {
     agent any

    stages{
        stage('Pull Code') {
            steps{
                dir('src'){
                     sh 'git clone https://github.com/Chokchai-Fa/MLOps-Project01'
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
                    def containerName = 'ml-opstest01'
                    def existingContainerId = sh(returnStdout: true, script: "docker ps -aqf \"name=${containerName}\"").trim()
                    if (!existingContainerId.isEmpty()) {
                        sh "docker stop ${existingContainerId}"
                        sh "docker rm ${existingContainerId}"
                    }
                    docker.image('chokchaifa/mlops-test:latest').run('-dit --name ml-opstest01 -d --gpus all ')
                }
            }
                
            }


        stage('PreProcessing Stage'){
            steps{
                script{
                  sh 'docker exec ml-opstest01  python3 preprocessing.py'
                }
            } 
            }

        

        stage('Train Stage'){
            steps{
                script{
                  sh 'docker exec ml-opstest01  python3 train.py'
                }
            } 
            }
            
        stage('Test Stage'){
        steps{
            script{
                sh 'docker exec ml-opstest01  python3 test.py'
                sh 'docker exec ml-opstest01  cat /results/train_metadata.json /results/test_metadata.json'
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