pipeline {
    agent {
        node {label 'maven'}
    }
    environment {
        APPLICATION_NAME = 'foo'
        GIT_REPO="https://github.com/vgokul984/foo.git"
        GIT_BRANCH="master"
        STAGE_TAG = "testing"
        DEV_PROJECT = "development"
        STAGE_PROJECT = "testing"
        TEMPLATE_NAME = "foo"
        PORT = 8081;
    }
    stages {
        stage('Get Latest Code') {
            steps {
                git branch: "${GIT_BRANCH}", url: "${GIT_REPO}"
            }
        }
        stage('Build App') {
            steps {
              sh "mvn install"
           }
          }
    stage('test[unit&quality]') {
        parallel 'unit-test': {
            node {
                unstash 'source'
                sh 'mvn -Dmaven.test.failure.ignore=true test'
                step([$class: 'JUnitResultArchiver', testResults: 'TEST-*.xml'])
                if(currentBuild.result == 'UNSTABLE'){
                    error "Unit test failures"
            }
        }
    }'quality-test': {
        node {
            unstash 'source'
            sh 'mvn sonar:sonar'
        } 
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
        stage('Deploy to DEV') {
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
        stage('Promote to Production?') {
            steps {
                timeout(time:15, unit:'MINUTES') {
                    input message: "Promote to STAGE?", ok: "Promote"
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
}
