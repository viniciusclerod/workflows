package io.jenkins.plugins.ci

class Pipeline {

    Configuration config

    def execute(def ctx) {
        this.buildNode(ctx)
    }

    def buildNode(def ctx) {
        def closure = {
            node {
                stage('Setup') {
                    def yaml = readYaml file: '.jenkins/config.yaml'
                    echo "${ctx.scm}=${ctx.scm.getProperties()}"
                    checkout ctx.scm
                }
                //   ci '.jenkins/config.yaml'
            }
        }
        closure.delegate = ctx
        closure.call()
    }

}
