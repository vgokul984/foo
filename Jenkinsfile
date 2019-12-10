node('maven') {
  stage 'build'
  openshiftBuild(buildConfig: 'foo', showBuildLogs: 'true')
  stage 'deploy'
  openshiftDeploy(deploymentConfig: 'foo')
}
