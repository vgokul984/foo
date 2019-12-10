pipeline {
  agent {
    // Using the maven builder agent
    label "maven"
  }
  stages {
    // Checkout Source Code and calculate Version Numbers and Tags
    stage('Checkout Source') {
      steps {
        git url: "https://github.com/vgokul984/foo.git"
       script {
          def pom = readMavenPom file: 'pom.xml'
          def version = pom.version
        }
      }
    }
    // Using Maven build the war file
    // Do not run tests in this step
    stage('Build App') {
      steps {
        echo "Building war file"
        sh "mvn clean package -DskipTests=true"
      }
    }
	stage 'Deploy'
    def builder = new com.openshift.jenkins.plugins.pipeline.OpenShiftBuilder("", "foo", "demo", "", "", "", "", "true", "", "")
    step builder
  }
} 
