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
                stage('Test Commands') {
                    echo "${this.config.commands}" 
                    // this.config.commands.sh.execute(ctx, [
                    //     label: "Hello Command",
                    //     script: "echo Hello"
                    // ])
                }
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
                this.config = ConfigParser.parse(ctx, yaml)
                echo "${this.config as Map}"
            }
        }
        closure.delegate = ctx
        closure.call()
    }

}
