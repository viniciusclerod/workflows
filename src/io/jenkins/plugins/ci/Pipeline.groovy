package io.jenkins.plugins.ci

class Pipeline {

    Map config

    def execute(def ctx) {
        this.buildNode(ctx)
    }

    def buildNode(def ctx) {
        def closure = {
            node {
                this.buildStage(ctx)
            }
        }
        closure.delegate = ctx
        closure.call()
    }

    def buildStage(def ctx) {
        def closure = {
            stage('Setup') {
                checkout ctx.scm
                def yaml = readYaml file: '.jenkins/config.yaml'
            }
        }
        closure.delegate = ctx
        closure.call()
    }

}
