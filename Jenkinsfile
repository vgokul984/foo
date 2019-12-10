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
      steps {
        script {
          openshift.withCluster() {
            openshift.newBuild("--name=java:11", "--image-stream=foo-git:latest", "--binary")
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "foo").startBuild("--from-file=target/foo-spring.jar", "--wait")
          }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("foo:latest", "foo:testing")
          }
        }
      }
    }
    stage('Create DEV') {
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
