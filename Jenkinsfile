pipeline {
    agent {
        node {label 'maven'}
    }
    environment {
        APPLICATION_NAME = 'foo'
        GIT_REPO="https://github.com/vgokul984/foo.git"
        GIT_BRANCH="master"
        STAGE_TAG = "promoteToQA"
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
        stage('Create Image Builder') {
            when {
                expression {
                    openshift.withCluster() {
                        openshift.withProject(DEV_PROJECT) {
                            openshift.raw("login", "--token='eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZXZlbG9wbWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqZW5raW5zLXRva2VuLTg1YzU5Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImplbmtpbnMiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJmMjA4ODIwZC0xYWQ0LTExZWEtOTJmNC0wYWZhZDMxNjg0NjkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGV2ZWxvcG1lbnQ6amVua2lucyJ9.T4JXPXlJy9nh7s27QGzFbj5M31otd7070XLWv9Lkj66QyPsUXLxkWykgHEK2F3gNNYcu0ksPt2Z66glcrGtrKVn6j3ZQFe2IXYaYGzQSBpRn6S0gc7C2kffVU3meETwmQNj6Uii5m0-cWltER3BOQp1egA10YaUwgkX8yOm2jQ9eUG_A5mxUQz00cX5tr9MWGKEzrn5NadhImOIR7n1dMr1190bY1Ns6EHjKghWan0Xgep3ogx1QuRZvhiYTxHnH6HrDCUFf5PzP4sLrEK6djxLpJKr8JPjeRVTR1cExJt3YgO3Du79pyzmOqK1KYEC8X__mcfVBb84mpvshS12VoA')
                            return !openshift.selector("bc", "${TEMPLATE_NAME}").exists();
                        }
                }
            }
        }
        steps {
            script {
                openshift.withCluster() {
                    openshift.withProject(DEV_PROJECT) {
                        openshift.raw("login", "--token='eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZXZlbG9wbWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqZW5raW5zLXRva2VuLTg1YzU5Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImplbmtpbnMiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJmMjA4ODIwZC0xYWQ0LTExZWEtOTJmNC0wYWZhZDMxNjg0NjkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGV2ZWxvcG1lbnQ6amVua2lucyJ9.T4JXPXlJy9nh7s27QGzFbj5M31otd7070XLWv9Lkj66QyPsUXLxkWykgHEK2F3gNNYcu0ksPt2Z66glcrGtrKVn6j3ZQFe2IXYaYGzQSBpRn6S0gc7C2kffVU3meETwmQNj6Uii5m0-cWltER3BOQp1egA10YaUwgkX8yOm2jQ9eUG_A5mxUQz00cX5tr9MWGKEzrn5NadhImOIR7n1dMr1190bY1Ns6EHjKghWan0Xgep3ogx1QuRZvhiYTxHnH6HrDCUFf5PzP4sLrEK6djxLpJKr8JPjeRVTR1cExJt3YgO3Du79pyzmOqK1KYEC8X__mcfVBb84mpvshS12VoA')
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
                            openshift.raw("login", "--token='eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZXZlbG9wbWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqZW5raW5zLXRva2VuLTg1YzU5Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImplbmtpbnMiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJmMjA4ODIwZC0xYWQ0LTExZWEtOTJmNC0wYWZhZDMxNjg0NjkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGV2ZWxvcG1lbnQ6amVua2lucyJ9.T4JXPXlJy9nh7s27QGzFbj5M31otd7070XLWv9Lkj66QyPsUXLxkWykgHEK2F3gNNYcu0ksPt2Z66glcrGtrKVn6j3ZQFe2IXYaYGzQSBpRn6S0gc7C2kffVU3meETwmQNj6Uii5m0-cWltER3BOQp1egA10YaUwgkX8yOm2jQ9eUG_A5mxUQz00cX5tr9MWGKEzrn5NadhImOIR7n1dMr1190bY1Ns6EHjKghWan0Xgep3ogx1QuRZvhiYTxHnH6HrDCUFf5PzP4sLrEK6djxLpJKr8JPjeRVTR1cExJt3YgO3Du79pyzmOqK1KYEC8X__mcfVBb84mpvshS12VoA')
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
                        openshift.raw("login", "--token='eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZXZlbG9wbWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqZW5raW5zLXRva2VuLTg1YzU5Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImplbmtpbnMiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJmMjA4ODIwZC0xYWQ0LTExZWEtOTJmNC0wYWZhZDMxNjg0NjkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGV2ZWxvcG1lbnQ6amVua2lucyJ9.T4JXPXlJy9nh7s27QGzFbj5M31otd7070XLWv9Lkj66QyPsUXLxkWykgHEK2F3gNNYcu0ksPt2Z66glcrGtrKVn6j3ZQFe2IXYaYGzQSBpRn6S0gc7C2kffVU3meETwmQNj6Uii5m0-cWltER3BOQp1egA10YaUwgkX8yOm2jQ9eUG_A5mxUQz00cX5tr9MWGKEzrn5NadhImOIR7n1dMr1190bY1Ns6EHjKghWan0Xgep3ogx1QuRZvhiYTxHnH6HrDCUFf5PzP4sLrEK6djxLpJKr8JPjeRVTR1cExJt3YgO3Du79pyzmOqK1KYEC8X__mcfVBb84mpvshS12VoA')
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
        stage('Promote to STAGE?') {
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
        stage('Rollout to STAGE') {
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
        stage('Scale in STAGE') {
            steps {
                script {
                    openshiftScale(namespace: "${STAGE_PROJECT}", deploymentConfig: "${TEMPLATE_NAME}", replicaCount: '3')
                }
            }
        }
    }
}
