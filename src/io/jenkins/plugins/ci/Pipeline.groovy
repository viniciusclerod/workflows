package io.jenkins.plugins.ci

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.parser.ConfigParser
import groovy.lang.GroovyShell

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
                this.buildBuiltIn(ctx)
                this.buildStages(ctx)
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
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildStages(def ctx) {
        def script = {
            stage('Schrödinger Cat') {
                this.config.steps.each { step ->
                    step.execute()
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildBuiltIn(def ctx) {
        def script = {
            def script(String code) {
                def shell new GroovyShell()
                shell.evaluate(code)
            }
        }
        script.delegate = ctx
        script.call()
        
    }

}
