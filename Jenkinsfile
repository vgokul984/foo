node('maven') {
  stage 'build & deploy in dev'
  openshiftBuild(namespace: 'testing',
  			    buildConfig: 'foo',
			    showBuildLogs: 'true',
			    waitTime: '3000000')
  
  stage 'verify deploy in dev'
  openshiftVerifyDeployment(namespace: 'development',
				       depCfg: 'foo',
				       replicaCount:'1',
				       verifyReplicaCount: 'true',
				       waitTime: '300000')
  
  stage 'deploy in test'
  openshiftTag(namespace: 'development',
  			  sourceStream: 'foo',
			  sourceTag: 'latest',
			  destinationStream: 'foo',
			  destinationTag: 'promoteQA')
			  
  stage 'Deploy to production'
  timeout(time: 2, unit: 'DAYS') {
      input message: 'Approve to production?'
 }

  openshiftTag(namespace: 'development',
  			  sourceStream: 'foo',
			  sourceTag: 'promoteQA',
			  destinationStream: 'foo',
			  destinationTag: 'promotePRD')

  
  openshiftDeploy(namespace: 'production',
  			     deploymentConfig: 'foo',
			     waitTime: '300000')
  
  openshiftScale(namespace: 'production',
  			     deploymentConfig: 'foo',
			     waitTime: '300000',
			     replicaCount: '2')
  
  stage 'verify deploy in production'
  openshiftVerifyDeployment(namespace: 'production',
				       depCfg: 'foo',
				       replicaCount:'2',
				       verifyReplicaCount: 'true',
				       waitTime: '300000')
}
