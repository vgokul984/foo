pipeline {
  agent {
      label 'maven'
  }
  stage('Checkout Source') {
      steps {
        git url: "https://github.com/vgokul984/foo.git"
       script {
          def pom = readMavenPom file: 'pom.xml'
          def version = pom.version
        }
      }
    }
  stages {
    stage('Build App') {
      steps {
	    echo "Building war file"
        sh "mvn clean package -DskipTests=true"
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
            openshift.newBuild("--name=foo", "--image-stream=foo:latest", "--binary")
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "foo").startBuild("--from-file=target/foo.jar", "--wait")
          }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("foo:latest", "foo:development")
          }
        }
      }
    }
    stage('Create DEV') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'foo-development').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("foo:latest", "--name=foo-development").narrow('svc').expose()
          }
        }
      }
    }
    stage('Promote STAGE') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("foo:development", "foo:stage")
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
