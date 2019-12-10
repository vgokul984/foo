node {
    stage 'Checkout'
    git url: "https://github.com/vgokul984/foo.git"  
    // Get the maven tool
    // ** NOTE: This 'M3' maven tool must be configured in the global configuration
    def mvnHome = tool 'm3'
    
    stage 'Build'
    sh "${mvnHome}/bin/mvn -f pom.xml clean install -DskipTests"
 
    stage 'Test'
    sh "${mvnHome}/bin/mvn -f pom.xml test"
}
