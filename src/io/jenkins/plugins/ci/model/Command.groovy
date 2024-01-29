package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.helper.MapHelper
import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Step

class Command {

    def context

    String name
    Map parameters = [:]
    List<Step> steps = []

    def execute(def arguments) {
        if (this.steps.isEmpty()) {
            this.invoke(arguments)
        } else {
            this.steps.each { step ->
                def parameters = this.getParameters(arguments)
                step.execute(parameters)
            }
        }
    }

    def invoke(def arguments) {
        def context
        switch (this.context) {
        case Configuration:
            context = this.context.commands
            break
        default:
            context = this.context
            context.echo "[command] arguments=${arguments}"
        }
        context.invokeMethod(this.name, arguments)
    }

    def getParameters(def arguments) {
        def parameters = this.parameters.collectEntries { key, value ->
            [(key): value.default]
        }.findAll { it.value != null } ?: [:]
        if (!arguments as Boolean) return parameters
        switch (arguments) {
            case Map:
                // Map externalArgs = arguments.collectEntries { key, value ->
                //     String type = value.getClass().getSimpleName().toLowerCase()
                //     return ["${key}": (type == this.parameters[key]?.type) ? value : null]
                // }.findAll { it.value != null } ?: [:]
                return MapHelper.merge(parameters, arguments, { a, b -> b ?: a })
            default:
                return arguments
        }
    }

}
