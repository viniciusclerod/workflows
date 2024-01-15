package io.jenkins.plugins.ci.model

import io.jenkins.plugins.ci.model.Step

class Command {

    def context

    String name
    Map parameters = [:]
    List<Step> steps = []

    def execute(Map arguments = [:]) {
        this.context.invokeMethod(this.name, arguments)
    }

}
