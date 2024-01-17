package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Command

class Step {

    Command command
    def arguments

    def execute(def parameters, def ctx) {
        def arguments = this.arguments
        if (parameters) {
            ctx.echo "parameters=${parameters}"
            arguments = this.parseArguments([ parameters: parameters ], arguments)
            ctx.echo "parameters=${parameters}"
        }
        this.command.execute(arguments)
    }

    def parseArguments(def context, def arguments) {
        switch (arguments) {
            case Map:
                arguments.collectEntries { it ->
                    def value = it.value instanceof String 
                        ? this.parseArgument(context, it.value) 
                        : it.value
                    return ["${it.key}": value]
                }
                break
            case String:
                return this.parseArgument(context, arguments)
            default:
                return arguments
        }
    }

    def parseArgument(def context, String text) {
        return text.replaceAll(/<<\s*([\S]+)\s*>>/) { match ->
            def keys = match[1].split("\\.")
            def value = context
            keys.each { key -> value = value[key] }
            return value
        }
    }

}