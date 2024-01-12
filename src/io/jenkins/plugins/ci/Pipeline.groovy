package io.jenkins.plugins.ci

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.parser.ConfigParser

class Pipeline {

    String yamlPath
    Configuration config

    Pipeline(String yamlPath) {
        this.yamlPath = yamlPath ?: '.jenkins/config.yaml'
    }

    def execute(def ctx) {
        this.buildPipeline(ctx)
    }

    def buildPipeline(def ctx) {
        def script = {
            node {
                this.buildSetupStage(ctx)
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildSetupStage(def ctx) {
        def script = {
            stage('Setup') {
                checkout ctx.scm
                def yaml = readYaml file: this.yamlPath
                this.config = ConfigParser.parse(ctx, yaml)
                echo "${this.config.commands}"
            }
        }
        script.delegate = ctx
        script.call()
    }

}
