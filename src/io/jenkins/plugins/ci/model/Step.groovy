package io.jenkins.plugins.ci.model

class Step {

    Command command
    Map arguments

    def execute(def ctx) {
        this.command.execute(this.arguments)
    }

}