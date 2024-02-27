package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.helper.MapHelper
import io.jenkins.plugins.workflows.model.Command

class Step {

    def context

    Command command
    def arguments

    def execute(def parameters = null) {
        try {
            def arguments = this.arguments
            arguments = this.parseArguments([ parameters: parameters ?: [:] ], arguments)
            this.context.echo "[${this.command}] command.execute(${arguments})"
            this.command.execute(arguments)
        } catch (Exception e) {
            this.context.echo "step.execute() ERROR: ${e.properties}"
        }
    }

    def parseArguments(Map map, def arguments) {
        switch (arguments) {
            case Map:
                return arguments.collectEntries { it ->
                    def value = it.value instanceof String ? this.parseArgument(map, it.value) : it.value
                    return [(it.key): value]
                }
            case String:
                return this.parseArgument(map, arguments)
            default:
                return arguments
        }
    }

    def parseArgument(Map map, String text) {
        def pattern = /<<\s*(\S+?\.\S+?|\S+?\(\s*\S+?\s*\))\s*>>/
        return text.replaceAll(pattern) { it ->
            def (key, match) = it
            switch (match) {
                case ~/parameters\.\S+/:
                    def keyList = match.split("\\.")
                    def value = MapHelper.getValueByKeys(map, keyList)
                    return value
                case ~/include\(\s*\S+\s*\)/:
                    def filePath = match.replaceAll(/include\(\s*|\s*\)/, "")
                    def value = this.context.readFile(file: filePath)
                    return value
            }
        }
    }

}