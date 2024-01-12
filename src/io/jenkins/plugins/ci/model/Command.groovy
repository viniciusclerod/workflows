package io.jenkins.plugins.ci.model

class Command {

    String name
    String description
    Map parameters = [:]
    List<Map> steps = []

    def execute(def ctx, Map arguments = [:]) {
        ctx.invokeMethod(this.name, arguments)
    }

}
