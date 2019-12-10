
pipeline {
  /* Which container to bring up for the build. Pick one of the templates configured in Kubernetes plugin. */
  agent {
    label 'maven'
  }

  stages {
    stage('Pull Source') {
      steps {
        git url: 'https://github.com/vgokul984/foo.git', branch: 'master'
      }
    }
    stage('Unit Tests') {
      steps {
        sh 'mvn test'
      }
    }
    stage('Deploy to Nexus') {
      steps {
        sh 'mvn deploy -DskipTests'
      }
    }
  }
}
