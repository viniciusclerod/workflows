package io.jenkins.plugins.ci

class Pipeline {

    // Configuration config
    
    // Pipeline(String yamlPath) {}

    def execute(def ctx) {
        def closure = {
            this.buildNode()
        }
        closure.delegate = ctx
        closure.call()
        
    }

    def buildNode() {
        node {
            stage('Setup') {
                echo "Executed (ctx is ${ctx.getClass()} class)"
                checkout scm
            }
            //   ci '.jenkins/config.yaml'
        }
    }

}
