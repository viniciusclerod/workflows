package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.helper.MapHelper
import io.jenkins.plugins.ci.model.Command

class Step {

    Command command
    def arguments

    def execute(def parameters = null) {
        def arguments = this.arguments
        if (parameters) {
            arguments = this.parseArguments([ parameters: parameters ], arguments)
        }
        this.command.execute(arguments)
    }

    def parseArguments(def context, def arguments) {
        switch (arguments) {
            case Map:
                return arguments.collectEntries { it ->
                    def value = it.value instanceof String ? this.parseArgument(context, it.value) : it.value
                    return [(it.key): value]
                }
            case String:
                return this.parseArgument(context, arguments)
            default:
                return arguments
        }
    }

    def parseArgument(def context, String text) {
        return text.replaceAll(/<<\s*([\S]+)\s*>>/) { match ->
            def keyList = match[1].split("\\.")
            def value = MapHelper.getValueByKeys(context, keyList)
            return value
        }
    }

}