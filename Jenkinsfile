 pipeline {
   agent any
     stages {
        stage('development: Build Tasks') {
           steps {
              openshiftBuild bldCfg: 'foo', namespace: 'development', showBuildLogs: 'true', verbose: 'false', waitTime: '', waitUnit: 'sec'
              openshiftVerifyBuild bldCfg: 'foo', checkForTriggeredDeployments: 'false', namespace: 'development', verbose: 'false', waitTime: ''
           }
        }
        stage('development: Tag Image') {
           steps {
              openshiftTag alias: 'false', destStream: 'foo', destTag: '${BUILD_NUMBER}', destinationAuthToken: '', destinationNamespace: 'development', namespace: 'development', srcStream: 'foo', srcTag: 'latest', verbose: 'false'
           }
        }
        stage('development: Deploy new image') {
           steps {
              openshiftDeploy depCfg: 'foo', namespace: 'development', verbose: 'false', waitTime: '', waitUnit: 'sec'
              openshiftVerifyDeployment depCfg: 'foo', namespace: 'development', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'true', waitTime: '', waitUnit: 'sec'
              openshiftVerifyService namespace: 'development', svcName: 'foo', verbose: 'false', retryCount: '5'
           }
        }
        stage('acknowledge production') {
           steps {
              timeout(time: 3, unit: 'MINUTES') {
                 input 'Ready for production?'
              }
           }
        }
        stage('production: Tag Image') {
           steps {
              openshiftTag alias: 'false', destStream: 'foo', destTag: '${BUILD_NUMBER}', destinationAuthToken: '', destinationNamespace: 'production', namespace: 'development', srcStream: 'foo', srcTag: 'latest', verbose: 'false'
           }
        }
        stage('production: Deploy new image') {
           steps {
              openshiftDeploy depCfg: 'foo', namespace: 'production', verbose: 'false', waitTime: '', waitUnit: 'sec'
              openshiftVerifyDeployment depCfg: 'foo', namespace: 'production', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'true', waitTime: '', waitUnit: 'sec'
              openshiftVerifyService namespace: 'production', svcName: 'foo', verbose: 'false', retryCount: '5'
           }
       }
    }
}
