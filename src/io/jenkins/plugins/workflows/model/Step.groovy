package io.jenkins.plugins.workflows.model

import io.jenkins.plugins.workflows.helper.MapHelper
import io.jenkins.plugins.workflows.model.Command

class Step {

    def ctx

    Command command
    def arguments

    def execute(def parameters = null) {
        // ctx.echo "step=(${this}) ${this.properties}"
        def arguments = this.arguments
        this.ctx.echo "arguments=${arguments} parameters=${parameters}"
        if (parameters) {
            arguments = this.parseArguments([ parameters: parameters ], arguments)
        }
        this.ctx.echo "arguments=${arguments}"
        this.command.execute(arguments)
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
        def pattern = /<<\s*(\S+\.\S+|\S+\(\s*\S+\s*\))\s*>>/
        return text.replaceAll(pattern) { key, match ->
            switch (match) {
                case ~/parameters\.\S+/:
                    def keyList = match.split("\\.")
                    def value = MapHelper.getValueByKeys(map, keyList)
                    return value
                case ~/include\(\s*\S+\s*\)/:
                    def filePath = match.replaceAll(/include\(\s*|\s*\)/, "")
                    File file = new File(filePath)
                    return file.text
            }
        }
    }

}