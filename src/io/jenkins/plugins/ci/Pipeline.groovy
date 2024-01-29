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
                this.buildWorkflows(ctx)
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildWorkflows(def ctx) {
        def script = {
            Map workflows = this.config.workflows.collectEntries { key, workflow ->
                [(workflow.name): { this.buildActions(ctx, workflow.actions) }]
            }
            parallel(workflows)
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
                if (this.config.environment) {
                    List<String> environment = this.getEnvironment(ctx)
                    environment.each { item ->
                        def (key, value) = item.split('=', 2)
                        ctx.env.setProperty(key, value)
                    }
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildActions(def ctx, List actions) {
        def script = {
            actions.each { action ->
                if (action.shouldRun(ctx.env)) {
                    stage(action.name) {
                        if (action.type == 'approval') {
                            input(message: "Approval is required to proceed.")
                        } else {
                            withEnv(this.getEnvironment(ctx, action.job.environment)) {
                                action.execute()
                            }
                        }
                    }
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    List<String> getEnvironment(def ctx, Map environment = this.config.environment) {
        if (!environment as Boolean) return []
        String output = ctx.sh(
            label: "Preparing environment variables",
            script: environment.collect { k, v -> "set +x && $k=$v && echo $k=\$$k"}.join('\n'),
            returnStdout: true
        ).trim()
        return output.split('\n') as List<String>
    }

}
