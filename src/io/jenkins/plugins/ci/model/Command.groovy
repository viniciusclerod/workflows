package io.jenkins.plugins.ci.model

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
          step.execute()
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

}
