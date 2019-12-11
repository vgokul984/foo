pipeline {
  agent any
  stages {
    stage('Build') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('bc', 'mapit-spring').exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp('java:11~https://github.com/vgokul984/foo.git')
          }
        }
      }
    }
  }
}
