package io.jenkins.plugins.ci

class Pipeline {

    // Configuration config
    
    // Pipeline(String yamlPath) {}

    def execute(def ctx) {
        this.buildNode(ctx)
    }

    def buildNode(def ctx) {
        ctx.node {
            ctx.stage('Setup') {
                ctx.echo "Executed (ctx is ${ctx.getClass()} class)"
                ctx.checkout scm
            }
            //   ci '.jenkins/config.yaml'
        }
    }

}
