package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.model.Step

class Command {

    def context

    String name
    Map parameters = [:]
    List<Step> steps = []

    def execute(Map arguments = [:]) {
      switch (this.context) {
        case Configuration:
          this.context.commands.invokeMethod(this.name, arguments)
          break
        default:
          this.context.invokeMethod(this.name, arguments)
      }
    }

}
