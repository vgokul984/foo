pipeline {
  agent {
      label 'maven'
  }
  stages {
    stage('Build') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('bc', 'foo').exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp('foo:latest~https://github.com/vgokul984/foo.git')
          }
        }
      }
    }
  }
}
