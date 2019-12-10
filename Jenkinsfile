
          try {
             timeout(time: 20, unit: 'MINUTES') {
                def appName="foo"
                def project=""
                node {
                  stage("Initialize") {
                    project = env.testing
                  }
                }
                node("maven") {
                  stage("Checkout") {
                    git url: "https://github.com/vgokul984/foo.git"
                  }
                  stage("Build WAR") {
                    sh "mvn clean package -Popenshift"
                  }
                }
                node {
                  stage("Build Image") {
                    def status = sh(returnStdout: true, script: "oc start-build foo -n testing")
                    def result = status.split("\n").find{ it.matches("^build.*started") }
                    
                    if(!result) {
                      echo "ERROR: No started build found for foo"
                      currentBuild.result = 'FAILURE'
                      return
                    }
                    
                    // result can be:
                    // - build "build-name" started
                    // - build build.build.openshift.io/build-name started
                    // - build "build.build.openshift.io/build-name" started
                    // Goal is to isolate "build-name"
                    def startedBuild = result.replaceAll("build [^0-9a-zA-Z]*", "").replaceAll("[^0-9a-zA-Z]* started", "").replaceFirst("^.*/", "")
                    echo "Build ${startedBuild} has started. Now watching it ..."
                    
                    timeout(time: 20, unit: 'MINUTES') {
                      openshift.withCluster() {
                        openshift.withProject() {
                          def build = openshift.selector('builds', "${startedBuild}")
                          build.untilEach {
                            def object = it.object()
                            if(object.status.phase == "Failed") {
                              error("Build ${startedBuild} failed")
                            }
                            return object.status.phase == "Complete"
                          }
                        }
                      }  
                    }
                  }
                  stage("Deploy") {
                    openshift.withCluster() {
                      openshift.withProject() {
                        def dc = openshift.selector('dc', "foo")
                        dc.rollout().status()
                      }
                    }
                  }
                }
             }
          } catch (err) {
             echo "in catch block"
             echo "Caught: ${err}"
             currentBuild.result = 'FAILURE'
             throw err
          }
