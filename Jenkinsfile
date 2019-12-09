node('maven') {
  stage('build & deploy') {
    openshiftBuild bldCfg: 'foo',
      namespace: 'Dev',
      showBuildLogs: 'true'
    openshiftVerifyDeployment depCfg: 'foo',
      namespace: 'Dev'
  }
  stage('approval (test)') {
    input message: 'Approve for testing?',
      id: 'approval'
  }
  stage('deploy to test') {
    openshiftTag srcStream: 'foo',
      namespace: 'Dev',
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
      namespace: 'Dev',
      srcTag: 'latest',
      destinationNamespace: 'production',
      destTag: 'prod'
    openshiftVerifyDeployment depCfg: 'foo',
      namespace: 'production'
  }
}
