package io.jenkins.plugins.workflows

import io.jenkins.plugins.workflows.helper.BuiltInHelper
import io.jenkins.plugins.workflows.helper.MapHelper
import io.jenkins.plugins.workflows.model.Configuration
import io.jenkins.plugins.workflows.parser.ConfigurationParser

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
                timeout(
                    time: this.config?.options?.timeout?.time ?: 1800,
                    unit: this.config?.options?.timeout?.unit ?: 'SECONDS'
                ) {
                    this.buildWorkflows(ctx)
                }
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
                this.config = ConfigurationParser.parse(ctx, yaml)
                List<String> environment = this.processEnvironment(ctx, MapHelper.merge(
                    BuiltInHelper.environment,
                    this.config?.environment ?: [:]
                ))
                environment.each { item ->
                    def (key, value) = item.split('=', 2)
                    ctx.env.setProperty(key, value)
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildActions(def ctx, List actions) {
        def script = {
            actions.each { action ->
                try {
                    if (action.shouldRun(ctx.env)) {
                        stage(action.name) {
                            if (action.type == 'approval') {
                                input(message: "Approval is required to proceed.")
                            } else {
                                withCredentials(
                                    this.processCredentials(ctx, action.getContextCredentials())
                                ) {
                                    withEnv(this.processEnvironment(ctx, MapHelper.merge(
                                        action.getContextEnvironment(),
                                        action.job.environment
                                    ))) {
                                        action.execute()
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    echo "Failed to parse action: ${e.message}"
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    List processCredentials(def ctx, List credentials) {
        if (!credentials as Boolean) return []
        return credentials.collect { credential ->
            ctx.invokeMethod(credential.type, credential.parameters)
        }
    }

    List<String> processEnvironment(def ctx, Map environment = this.config.environment) {
        if (!environment as Boolean) return []
        String script = '#!/usr/bin/env bash\n' << 
            environment.collect { k, v -> "$k=$v\necho $k=\$$k" }.join('\n') as String
        String output = ctx.sh(
            label: "Preparing environment variables",
            script: script,
            returnStdout: true
        ).trim()
        return output.split('\n') as List<String>
    }

}
