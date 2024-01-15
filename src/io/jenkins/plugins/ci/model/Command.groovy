package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Step

class Command {

    String name
    Map parameters = [:]
    List<Step> steps = []

    def execute(def ctx, Map arguments = [:]) {
        ctx.invokeMethod(this.name, arguments)
    }

}
