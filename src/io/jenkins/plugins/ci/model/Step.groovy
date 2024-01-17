package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Command

class Step {

    Command command
    def arguments

    def execute(Map parameters) {
        def arguments = this.arguments
        if (parameters) {
            arguments = parseArguments([ parameters: parameters ])
        }
        this.command.execute(arguments)
    }

    def parseArguments(def context = this, def arguments = this.arguments) {
        switch (arguments) {
            case Map:
                arguments.collectEntries { it ->
                    value = it.value instanceof String 
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

    def parseArgument(def context = this, String text) {
        return text.replaceAll(/<<\s*([\S]+)\s*>>/) { match ->
            def keys = match[1].split("\\.")
            def value = context
            keys.each { key -> value = value[key] }
            return value
        }
    }

}