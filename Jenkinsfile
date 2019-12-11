pipeline {
  agent {
      label 'maven'
  }
  stages {
   stage('Deploy Previous') {
     steps {
       script {
         openshift.withCluster() {
           openshift.withProject() {
             def appName = "foo-app"
             def ver = openshift.selector('dc', appName).object().status.latestVersion //(1)
             println "Version: ${ver}"
             env.VERSION = ver
             openshift.tag("${appName}:latest", "${appName}:${ver}") //(2)
             def dcNew = openshift.newApp("--image-stream=${appName}:${ver}", "--name=${appName}-v${ver}").narrow('dc') //(3)
             def verNew = dcNew.object().status.latestVersion
             println "New deployment: ${verNew}"
             def rc = openshift.selector('rc', "sample-app-v${ver}-1")
             timeout(5) { //(4)
               rc.untilEach(1) {
                 def rcMap = it.object()
                 return (rcMap.status.replicas.equals(rcMap.status.readyReplicas))
            }
          }
        }
      }
    }
  }
}
}
}
