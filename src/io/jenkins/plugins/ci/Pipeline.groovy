package io.jenkins.plugins.ci

class Pipeline {

    // Configuration config
    
    // Pipeline(String yamlPath) {}

    def execute(def ctx) {
        this.buildNode(ctx)
    }

    def buildNode(def ctx) {
        def closure = {
            node {
                stage('Setup') {
                    echo "Executed (ctx is ${ctx.getClass()} class)"
                    checkout scm
                }
                //   ci '.jenkins/config.yaml'
            }
        }
        closure.delegate = ctx
        closure.call()
    }

}
