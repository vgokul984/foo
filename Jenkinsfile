pipeline {
  agent {
      label 'maven'
  }
  stages {
    stage('Build App') {
      steps {
        sh "mvn install"
      }
    }
    stage('Create Image Builder') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector("bc", "foo").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newBuild("--name=foo", "--image-stream=foo:latest")
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "foo").startBuild
          }
        }
      }
    }
    stage('Promote to testing') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("foo:latest", "foo:testing")
          }
        }
      }
    }
    stage('Create testing') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'foo-testing').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("foo:latest", "--name=foo-testing").narrow('svc').expose()
          }
        }
      }
    }
    stage('Promote STAGE') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("foo:testing", "foo:stage")
          }
        }
      }
    }
    stage('Create STAGE') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'foo-stage').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("foo:stage", "--name=foo-stage").narrow('svc').expose()
          }
        }
      }
    }
  }
}
