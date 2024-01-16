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
        this.invoke(this.getMergedArgs(arguments))
      } else {
        this.steps.each { source ->
          Step target = new Step(source.properties.findAll { it.key != 'class' })
          target.arguments.each {
            target.arguments[it.key] = this.parseAttrs([
                parameters: this.getMergedArgs(arguments)
            ], it.value)
          }
          target.execute()
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
          context.echo "arguments=${arguments}"
      }
      context.invokeMethod(this.name, arguments)
    }

    def getMergedArgs(def arguments) {
        if (arguments instanceof Map) {
          Map defaultArgs = this.parameters.collectEntries { key, value ->
              ["${key}": value.default]
          }.findAll { it.value != null } ?: [:]
          Map externalArgs = arguments.collectEntries { key, value ->
              String type = value.getClass().getSimpleName().toLowerCase()
              return ["${key}": (type == this.parameters[key]?.type) ? value : null]
          }.findAll { it.value != null } ?: [:]
          return MapHelper.merge(defaultArgs, arguments)
        }
        return arguments
    }

    def parseAttrs(def context = this, String text) {
        return text.replaceAll(/<<\s*([\S]+)\s*>>/) { match ->
            def keys = match[1].split("\\.")
            def value = context
            keys.each { key -> value = value[key] }
            return value
        }
    }

}
