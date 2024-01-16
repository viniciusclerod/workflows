package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.helper.MapHelper
import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Step

class Command {

    def context
    def ctx

    String name
    Map parameters = [:]
    List<Step> steps = []

    def execute(def arguments) {
      if (this.steps.isEmpty()) {
        this.invoke(arguments)
      } else {
        this.steps.each { source ->
          Step target = new Step(source.properties.findAll { it.key != 'class' })
          this.ctx.echo "source.properties=${source.properties}\ntarget.properties=${target.properties}"
          // target.arguments.each {
          //   target.arguments[it.key] = this.parseAttrs([
          //       parameters: this.getMergedArgs(arguments)
          //   ], it.value)
          // }
          target.arguments = this.getMergedArgs(arguments)
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
      }
      context.invokeMethod(this.name, arguments)
    }

    def getMergedArgs(def arguments) {
      this.ctx.echo "arguments=${arguments} as ${arguments.getClass()}"
      Map defaultArgs = this.parameters.collectEntries { key, value ->
          ["${key}": value.default]
      }.findAll { it.value != null } ?: [:]
      switch (arguments) {
        case Map:
          // Map externalArgs = arguments.collectEntries { key, value ->
          //     String type = value.getClass().getSimpleName().toLowerCase()
          //     return ["${key}": (type == this.parameters[key]?.type) ? value : null]
          // }.findAll { it.value != null } ?: [:]
          if (defaultArgs) return MapHelper.merge(defaultArgs, arguments)
          break
        case NullObject:
          if (defaultArgs) return defaultArgs
          break
        case String:
        default:
          // TODO
          break
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
