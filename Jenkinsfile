pipeline {
  agent any
  stages {
    stage('Get Latest Code') {
      steps {
        git(branch: "${GIT_BRANCH}", url: "${GIT_REPO}")
      }
    }

    stage('test[unit]') {
      steps {
        sh '/bin/bash -c "mvn -s pom.xml -B clean test"'
      }
    }

    stage('Build App') {
      steps {
        sh 'mvn install'
      }
    }

    stage('Create Image Builder') {
      when {
        expression {
          openshift.withCluster() {
            openshift.withProject(DEV_PROJECT) {
              return !openshift.selector("bc", "${TEMPLATE_NAME}").exists();
            }
          }
        }

      }
      steps {
        timeout(time: 15, unit: 'MINUTES') {
          input(message: 'Test passed Build and Deploy Application?', ok: 'Deploy')
        }

        script {
          openshift.withCluster() {
            openshift.withProject(DEV_PROJECT) {
              openshift.newBuild("--name=${TEMPLATE_NAME}", "--image-stream=java:11", "--binary=true")
            }
          }
        }

      }
    }

    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject(env.DEV_PROJECT) {
              openshift.selector("bc", "$TEMPLATE_NAME").startBuild("--from-file=target/foo-0.0.1-SNAPSHOT.jar",  "--wait=true")
            }
          }
        }

      }
    }

    stage('Deploy to Development') {
      when {
        expression {
          openshift.withCluster() {
            openshift.withProject(env.DEV_PROJECT) {
              return !openshift.selector('dc', "${TEMPLATE_NAME}").exists()
            }
          }
        }

      }
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject(env.DEV_PROJECT) {
              def app = openshift.newApp("${TEMPLATE_NAME}:latest")
              app.narrow("svc").expose();
              def dc = openshift.selector("dc", "${TEMPLATE_NAME}")
              while (dc.object().spec.replicas != dc.object().status.availableReplicas) {
                sleep 10
              }
            }
          }
        }

      }
    }

    stage('Promote to production?') {
      steps {
        timeout(time: 15, unit: 'MINUTES') {
          input(message: 'Promote to production?', ok: 'Promote')
        }

        script {
          openshift.withCluster() {
            openshift.tag("${DEV_PROJECT}/${TEMPLATE_NAME}:latest", "${STAGE_PROJECT}/${TEMPLATE_NAME}:${STAGE_TAG}")
          }
        }

      }
    }

    stage('Rollout to production') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject(STAGE_PROJECT) {
              if (openshift.selector('dc', '${TEMPLATE_NAME}').exists()) {
                openshift.selector('dc', '${TEMPLATE_NAME}').delete()
                openshift.selector('svc', '${TEMPLATE_NAME}').delete()
                openshift.selector('route', '${TEMPLATE_NAME}').delete()
              }
              openshift.newApp("${TEMPLATE_NAME}:${STAGE_TAG}").narrow("svc").expose()
            }
          }
        }

      }
    }

  }
  environment {
    APPLICATION_NAME = 'foo'
    GIT_REPO = 'https://github.com/vgokul984/foo.git'
    GIT_BRANCH = 'master'
    STAGE_TAG = 'production'
    DEV_PROJECT = 'development'
    STAGE_PROJECT = 'production'
    TEMPLATE_NAME = 'foo'
    PORT = 8081
  }
}