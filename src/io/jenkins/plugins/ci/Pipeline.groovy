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
        def closure = {
            node {
                this.buildSetupStage(ctx)
            }
        }
        closure.delegate = ctx
        closure.call()
    }

    def buildSetupStage(def ctx) {
        def closure = {
            stage('Setup') {
                checkout ctx.scm
                def yaml = readYaml file: this.yamlPath
                def config = ConfigParser.parse(ctx, yaml)
                echo "${config as Map}"
            }
        }
        closure.delegate = ctx
        closure.call()
    }

}
