node('maven') {
  stage('build & deploy') {
    openshiftBuild bldCfg: 'foo',
      namespace: 'development',
      showBuildLogs: 'true'
    openshiftVerifyDeployment depCfg: 'foo',
      namespace: 'development'
  }
  stage('approval (test)') {
    input message: 'Approve for testing?',
      id: 'approval'
  }
  stage('deploy to test') {
    openshiftTag srcStream: 'foo',
      namespace: 'development',
      srcTag: 'latest',
      destinationNamespace: 'testing',
      destTag: 'test'
    openshiftVerifyDeployment depCfg: 'foo',
      namespace: 'testing'
  }
  stage('approval (production)') {
    input message: 'Approve for production?',
      id: 'approval'
  }
  stage('deploy to production') {
    openshiftTag srcStream: 'fooapp',
      namespace: 'development',
      srcTag: 'latest',
      destinationNamespace: 'production',
      destTag: 'prod'
    openshiftVerifyDeployment depCfg: 'fooapp',
      namespace: 'production'
  }
}
